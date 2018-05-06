package com.depromeet.donkey.retrofit;

import com.depromeet.donkey.main.data.Marker;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitService {
    @GET("users/{user}/repos")
    Call<List<Marker>> listRepos(@Path("user") String user);
}
