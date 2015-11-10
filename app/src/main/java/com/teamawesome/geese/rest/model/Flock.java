package com.teamawesome.geese.rest.model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by lcolam on 11/8/15.
 */
@Parcel
public class Flock {
    @SerializedName("name")
    public String name;

    @SerializedName("description")
    public String description;

    @SerializedName("latitude")
    public float latitude;

    @SerializedName("longitude")
    public float longitude;

    @SerializedName("authorid")
    public int authorId;

    @SerializedName("radius")
    public double radius;

    @SerializedName("score")
    public int score;

    // TODO class for datetime

    public Flock() {}

    public Flock(String name, String description, float latitude, float longitude, int authorId, double radius) {
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.authorId = authorId;
        this.radius = radius;
    }

    public void print() {
        Log.d("Flock", "Name<" + name + "> Desc<" + description + "> Lat<" + latitude + "> Long<" + longitude
                + "> Author<" + authorId + "> Radius<" + radius + ">");
    }

}
