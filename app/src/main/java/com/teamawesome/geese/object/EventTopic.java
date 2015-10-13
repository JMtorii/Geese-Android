package com.teamawesome.geese.object;

import java.util.Date;

/**
 * Created by codychung on 10/11/15.
 */
public class EventTopic {
    private String title;
    private Date eventDateTime;
    private int numGuests;

    public EventTopic(String title, Date time, Integer guests) {
        this.title = title;
        this.eventDateTime = time;
        this.numGuests = guests;
    }

    public String getTitle() {
        return title;
    }

    public Date getEventDateTime() {
        return eventDateTime;
    }

    public int getNumGuests() {
        return numGuests;
    }
}
