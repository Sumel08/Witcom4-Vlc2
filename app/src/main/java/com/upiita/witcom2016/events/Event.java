package com.upiita.witcom2016.events;

import java.util.Date;

/**
 * Created by oscar on 23/04/17.
 */

public class Event {
    private String eventName;
    private String eventCode;
    private String eventImage;
    private Date startDate;
    private Date endDate;

    public Event() {
    }

    public Event(String eventName, String eventCode, String eventImage, Date startDate, Date endDate) {
        this.eventName = eventName;
        this.eventCode = eventCode;
        this.eventImage = eventImage;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getEventImage() {
        return eventImage;
    }

    public void setEventImage(String eventImage) {
        this.eventImage = eventImage;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
