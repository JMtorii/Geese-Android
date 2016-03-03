package com.teamawesome.geese.rest.model;

import android.graphics.Bitmap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by JMtorii on 15-12-13.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Flock {

    @JsonProperty("id")
    private int id;

    @JsonProperty("authorid")
    private int authorid;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("latitude")
    private float latitude;

    @JsonProperty("longitude")
    private float longitude;

    @JsonProperty("radius")
    private double radius;

    @JsonProperty("score")
    private int score;

    @JsonProperty("favourited")
    private boolean favourited;

    @JsonProperty("members")
    private int members;

    @JsonProperty("imageUri")
    private String imageUri;

//    @JsonProperty("createdTime")
//    private ObjectNode createdTime;

    @JsonIgnore
    private Bitmap mapImage200x200;

    public Flock() {}

    private Flock(Builder builder) {
        this.id = builder.id;
        this.authorid = builder.authorid;
        this.name = builder.name;
        this.description = builder.description;
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
        this.radius = builder.radius;
        this.score = builder.score;
        this.members = builder.members;
        this.imageUri = builder.imageUri;
        this.mapImage200x200 = builder.mapImage200x200;

        // TODO: implement me
//        this.createdTime = builder.createdTime;
//        this.expireTime = builder.expireTime;
    }

    public int getId() {
        return id;
    }

    public int getAuthorid() {
        return authorid;
    }

    public void setAuthorid(int authorid) {
        this.authorid = authorid;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public double getRadius() {
        return radius;
    }

    public int getScore() {
        return score;
    }

    public Bitmap getMapImage200x200() {
        return mapImage200x200;
    }

    public void setMapImage200x200(Bitmap mapImage200x200) {
        this.mapImage200x200 = mapImage200x200;
    }

    public boolean getFavourited() {
        return favourited;
    }

    public void setFavourited(boolean favourited) {
        this.favourited = favourited;
    }

    public int getMembers() { return members; }

    public void setMembers(int members) { this.members = members; }

    public String getImageUri() { return imageUri; }

//    public String getCreatedTime() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:MM");
//        Date testDate = null;
//        try {
//            testDate = sdf.parse(createdTime);
//        }catch(Exception ex){
//            ex.printStackTrace();
//        }
//        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd,yyyy");
//        String newFormat = formatter.format(testDate);
//        return newFormat;
//    }

    public static class Builder {
        private int id;
        private int authorid;
        private String name;
        private String description;
        private float latitude;
        private float longitude;
        private double radius;
        private int score;
        private int members;
        private String imageUri;
        private Bitmap mapImage200x200;

        public Builder() {}

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder authorid(int authorid) {
            this.authorid = authorid;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder latitude(float latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder longitude(float longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder radius(double radius) {
            this.radius = radius;
            return this;
        }

        public Builder score(int score) {
            this.score = score;
            return this;
        }

        public Builder members(int members) {
            this.members = members;
            return this;
        }

        public Builder imageUri(String imageUri) {
            this.imageUri = imageUri;
            return this;
        }

        public Builder mapImage200x200(Bitmap mapImage200x200) {
            this.mapImage200x200 = mapImage200x200;
            return this;
        }

        public Flock build() {
            return new Flock(this);
        }
    }
}