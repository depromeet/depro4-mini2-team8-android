package com.depromeet.donkey.join.model;

import android.content.Context;

import com.depromeet.donkey.R;
import com.depromeet.donkey.login.data.Member;
import com.depromeet.donkey.join.model.callback.ModelCallback;
import com.depromeet.donkey.retrofit.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JoinRetrofitModel {
    private final String ERROR_MESSAGE = "서버 응답이 없습니다.\n 다시 시도해 주세요.";

    private Retrofit retrofit;
    private RetrofitService retrofitService;
    private ModelCallback.JoinRetrofitCallback callback;

    public JoinRetrofitModel(Context context) {
        retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.server_host))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);
    }

    public void setCallback(ModelCallback.JoinRetrofitCallback callback) {
        this.callback = callback;
    }

    public void putMember(final Member member) {
        Call<List<Member>> call = retrofitService.getMembers();
        call.enqueue(new Callback<List<Member>>() {
            @Override
            public void onResponse(Call<List<Member>> call, Response<List<Member>> response) {
                List<Member> items = response.body();
                if (response.code() != 200) {
                    callback.onFailure(ERROR_MESSAGE);
                    return;
                }

                if (items != null) {
                    for (Member m : items) {
                        if (m.getEmail().equals(member.getEmail())) {
                            callback.onFailure("아이디가 중복됩니다.");
                            return;
                        }
                    }
                }

                Call<Member> call2 = retrofitService.postMember(member);
                call2.enqueue(new Callback<Member>() {
                    @Override
                    public void onResponse(Call<Member> call, Response<Member> response) {
                        callback.onSuccess();
                    }

                    @Override
                    public void onFailure(Call<Member> call, Throwable t) {
                        callback.onFailure(ERROR_MESSAGE);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Member>> call, Throwable t) {
                callback.onFailure(ERROR_MESSAGE);
            }
        });
    }
}
