package com.teamawesome.geese.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by MichaelQ on 2016-02-04.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreatePostRequestBody {

    @JsonProperty("flockid")
    public int flockId;

    @JsonProperty("title")
    public String title;

    @JsonProperty("description")
    public String description;

    public CreatePostRequestBody(int flockId, String title, String description) {
        this.flockId = flockId;
        this.title = title;
        this.description = description;
    }
}
