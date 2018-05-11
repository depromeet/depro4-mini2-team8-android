package com.depromeet.donkey.join.model;

import android.content.Context;
import android.util.Log;

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
    private static final String TAG = JoinRetrofitModel.class.getSimpleName();
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
        Call<Void> call = retrofitService.getLoginInfo(member);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d(TAG, "code " + response.code());
                Log.d(TAG, member.getEmail() + "/"
                + member.getName() + "/" + member.getPassword());
                if (response.code() == 200) {
                    callback.onFailure("아이디가 중복됩니다.");
                    return;
                }

                if (response.code() != 401) {
                    callback.onFailure(ERROR_MESSAGE);
                    return;
                }

                Call<Void> call2 = retrofitService.postMember(member);
                call2.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.d(TAG, "code2 : " + response.code());
                        callback.onSuccess();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        callback.onFailure(ERROR_MESSAGE);
                        t.printStackTrace();
                    }
                });
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure(ERROR_MESSAGE);
            }
        });
    }
}
