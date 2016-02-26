package com.teamawesome.geese.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by MichaelQ on 2016-02-26.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateCommentRequestBody {

    @JsonProperty("postid")
    public int postId;

    @JsonProperty("text")
    public String text;

    public CreateCommentRequestBody(int postId, String text) {
        this.postId = postId;
        this.text = text;
    }
}
