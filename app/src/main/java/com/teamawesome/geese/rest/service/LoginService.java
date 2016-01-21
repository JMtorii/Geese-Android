package com.teamawesome.geese.rest.service;

import com.squareup.okhttp.ResponseBody;
import com.teamawesome.geese.rest.model.Goose;

import retrofit.Call;
import retrofit.http.POST;
import retrofit.http.Headers;
import retrofit.http.Body;

/**
 * Created by lcolam on 1/13/16.
 */
public interface LoginService {
    @POST("/login")
    @Headers({
            "Accept: */*",
            "Cache-Control: no-cache",
            "Content-Type: application/json"
    })
    Call<ResponseBody> attemptLogin(@Body Goose goose);
}
