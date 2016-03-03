package com.teamawesome.geese.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by MichaelQ on 2016-03-02.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserVote {

    @JsonProperty("gooseid")
    private int gooseId;

    @JsonProperty("postid")
    private int postId;

    @JsonProperty("value")
    private int value;

    // dummy constructor for jackson
    public UserVote() {

    }

    public int getGooseId() {
        return gooseId;
    }

    public int getPostId() {
        return postId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
