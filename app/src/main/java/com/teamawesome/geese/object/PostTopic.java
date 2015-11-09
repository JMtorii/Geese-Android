package com.teamawesome.geese.object;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by MichaelQ on 2015-09-27.
 */
public class PostTopic {
    private String title;
    private String description;
    private ArrayList<PostComment> postComments;
    private String imageURL;
    private Bitmap imageData;
    private int upvotes;

    public PostTopic(String title, String description, ArrayList<PostComment> postComments, String imageURL, int upvotes) {
        this.title = title;
        this.description = description;
        this.postComments = postComments;
        this.imageURL = imageURL;
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

    public String getImageURL() {
        return imageURL;
    }

    public Bitmap getImageData() {
        return imageData;
    }

    public void setImageData(Bitmap imageData) {
        this.imageData = imageData;
    }
}
