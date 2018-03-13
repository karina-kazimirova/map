package test.svg.svg.entities;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Event {
    private String eventTitle;
    private String eventDescription;
    private Timestamp startTime;
    private Timestamp endTime;
    private ArrayList<String> categoryNameList;
    private boolean isFavorite;

    public Event(String eventTitle, String eventDescription, Timestamp startTime,
                 Timestamp endTime, ArrayList<String> categoryNameList, boolean isFavorite) {
        this.eventTitle = eventTitle;
        this.eventDescription = eventDescription;
        this.startTime = startTime;
        this.endTime = endTime;
        this.categoryNameList = categoryNameList;
        this.isFavorite = isFavorite;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public ArrayList<String> getCategoryNameList() {
        return categoryNameList;
    }

    public void setCategoryNameList(ArrayList<String> categoryNameList) {
        this.categoryNameList = categoryNameList;
    }

}
