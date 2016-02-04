package com.teamawesome.geese.util;

import com.squareup.okhttp.OkHttpClient;
import com.teamawesome.geese.rest.service.FlockService;
import com.teamawesome.geese.rest.service.GeeseService;
import com.teamawesome.geese.rest.service.LoginService;

import retrofit.JacksonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by lcolam on 2/3/16.
 */
public class RestClient {
    private static Retrofit retrofitReactiveClient;

    public static HeaderInterceptor headerInterceptor;
    public static FlockService flockService;
    public static GeeseService geeseService;
    public static LoginService loginService;

    public static void init() {
        OkHttpClient client = new OkHttpClient();

        headerInterceptor = new HeaderInterceptor();
        if (SessionManager.checkLogin()) {
            String token = SessionManager.getUserDetails().get("Token");
            headerInterceptor.addTokenHeader(token);
        } else {
            headerInterceptor.removeTokenHeader();
        }
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
