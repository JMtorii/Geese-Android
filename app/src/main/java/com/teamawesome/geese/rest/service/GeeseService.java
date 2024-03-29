package com.teamawesome.geese.rest.service;

import com.teamawesome.geese.rest.model.Goose;

import java.util.List;

import retrofit.Call;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Headers;
import retrofit.http.Body;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by lcolam on 11/1/15.
 */
public interface GeeseService {
    @GET("/goose/{gooseId}")
    Call<Goose> getGoose(@Path("gooseId") int gooseId);

    @GET("/goose")
    Call<List<Goose>> getGeese();

    @POST("/goose")
    Call<Void> createGoose(@Body Goose goose);

    @PUT("/goose/{gooseId}")
    Call<Goose> updateGoose(@Path("gooseId") int gooseId, @Body Goose goose);

    @DELETE("/goose/{gooseId}")
    Call<Goose> deleteGoose(@Path("gooseId") int gooseId);
}
