package com.teamawesome.geese.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    //TODO: temp thing till we get what the user has voted for back from the server
    public int vote;

    @JsonProperty("commentCount")
    private int commentCount;

    private Post(Builder builder) {
        builder.id = id;
        builder.flockid = flockid;
        builder.authorid = authorid;
        builder.title = title;
        builder.description = description;
        builder.pinned = pinned;
        builder.score = score;
        builder.commentCount = commentCount;
    }

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

    public static class Builder {
        private int id;
        private int flockid;
        private int authorid;
        private String title;
        private String description;
        private boolean pinned;
        private int score;
        private int commentCount;

        public Builder() {}

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder flockid(int flockid) {
            this.flockid = flockid;
            return this;
        }

        public Builder authorid(int authorid) {
            this.authorid = authorid;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder pinned(boolean pinned) {
            this.pinned = pinned;
            return this;
        }

        public Builder score(int score) {
            this.score = score;
            return this;
        }

        public Builder commentCount(int commentCount) {
            this.commentCount = commentCount;
            return  this;
        }

        public Post build() {
            return new Post(this);
        }
    }


}
