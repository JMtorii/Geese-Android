package com.teamawesome.geese.rest.service;

import com.squareup.okhttp.ResponseBody;
import com.teamawesome.geese.rest.model.Comment;
import com.teamawesome.geese.rest.model.CreateCommentRequestBody;
import com.teamawesome.geese.rest.model.CreatePostRequestBody;
import com.teamawesome.geese.rest.model.Post;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
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

    @POST("/post/vote/{postId}")
    Observable<ResponseBody> voteForPost(@Path("postId") int postId, @Query("value") int value);

    @GET("/comment")
    Observable<List<Comment>> getCommentsForPost(@Query("postId") int postId);

    @POST("/comment")
    Call<ResponseBody> saveCommentForPost(@Body CreateCommentRequestBody createCommentRequestBody);

    @POST("/comment/vote/{commentId}")
    Call<ResponseBody> voteForComment(@Path("commentId") int commentId, @Query("value") int value);

    @DELETE("/post/{postId}")
    Observable<ResponseBody> deletePost(@Path("postId") int postId);

    @DELETE("/comment/{commentId}")
    Observable<ResponseBody> deleteComment(@Path("commentId") int commentId);
}
