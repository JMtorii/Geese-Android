package com.teamawesome.geese.util;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.teamawesome.geese.rest.service.FlockService;
import com.teamawesome.geese.rest.service.GeeseService;
import com.teamawesome.geese.rest.service.LoginService;

import java.io.IOException;

import retrofit.JacksonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by lcolam on 2/3/16.
 */
public class RestClient {
    private static Retrofit retrofitReactiveClient;

    private static class HeaderInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (SessionManager.checkLogin()) {
                request = request.newBuilder()
                        .addHeader("X-AUTH-TOKEN", SessionManager.getToken())
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

    private static HeaderInterceptor headerInterceptor;
    public static FlockService flockService;
    public static GeeseService geeseService;
    public static LoginService loginService;

    public static void init() {
        OkHttpClient client = new OkHttpClient();

        headerInterceptor = new HeaderInterceptor();
        client.interceptors().add(headerInterceptor);

        retrofitReactiveClient = new Retrofit.Builder()
                .baseUrl(SessionManager.getIPAddress())
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        flockService = retrofitReactiveClient.create(FlockService.class);
        geeseService = retrofitReactiveClient.create(GeeseService.class);
        loginService = retrofitReactiveClient.create(LoginService.class);
    }
}
