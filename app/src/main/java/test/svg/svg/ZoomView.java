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
import test.svg.svg.entities.MapDrawableIcon;
import test.svg.svg.entities.MarkerArea;


public class ZoomView extends android.support.v7.widget.AppCompatImageView {
    private static float MIN_ZOOM;
    private static float MAX_ZOOM;
    private static float normalizedScale;
    private float scaleFactor;
    private float minScaleFactor;
    private ScaleGestureDetector detector;
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
    private List<MapDrawableIcon> mapDrawableList;
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
        scaleFactor = 1.f;
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
        detector = new ScaleGestureDetector(getContext(), new ScaleListener());
        gestureDetector = new GestureDetector(getContext(), new MyGestureListener());
        displayHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        displayWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        mapHeight = mapBackground.getIntrinsicHeight();
        mapWidth = mapBackground.getIntrinsicWidth();
        controller = new Controller();
        mapDrawableList = controller.getList();
        paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);
    }

    // set markers to map
    private void initializeMap(Canvas canvas) {
        markerAreas = new ArrayList<>();

        for (int i = 0; i < mapDrawableList.size(); i++) {
            Bitmap markerBitmap = createBitmap(mapDrawableList.get(i));

            float posX = getCoordinate(mapWidth,
                    mapDrawableList.get(i).getCoordinatesFloatX());
            float posY = getCoordinate(mapHeight,
                    mapDrawableList.get(i).getCoordinatesFloatY());

            float markerScaleFactor = mapDrawableList.get(i).getMarkerScale();
            float markerWidth = markerBitmap.getWidth() * markerScaleFactor;
            float markerHeight = markerBitmap.getHeight() * markerScaleFactor;

            scaleBitmap = Bitmap.createScaledBitmap(markerBitmap, (int) (markerWidth /
                    scaleFactor), (int) (markerHeight / scaleFactor), true);

            canvas.drawBitmap(scaleBitmap, posX, posY, paint);

            markerAreas.add(new MarkerArea(posX - markerWidth, posX + markerWidth,
                    posY - markerHeight, posY + markerHeight, markerWidth, markerHeight,
                    mapDrawableList.get(i).getPhotoUrl(),
                    mapDrawableList.get(i).getMarkerType(),
                    mapDrawableList.get(i).getMarkerName(),
                    mapDrawableList.get(i).isEvent()));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mode = DRAG;
                startX = event.getX() - previousTranslateX;
                startY = event.getY() - previousTranslateY;

                // Get click coordinate on finger down
                float clickPositionX = event.getX();
                float clickPositionY = event.getY();
                float startAreaX;
                float endAreaX;
                float startAreaY;
                float endAreaY;

                for (int i = 0; i < markerAreas.size(); i++) {
                    startAreaX = markerAreas.get(i).getStartX() - distancesX;
                    endAreaX = markerAreas.get(i).getEndX() - distancesX;
                    startAreaY = markerAreas.get(i).getStartY();
                    endAreaY = markerAreas.get(i).getEndY();

                    if ((clickPositionX >= startAreaX && clickPositionX <= endAreaX) &&
                            (clickPositionY >= startAreaY && clickPositionY <= endAreaY)) {
                        showDialogs(markerAreas.get(i), startAreaX, endAreaY);
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
        detector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);

        if ((mode == DRAG && scaleFactor != normalizedScale && dragged) || mode == ZOOM) {
            invalidate();
        }
        return true;
    }

    // show dialogs
    private void showDialogs(MarkerArea markerArea, float startAreaX, float endAreaY) {
        dialog = new PreviewDialog(getContext(), markerArea.getPhotoUrl(),
                startAreaX + markerArea.getMarkerWidth() / 1.5f,
                endAreaY + markerArea.getMarkerHeight());

        bottomSheetDialog = markerArea.isEvent() ?
                new BottomSheetSpoilerHint(getContext(), markerArea.getMarkerName(), dialog) :
                new BottomSheetHint(getContext(), markerArea.getMarkerName(), dialog);
        bottomSheetDialog.show();
    }

    /**
     * centering the map when selecting a marker
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
        }

        canvas.scale(scaleFactor, scaleFactor);

        if ((translateX * -normalizedScale) < 0) {
            translateX = 0;
        } else if ((translateX * -normalizedScale) > (scaleFactor - normalizedScale) * mapWidth) {
            translateX = (normalizedScale - scaleFactor) * mapWidth;
        }
        if (translateY * -normalizedScale < 0) {
            translateY = 0;
        } else if ((translateY * -normalizedScale) > (scaleFactor - normalizedScale) *
                mapHeight) {
            translateY = (normalizedScale - scaleFactor) * mapHeight;
        }

        canvas.translate(translateX / scaleFactor, translateY / scaleFactor);
        canvas.save();
        mapBackground.draw(canvas);
        initializeMap(canvas);
    }


    // convert marker type to bitmap source
    private Bitmap createBitmap(MapDrawableIcon mapDrawableIcon) {
        MarkersDisplay markersDisplay = MarkersDisplay.POINTER;
        markersDisplay = markersDisplay.getIndexByType(mapDrawableIcon.getMarkerType());
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
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(MIN_ZOOM, Math.min(scaleFactor, MAX_ZOOM));
            return true;
        }
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (getScrollX() + distanceX < (mapWidth * minScaleFactor - displayWidth) &&
                    getScrollX() + distanceX > 0) {
                scrollBy((int) distanceX, 0);
                distancesX = getScrollX();
            }
            return true;
        }
    }
}
