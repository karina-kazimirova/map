package test.svg.svg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import java.util.ArrayList;
import java.util.List;
import test.svg.svg.dialogs.BottomSheetHint;
import test.svg.svg.dialogs.BottomSheetSpoilerHint;
import test.svg.svg.dialogs.PreviewDialog;
import test.svg.svg.entities.Event;
import test.svg.svg.entities.Marker;
import test.svg.svg.entities.MarkerArea;


public class ZoomView extends android.support.v7.widget.AppCompatImageView {
    private static float MIN_ZOOM;
    private static float MAX_ZOOM;
    private static float normalizedScale;
    private float scaleFactor;
    private float minScaleFactor;
    private ScaleGestureDetector scaleDetector;
    private GestureDetector gestureDetector;
    private static int NONE;
    private static int DRAG;
    private static int ZOOM;
    private Bitmap scaleBitmap;
    private Drawable mapBackground;
    private int mode;
    boolean dragged;
    private float startX;
    private float startY;
    private float translateX;
    private float translateY;
    private float previousTranslateX;
    private float previousTranslateY;
    private List<Marker> markersList;
    private List<MarkerArea> markerAreas;
    private Controller controller;
    private Paint paint;
    private int mapWidth;
    private int mapHeight;
    private float displayHeight;
    private float displayWidth;
    private float distancesX;
    private float distancesY;
    private PreviewDialog dialog;
    private BottomSheetDialog bottomSheetDialog;
    private List<Event> eventList;

    public ZoomView(Context context, Drawable mapBackground) {
        super(context);
        this.mapBackground = mapBackground;
        init();
    }

    public ZoomView(Context context, @Nullable AttributeSet attrs, Drawable mapBackground) {
        super(context, attrs);
        this.mapBackground = mapBackground;
        init();
    }

    public ZoomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
                    Drawable mapBackground) {
        super(context, attrs, defStyleAttr);
        this.mapBackground = mapBackground;
        init();
    }

    private void init() {
        MIN_ZOOM = 1;
        MAX_ZOOM = 3;
        normalizedScale = 1;
        scaleFactor = 1f;
        minScaleFactor = 1;
        distancesX = 0;
        distancesY = 0;
        NONE = 0;
        DRAG = 1;
        ZOOM = 2;
        startX = 0f;
        startY = 0f;
        translateX = 0f;
        translateY = 0f;
        previousTranslateX = 0f;
        previousTranslateY = 0f;
        scaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
        gestureDetector = new GestureDetector(getContext(), new MyGestureListener());
        displayHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        displayWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        mapHeight = mapBackground.getIntrinsicHeight();
        mapWidth = mapBackground.getIntrinsicWidth();
        controller = new Controller();
        markersList = controller.getList();
        paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);
    }


    // set markers to map
    private void initializeMap(Canvas canvas) {
        markerAreas = new ArrayList<>();

        for (int i = 0; i < markersList.size(); i++) {
            Bitmap markerBitmap = createBitmap(markersList.get(i));

            float posX = getCoordinate(mapWidth,
                    markersList.get(i).getCoordinatesFloatX());
            float posY = getCoordinate(mapHeight,
                    markersList.get(i).getCoordinatesFloatY());

            float markerScaleFactor = markersList.get(i).getMarkerScale();
            float markerWidth = markerBitmap.getWidth() * markerScaleFactor;
            float markerHeight = markerBitmap.getHeight() * markerScaleFactor;

            scaleBitmap = Bitmap.createScaledBitmap(markerBitmap, (int) (markerWidth /
                    scaleFactor), (int) (markerHeight / scaleFactor), true);

            canvas.drawBitmap(scaleBitmap, posX, posY, paint);

            //get list of events by this marker
            eventList = controller.getEventByMarkerId(markersList.get(i).getMarkerId());

            markerAreas.add(new MarkerArea(posX - markerWidth, posX + markerWidth,
                    posY - markerHeight, posY + markerHeight, markerWidth, markerHeight,
                    markersList.get(i).getPhotoUrl(),
                    markersList.get(i).getMarkerType(),
                    markersList.get(i).getMarkerName(),
                    markersList.get(i).isEvent(), eventList));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX() - previousTranslateX;
                startY = event.getY() - previousTranslateY;

                float startAreaX;
                float endAreaX;
                float startAreaY;
                float endAreaY;

                for (int i = 0; i < markerAreas.size(); i++) {
                    startAreaX = markerAreas.get(i).getStartX() * scaleFactor - distancesX;
                    endAreaX = markerAreas.get(i).getEndX() * scaleFactor - distancesX;
                    startAreaY = markerAreas.get(i).getStartY() * scaleFactor - distancesY;
                    endAreaY = markerAreas.get(i).getEndY()* scaleFactor - distancesY;

                    if ((event.getX() >= startAreaX && event.getX() <= endAreaX) &&
                            (event.getY() >= startAreaY && event.getY() <= endAreaY)) {
                        showDialogs(markerAreas.get(i), startAreaX, endAreaY,
                                markerAreas.get(i).getEventList());
                        scrollByCenter(startAreaX + markerAreas.get(i).getMarkerWidth()
                                * 1.5f);
                    }
                }
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                mode = ZOOM;
                previousTranslateX = distancesX;
                previousTranslateY = translateY;

                break;

            case MotionEvent.ACTION_MOVE:
                double distance;
                translateX = event.getX() - startX;
                translateY = event.getY() - startY;
                distance = Math.sqrt(Math.pow(event.getX() - (startX + previousTranslateX),
                        normalizedScale * 2) + Math.pow(event.getY() -
                        (startY + previousTranslateY), normalizedScale * 2));
                if (distance > 0) {
                    dragged = true;
                }
                break;

            case MotionEvent.ACTION_UP:
                mode = NONE;
                dragged = false;
                previousTranslateX = translateX;
                previousTranslateY = translateY;
                break;

            case MotionEvent.ACTION_POINTER_UP:
                mode = DRAG;
                previousTranslateX = translateX;
                previousTranslateY = translateY;
                break;
        }
        scaleDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);

        if ((mode == DRAG && scaleFactor != normalizedScale && dragged) || mode == ZOOM) {
            invalidate();
        }
        return true;
    }

    // show dialogs
    private void showDialogs(MarkerArea markerArea, float startAreaX, float endAreaY,
                             List<Event> eventList) {
        dialog = new PreviewDialog(getContext(), markerArea.getPhotoUrl(),
                startAreaX + markerArea.getMarkerWidth() / 1.5f,
                endAreaY + markerArea.getMarkerHeight());

        bottomSheetDialog = markerArea.isEvent() ?
                new BottomSheetSpoilerHint(getContext(), markerArea.getMarkerName(), dialog,
                        eventList) :
                new BottomSheetHint(getContext(), markerArea.getMarkerName(), dialog);
        bottomSheetDialog.show();
    }

    /**
     * centering the map when selecting a marker
     *
     * @param markerPosX position of marker along the x-axis
     */
    private void scrollByCenter(float markerPosX) {
        int centerPoint = (int) markerPosX - ((int) displayWidth / 2);
        int maxScroll = (int) ((int) (mapWidth * minScaleFactor) - displayWidth);
        int scrollFactor = getScrollX() + centerPoint;

        if (scrollFactor < maxScroll) {
            if (scrollFactor > 0) {
                scrollBy(centerPoint, 0);
                distancesX = getScrollX();
            } else if (scrollFactor < 0) {
                scrollBy(-getScrollX(), 0);
                distancesX = getScrollX();
                // scaleFactor = 1.2f;
            }
        } else {
            scrollBy(maxScroll - getScrollX(), 0);
            distancesX = getScrollX();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        if (displayHeight < mapHeight) {
            minScaleFactor = displayHeight / mapHeight;
            canvas.scale(minScaleFactor, minScaleFactor);
        } else {
            canvas.scale(scaleFactor, scaleFactor);
        }
        mapBackground.draw(canvas);
        initializeMap(canvas);
    }

    // convert marker type to bitmap source
    private Bitmap createBitmap(Marker marker) {
        MarkersDisplay markersDisplay = MarkersDisplay.POINTER;
        markersDisplay = markersDisplay.getIndexByType(marker.getMarkerType());
        return BitmapFactory.decodeResource(getResources(), markersDisplay.getMarkerId());
    }

    // convert coordinates, received in float to px
    public int getCoordinate(int imageParam, float coordinatesFloat) {
        int scalePercent = (int) ((imageParam / 100) * (scaleFactor - normalizedScale));
        int valuePX = (imageParam) * getPercent(coordinatesFloat) / 100;
        return valuePX + scalePercent;
    }

    // convert float to int
    private int getPercent(float parseCoordinate) {
        return (int) (100 * parseCoordinate);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float currentScaleFactor = detector.getScaleFactor();
            float focusX = detector.getFocusX();
            float focusY = detector.getFocusY();

            if (scaleFactor * currentScaleFactor > MIN_ZOOM &&
                    scaleFactor * currentScaleFactor < MAX_ZOOM) {
                scaleFactor *= detector.getScaleFactor();
                scaleFactor = Math.max(MIN_ZOOM, Math.min(scaleFactor, MAX_ZOOM));

                int scrollX = (int) ((getScrollX() + focusX) * currentScaleFactor - focusX);
                scrollX = Math.min(Math.max(scrollX, 0), mapWidth);

                int scrollY = (int) ((getScrollY() + focusY) * currentScaleFactor - focusY);
                scrollY = Math.min(Math.max(scrollY, 0), mapHeight);

                scrollTo(scrollX, scrollY);
                invalidate();
            }
            return true;
        }
        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            distancesX = getScrollX();
            distancesY = getScrollY();
        }
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            int scaledMapWidth = (int) ((mapWidth * minScaleFactor) * scaleFactor);
            int scaledMapHeight = (int) ((mapHeight * minScaleFactor) * scaleFactor);
            if (getScrollX() + distanceX < (scaledMapWidth - displayWidth)
                    && getScrollX() + distanceX > 0) {
                scrollBy((int) distanceX, 0);
                distancesX = getScrollX();
            }
            if (getScrollY() + distanceY < (scaledMapHeight - displayHeight)
                    && getScrollY() + distanceY > 0) {
                scrollBy(0, (int) distanceY);
                distancesY = getScrollY();
            }
            invalidate();
            return true;
        }
    }
}
