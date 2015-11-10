package com.teamawesome.geese.rest.model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import org.parceler.Parcel;

/**
 * Created by lcolam on 11/1/15.
 */
@Parcel
public class Goose {
    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("email")
    public String email;

    @SerializedName("verified")
    public boolean verified;

    @SerializedName("password")
    public String password;

    @SerializedName("salt")
    public String salt;

    public Goose() {}

    // TODO smaller constructor for Goose keys

    public Goose(int id, String name, String email, boolean verified, String password, String salt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.verified = verified;
        this.password = password;
        this.salt = salt;
    }

    public void print() {
        Log.d("Goose", "Name<" + name + "> Email<" + email + "> Verified<" + verified
                + "> Password<" + password + "> Salt<" + salt + ">");
    }
}
