package com.teamawesome.geese.object;

/**
 * Created by MichaelQ on 2015-09-27.
 */
public class PostTopic {
    private String title;
    private String message;
    // TODO:add PostDetails list
    // TODO:support images
    private int upvotes;

    public PostTopic(String title, String message, int upvotes) {
        this.title = title;
        this.message = message;
        this.upvotes = upvotes;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public int getUpvotes() {
        return upvotes;
    }
}
