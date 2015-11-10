package com.teamawesome.geese.rest.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by lcolam on 11/10/15.
 */
@Parcel
public class Event {
    @SerializedName("id")
    public int id;

    @SerializedName("authorid")
    public int authorId;

    @SerializedName("name")
    public String name;

    @SerializedName("description")
    public String description;

    @SerializedName("latitude")
    public float latitude;

    @SerializedName("longitude")
    public float longitude;

    @SerializedName("radius")
    public double radius;

    @SerializedName("score")
    public int score;

    // TODO datetime class for create and expire

    public Event() {}

    public Event(int authorId, String name, String description, float latitude, float longitude, double radius) {
        this.authorId = authorId;
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }
}
