package com.teamawesome.geese.object;

import java.util.ArrayList;

/**
 * Created by MichaelQ on 2015-09-27.
 */
public class PostTopic {
    private String title;
    private String description;
    private ArrayList<PostComment> postComments;
    // TODO:support images
    private int upvotes;

    public PostTopic(String title, String description, ArrayList<PostComment> postComments, int upvotes) {
        this.title = title;
        this.description = description;
        this.postComments = postComments;
        this.upvotes = upvotes;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<PostComment> getPostComments() {
        return postComments;
    }

    public int getUpvotes() {
        return upvotes;
    }
}
