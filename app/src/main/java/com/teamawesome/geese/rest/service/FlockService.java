package com.teamawesome.geese.rest.service;

import com.squareup.okhttp.ResponseBody;
import com.teamawesome.geese.rest.model.Flock;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by lcolam on 11/8/15.
 */
public interface FlockService {
    @GET("/flock/{flockId}")
    Call<Flock> getFlock(@Path("flockId") int flockId);

    @GET("/flock")
    Call<List<Flock>> getFlocks();

    @PUT("/flock/{flockId}")
    Call<Flock> updateFlock(@Path("flockId") int flockId, Flock flock);

    @DELETE("/flock/{flockId}")
    Call<Flock> deleteFlock(@Path("flockId") int flockId);

    @GET("/flock/getNearbyFlocks")
    Observable<List<Flock>> getNearbyFlocks(@Query("latitude") float latitude, @Query("longitude") float longitude);

    @GET("/flock/getFavourited")
    Observable<List<Flock>> getFavourited();

    @POST("/flock/{flockId}/joinFlock")
    Observable<ResponseBody> joinFlock(@Path("flockId") int flockId);

    @POST("/flock/{flockId}/unjoinFlock")
    Observable<ResponseBody> unjoinFlock(@Path("flockId") int flockId);

    @POST("/flock")
    Call<Void> createFlock(@Body Flock flock);
}
