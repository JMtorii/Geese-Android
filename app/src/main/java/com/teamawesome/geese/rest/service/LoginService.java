package com.teamawesome.geese.rest.service;

import com.squareup.okhttp.ResponseBody;
import com.teamawesome.geese.rest.model.Goose;

import retrofit.Call;
import retrofit.http.POST;
import retrofit.http.Headers;
import retrofit.http.Body;
import retrofit.http.Path;

/**
 * Created by lcolam on 1/13/16.
 */
public interface LoginService {
    @POST("/login")
    Call<ResponseBody> attemptLogin(@Body Goose goose);

    @POST("/login/fb/{accessToken}")
    Call<ResponseBody> attemptFacebookLogin(@Path("accessToken") String accessToken);
}
