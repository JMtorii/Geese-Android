package com.teamawesome.geese.object;

/**
 * Created by MichaelQ on 2015-10-25.
 */
public class PostComment {
    private String comment;
    private int upvotes;

    public PostComment(String comment, int upvotes) {
        this.comment = comment;
        this.upvotes = upvotes;
    }

    public String getComment() {
        return comment;
    }

    public int getUpvotes() {
        return upvotes;
    }
}
