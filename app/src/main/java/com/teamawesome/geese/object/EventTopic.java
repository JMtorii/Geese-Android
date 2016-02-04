package com.teamawesome.geese.object;

import com.teamawesome.geese.adapter.FlockEventTopicAdapter;

import java.util.Date;

/**
 * Created by codychung on 10/11/15.
 */
public class EventTopic implements EventItem {
    private String title;
    private Date eventDateTime;
    private int numGuests;
    private String description;

    public EventTopic(String title, Date time, Integer guests, String description) {
        this.title = title;
        this.eventDateTime = time;
        this.numGuests = guests;
        this.description = description;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    public Date getEventDateTime() {
        return this.eventDateTime;
    }

    public int getNumGuests() {
        return this.numGuests;
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public int getViewType() {
        return FlockEventTopicAdapter.RowType.Topic.ordinal();
    }
}
