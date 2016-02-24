package com.teamawesome.geese.rest.service;

import com.squareup.okhttp.ResponseBody;
import com.teamawesome.geese.rest.model.CreatePostRequestBody;
import com.teamawesome.geese.rest.model.Post;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by lcolam on 11/10/15.
 */
public interface PostService {

    @GET("/post")
    Observable<List<Post>> getPostsForFlock(@Query("flockId") int flockId);

    @POST("/post")
    Call<ResponseBody> savePostForFlock(@Body CreatePostRequestBody createPostRequestBody);
}
