package com.teamawesome.geese.util;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by lcolam on 1/27/16.
 */
public class HeaderInterceptor implements Interceptor {
    private String token;

    public void addTokenHeader(String token) {
        this.token = token;
    }

    public void removeTokenHeader() {
        this.token = null;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (this.token != null) {
            request = request.newBuilder()
                    .addHeader("X-AUTH-TOKEN", this.token)
                    .addHeader("Accept", "*/*")
                    .addHeader("Cache-Control", "no-cache")
                    .addHeader("Content-Type", "application/json")
                    .build();
        } else {
            request = request.newBuilder()
                    .addHeader("Accept", "*/*")
                    .addHeader("Cache-Control", "no-cache")
                    .addHeader("Content-Type", "application/json")
                    .build();
        }
        Response response = chain.proceed(request);
        return response;
    }
}
