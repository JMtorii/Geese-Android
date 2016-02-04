package com.teamawesome.geese.rest;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.teamawesome.geese.activity.MainActivity;
import com.teamawesome.geese.rest.model.Goose;
import com.teamawesome.geese.rest.service.FlockService;
import com.teamawesome.geese.rest.service.GeeseService;
import com.teamawesome.geese.util.Constants;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by lcolam on 11/9/15.
 */
public final class RestExamples {
    private RestExamples() {}

    public static void testAllGoose() {
        Goose goose;
        int gooseId;

        gooseId = 1;
        getGoose(gooseId);

        goose = new Goose(0, "Leotest", "130.leo@gmail.com", true, "P@ssword", "NaCl");
        createGoose(goose);

        getGeese();

        gooseId = 1;
        goose = new Goose(0, "LeotestPut", "130.leo@gmail.com", true, "P@ssword", "NaCl");
        updateGoose(gooseId, goose);

        gooseId = 10;
        deleteGoose(gooseId);
    }

    // 1) Create retrofit instance (should have one in MainActivity already that can be accessed with getRetrofitClient()
    private static Retrofit retrofitClient = new Retrofit.Builder()
            .baseUrl(Constants.GEESE_SERVER_ADDRESS)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    // 2) Set up service
    private static GeeseService geeseService = retrofitClient.create(GeeseService.class);

    // 3) Create call

    // Custom path with response
    public static void getGoose(int gooseId) {
        Call<Goose> call = geeseService.getGoose(gooseId);
        call.enqueue(new Callback<Goose>() {
            @Override
            public void onResponse(Response<Goose> response, Retrofit retrofit) {
                Log.d("Test", String.valueOf(response.raw()));
                Log.d("Test", String.valueOf(response.body()));
                if (response.isSuccess()) {
                    Log.d("Test", "Successfully got geese!");
                    Goose goose = response.body();
                    goose.print();
                } else {
                    Log.e("Test", "Error parsing from geese.");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("Test", "Failed to get geese.");
                Log.e("Test", t.getMessage().toString());
                t.printStackTrace();
            }
        });
    }

    // List response
    public static void getGeese() {
        Call<List<Goose>> call = geeseService.getGeese();
        call.enqueue(new Callback<List<Goose>>() {
            @Override
            public void onResponse(Response<List<Goose>> response, Retrofit retrofit) {
                Log.d("Test", String.valueOf(response.raw()));
                Log.d("Test", String.valueOf(response.body()));
                if (response.isSuccess()) {
                    Log.d("Test", "Successfully got geese!");
                    List<Goose> geese = response.body();
                    for (Iterator<Goose> i = geese.iterator(); i.hasNext(); ) {
                        Goose goose = i.next();
                        goose.print();
                    }
                } else {
                    Log.e("Test", "Error parsing from geese.");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("Test", "Failed to get geese.");
                Log.e("Test", t.getMessage().toString());
                t.printStackTrace();
            }
        });
    }

    // Post with argument Body
    public static void createGoose(Goose goose) {
        // Goose goose = new Goose(100, "Leotest", "130.leo@gmail.com", true, "P@ssword", "NaCl");
        Call<Void> call = geeseService.createGoose(goose);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Response<Void> response, Retrofit retrofit) {
                Log.d("Test", String.valueOf(response.raw()));
                Log.d("Test", String.valueOf(response.body()));
                if (response.isSuccess()) {
                    Log.d("Test", "Success, goose created!");
                } else {
                    try {
                        Log.e("test", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("Test", "Failed to create goose.");
                Log.e("Test", t.getMessage().toString());
                t.printStackTrace();
            }
        });
    }

    public static void updateGoose(int gooseId, Goose goose) {
        Call<Goose> call = geeseService.updateGoose(gooseId, goose);
        call.enqueue(new Callback<Goose>() {
            @Override
            public void onResponse(Response<Goose> response, Retrofit retrofit) {
                Log.d("Test", String.valueOf(response.raw()));
                Log.d("Test", String.valueOf(response.body()));
                if (response.isSuccess()) {
                    Log.d("Test", "Successfully updated goose!");
                } else {
                    Log.e("Test", "Error updating goose.");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("Test", "Failed to update goose.");
                Log.e("Test", t.getMessage().toString());
                t.printStackTrace();
            }
        });
    }

    public static void deleteGoose(int gooseId) {
        Call<Goose> call = geeseService.deleteGoose(gooseId);
        call.enqueue(new Callback<Goose>() {
            @Override
            public void onResponse(Response<Goose> response, Retrofit retrofit) {
                Log.d("Test", String.valueOf(response.raw()));
                Log.d("Test", String.valueOf(response.body()));
                if (response.isSuccess()) {
                    Log.d("Test", "Successfully deleted goose!");
                } else {
                    Log.e("Test", "Error deleting goose.");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("Test", "Failed to delete goose.");
                Log.e("Test", t.getMessage().toString());
                t.printStackTrace();
            }
        });
    }
}
