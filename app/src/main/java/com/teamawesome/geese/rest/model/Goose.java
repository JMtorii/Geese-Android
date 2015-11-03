package com.teamawesome.geese.rest.model;

import com.google.gson.annotations.SerializedName;
import org.parceler.Parcel;

/**
 * Created by lcolam on 11/1/15.
 */
@Parcel
public class Goose {
    @SerializedName("id")
    int id;

    @SerializedName("name")
    String name;

    @SerializedName("email")
    String email;

    @SerializedName("verified")
    boolean verified;

    @SerializedName("password")
    String password;

    @SerializedName("salt")
    String salt;

    public Goose() {}

    public Goose(int id, String name, String email, boolean verified, String password, String salt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.verified = verified;
        this.password = password;
        this.salt = salt;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean getVerified() {
        return verified;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }
}
