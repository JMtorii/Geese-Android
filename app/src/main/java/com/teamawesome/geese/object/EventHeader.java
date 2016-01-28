package com.teamawesome.geese.object;

import com.teamawesome.geese.adapter.FlockEventTopicAdapter;

/**
 * Created by codychung on 11/7/15.
 */
public class EventHeader implements EventItem {
    private String title;

    public EventHeader(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getViewType() {
        return FlockEventTopicAdapter.RowType.Header.ordinal();
    }
}
