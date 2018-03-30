package test.svg.svg;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
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
    private float scalePointX;
    private float scalePointY;
    private Matrix matrix;
    private float mLastGestureX;
    private float mLastGestureY;
    private int NORMAL;
    float lastFocusX;
    float lastFocusY;
    private float originX; // current position of viewport
    private float originY;

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
        matrix = new Matrix();
        MIN_ZOOM = 1;
        MAX_ZOOM = 3;
        normalizedScale = 1;
        mode = NORMAL;
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
        originX = 0f;
        originY = 0f;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);

        return true;
    }


    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.scale(scaleFactor, scaleFactor, scalePointX, scalePointY);

        mapBackground.draw(canvas);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            lastFocusX = detector.getFocusX();
            lastFocusY = detector.getFocusY();

            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float currentScaleFactor = detector.getScaleFactor();
            float focusX = detector.getFocusX();
            float focusY = detector.getFocusY();


            if (scaleFactor * currentScaleFactor > MIN_ZOOM &&
                    scaleFactor * currentScaleFactor < MAX_ZOOM) {
                scaleFactor *= detector.getScaleFactor();
                scaleFactor = Math.max(MIN_ZOOM, Math.min(scaleFactor, MAX_ZOOM));


                scalePointX = (int) ((getScrollX() + focusX));
                scalePointX = Math.min(Math.max(scalePointX, 0), mapWidth);

                scalePointY = (int) ((getScrollY() + focusY));
                scalePointY = Math.min(Math.max(scalePointY, 0), mapHeight);
                matrix.postScale(scaleFactor, scaleFactor, scalePointX, scalePointY);

                invalidate();
            }
            return true;
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