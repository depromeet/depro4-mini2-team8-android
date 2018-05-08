package com.depromeet.donkey.retrofit;

import com.depromeet.donkey.join.data.Member;
import com.depromeet.donkey.main.data.Marker;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RetrofitService {
    @GET("api/v1/accounts")
    Call<List<Member>> getMembers();

    @POST("api/v1/accounts")
    Call<Member> postMember(
            @Body Member member
    );
}
