package com.teamawesome.geese.rest.service;

import com.teamawesome.geese.rest.model.Flock;
import com.teamawesome.geese.rest.model.FlockV2;

import java.util.List;

import retrofit.Call;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Headers;
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
    Observable<List<FlockV2>> getNearbyFlocks(@Query("latitude") float latitude, @Query("longitude") float longitude);

    @GET("/flock/getFavourited")
    Observable<List<FlockV2>> getFavourited();
}
