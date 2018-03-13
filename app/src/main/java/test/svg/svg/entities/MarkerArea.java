package test.svg.svg.entities;

import java.util.List;

public class MarkerArea {
    private float startX;
    private float endX;
    private float startY;
    private float endY;
    private float markerWidth;
    private float markerHeight;
    private String photoUrl;
    private String markerType;
    private String markerName;
    private boolean isEvent;
    private List<Event> eventList;


    public MarkerArea(float startX, float endX, float startY, float endY, float markerWidth,
                      float markerHeight, String photoUrl, String markerType, String markerName,
                      boolean isEvent, List<Event> eventList) {
        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;
        this.markerWidth = markerWidth;
        this.markerHeight = markerHeight;
        this.photoUrl = photoUrl;
        this.markerType = markerType;
        this.markerName = markerName;
        this.isEvent = isEvent;
        this.eventList = eventList;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    public boolean isEvent() {
        return isEvent;
    }

    public void setEvent(boolean event) {
        isEvent = event;
    }

    public String getMarkerType() {
        return markerType;
    }

    public void setMarkerType(String markerType) {
        this.markerType = markerType;
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

    public float getStartX() {
        return startX;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public float getEndX() {
        return endX;
    }

    public void setEndX(float endX) {
        this.endX = endX;
    }

    public float getStartY() {
        return startY;
    }

    public void setStartY(float startY) {
        this.startY = startY;
    }

    public float getEndY() {
        return endY;
    }

    public void setEndY(float endY) {
        this.endY = endY;
    }

    public float getMarkerWidth() {
        return markerWidth;
    }

    public void setMarkerWidth(float markerWidth) {
        this.markerWidth = markerWidth;
    }

    public float getMarkerHeight() {
        return markerHeight;
    }

    public void setMarkerHeight(float markerHeight) {
        this.markerHeight = markerHeight;
    }
}