package com.teamawesome.geese.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by MichaelQ on 2016-02-24.
 */
public class Comment {
//    {
//        "id": 2,
//        "postid": 9,
//        "authorid": 1,
//        "text": "First",
//        "score": 222,
//        "createdTime": [
//            2016,
//            1,
//            20,
//            19,
//            32,
//            12
//        ],
//        "startTime": null,
//        "endTime": null
//    }
    @JsonProperty("id")
    private int commentId;

    @JsonProperty("postid")
    private int postId;

    @JsonProperty("authorid")
    private int authorId;

    @JsonProperty("text")
    private String text;

    @JsonProperty("score")
    private int score;

    //dummy constructor for jackson
    public Comment() {}

    public Comment(Builder builder) {
        this.commentId = builder.commentId;
        this.postId = builder.postId;
        this.authorId = builder.authorId;
        this.text = builder.text;
        this.score = builder.score;
    }

    public int getCommentId() {
        return commentId;
    }

    public int getPostId() {
        return postId;
    }

    public int getAuthorId() {
        return authorId;
    }

    public String getText() {
        return text;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public static class Builder {
        private int commentId;
        private int postId;
        private int authorId;
        private String text;
        private int score;

        public Builder() {}

        public Builder commentId(int commentId) {
            this.commentId = commentId;
            return this;
        }

        public Builder postId(int postId) {
            this.postId = postId;
            return this;
        }

        public Builder authorId(int authorId) {
            this.authorId = authorId;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder score(int score) {
            this.score = score;
            return this;
        }

        public Comment build() {
            return new Comment(this);
        }
    }
}
