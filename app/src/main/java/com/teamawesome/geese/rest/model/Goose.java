package com.teamawesome.geese.rest.model;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Created by lcolam on 11/1/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Goose {
    @JsonProperty("id")
    public int id;

    @JsonProperty("name")
    public String name;

    @JsonProperty("email")
    public String email;

    @JsonProperty("verified")
    public boolean verified;

    @JsonProperty("password")
    public String password;

    @JsonProperty("salt")
    public String salt;

    public Goose() {}

    public Goose(int id, String name, String email, boolean verified, String password, String salt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.verified = verified;
        this.password = password;
        this.salt = salt;
    }

    public Goose(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Goose(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void print() {
        Log.d("Goose", "ID<" + id + "> Name<" + name + "> Email<" + email + "> Verified<" + verified
                + "> Password<" + password + "> Salt<" + salt + ">");
    }
}
