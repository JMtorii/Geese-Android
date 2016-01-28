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
    @Headers({
        "Accept: */*",
        "Cache-Control: no-cache",
        "Content-Type: application/json"
    })
    Call<Flock> getFlock(@Path("flockId") int flockId);

    @GET("/flock")
    @Headers({
        "Accept: */*",
        "Cache-Control: no-cache",
        "Content-Type: application/json"
    })
    Call<List<Flock>> getFlocks();

    @PUT("/flock/{flockId}")
    @Headers({
        "Accept: */*",
        "Cache-Control: no-cache",
        "Content-Type: application/json"
    })
    Call<Flock> updateFlock(@Path("flockId") int flockId, Flock flock);

    @DELETE("/flock/{flockId}")
    @Headers({
            "Accept: */*",
            "Cache-Control: no-cache",
            "Content-Type: application/json"
    })
    Call<Flock> deleteFlock(@Path("flockId") int flockId);

    @GET("/flock/getNearbyFlocks")
    @Headers({
            "Accept: */*",
            "Cache-Control: no-cache",
            "Content-Type: application/json",
            "X-AUTH-TOKEN: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QGVtYWlsLmNvbSJ9.qufWHyPzMLAwaF_1QARepchXGRTx5fsuHOJXcfnF6OLTBbcD6PyD575geXdU2zwbwIYL_5ThGRSMlb7Qa_rpxw"
    })
    Observable<List<FlockV2>> getNearbyFlocks(@Query("latitude") float latitude, @Query("longitude") float longitude);

    @GET("/flock/getFavourited")
    @Headers({
            "Accept: */*",
            "Cache-Control: no-cache",
            "Content-Type: application/json",
            "X-AUTH-TOKEN: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QGVtYWlsLmNvbSJ9.qufWHyPzMLAwaF_1QARepchXGRTx5fsuHOJXcfnF6OLTBbcD6PyD575geXdU2zwbwIYL_5ThGRSMlb7Qa_rpxw"
    })
    Observable<List<FlockV2>> getFavourited();
}
