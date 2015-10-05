package com.teamawesome.geese.object;

/**
 * Created by MichaelQ on 2015-09-27.
 */
public class PostTopic {
    private String title;
    private String description;
    // TODO:add PostDetails list
    // TODO:support images
    private int upvotes;

    public PostTopic(String title, String description, int upvotes) {
        this.title = title;
        this.description = description;
        this.upvotes = upvotes;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getUpvotes() {
        return upvotes;
    }
}
