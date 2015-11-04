package com.teamawesome.geese.rest.service;

import com.teamawesome.geese.rest.model.Goose;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Headers;
import retrofit.http.Body;
import retrofit.http.Query;

/**
 * Created by lcolam on 11/1/15.
 */
public interface GeeseService {
    @GET("/goose")
    @Headers({
        "Accept: */*",
        "Cache-Control: no-cache",
        "Content-Type: application/json"
    })
    Call<List<Goose>> getGeese();

    @POST("/goose")
    @Headers({
        "Accept: */*",
        "Cache-Control: no-cache",
        "Content-Type: application/json"
    })
    Call<Goose> createGoose(@Body Goose goose);
}
