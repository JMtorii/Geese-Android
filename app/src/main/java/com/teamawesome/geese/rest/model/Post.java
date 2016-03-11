package com.teamawesome.geese.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.teamawesome.geese.util.DateUtil;

import java.util.List;

/**
 * Created by lcolam on 11/10/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Post {

    @JsonProperty("id")
    private int id;

    @JsonProperty("flockid")
    private int flockid;

    @JsonProperty("authorid")
    private int authorid;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("pinned")
    private boolean pinned;

    @JsonProperty("score")
    private int score;

    @JsonProperty("userVote")
    private UserVote userVote;

    @JsonProperty("commentCount")
    private int commentCount;

    @JsonProperty("createdTime")
    private List<Integer> dateArray;

    @JsonProperty("authorName")
    private String authorName;

    @JsonProperty("imageUri")
    private String imageUri;

    public Post() {}

    public int getId() {
        return id;
    }

    public int getFlockid() {
        return flockid;
    }

    public int getAuthorid() {
        return authorid;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPinned() {
        return pinned;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public UserVote getUserVote() {
        return userVote;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getCreatedDate() {
        return DateUtil.getCreatedDateFromDateArray(this.dateArray);
    }
}
