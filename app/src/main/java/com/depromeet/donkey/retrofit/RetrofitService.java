package com.depromeet.donkey.retrofit;

import com.depromeet.donkey.content_edit.data.Post;
import com.depromeet.donkey.login.data.Member;
import com.depromeet.donkey.main.data.Marker;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface RetrofitService {
    @POST("api/v1/accounts/sign-in")
    Call<Void> getLoginInfo(
            @Body Member member
    );

    @POST("api/v1/accounts")
    Call<Void> postMember(
            @Body Member member
    );

    @GET("api/v1/posts")
    Call<List<Marker>> getMarkers(
            @QueryMap HashMap<String, String> query
    );

    @POST("api/v1/posts")
    Call<Void> postMarker(
            @Body Post post
    );


}
