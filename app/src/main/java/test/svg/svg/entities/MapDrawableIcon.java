package test.svg.svg.entities;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MapDrawableIcon extends Drawable {
    private float coordinatesFloatX;
    private float coordinatesFloatY;
    private float markerScale;
    private String photoUrl;
    private String markerType;
    private String markerName;
    private boolean isEvent;

    public MapDrawableIcon(float coordinatesFloatX, float coordinatesFloatY, float markerScale,
                           String photoUrl, String markerType, String markerName, boolean isEvent) {
        this.coordinatesFloatX = coordinatesFloatX;
        this.coordinatesFloatY = coordinatesFloatY;
        this.markerScale = markerScale;
        this.photoUrl = photoUrl;
        this.markerType = markerType;
        this.markerName = markerName;
        this.isEvent = isEvent;
    }

    public boolean isEvent() {
        return isEvent;
    }

    public void setEvent(boolean event) {
        isEvent = event;
    }

    public String getMarkerName() {
        return markerName;
    }

    public void setMarkerName(String markerName) {
        this.markerName = markerName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public float getCoordinatesFloatX() {
        return coordinatesFloatX;
    }

    public void setCoordinatesFloatX(float coordinatesFloatX) {
        this.coordinatesFloatX = coordinatesFloatX;
    }

    public float getCoordinatesFloatY() {
        return coordinatesFloatY;
    }

    public void setCoordinatesFloatY(float coordinatesFloatY) {
        this.coordinatesFloatY = coordinatesFloatY;
    }

    public String getMarkerType() {
        return markerType;
    }

    public void setMarkerType(String markerType) {
        this.markerType = markerType;
    }

    public float getMarkerScale() {
        return markerScale;
    }

    public void setMarkerScale(float markerScale) {
        this.markerScale = markerScale;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
    }

    @SuppressLint("WrongConstant")
    @Override
    public int getOpacity() {
        return 0;
    }
}
