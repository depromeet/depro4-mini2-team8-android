package com.depromeet.donkey.login.model;

import android.content.Context;
import android.util.Log;

import com.depromeet.donkey.R;
import com.depromeet.donkey.login.data.Member;
import com.depromeet.donkey.login.model.callback.ModelCallback;
import com.depromeet.donkey.retrofit.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginRetrofitModel {
    private static final String TAG = LoginRetrofitModel.class.getSimpleName();
    private static final String ERROR_MESSAGE = "서버 응답이 없습니다.\n 다시 시도해주세요.";
    private Context context;

    private Retrofit retrofit;
    private RetrofitService retrofitService;
    private ModelCallback.LoginRetrofitCallback callback;

    public LoginRetrofitModel(Context context) {
        this.context = context;
        retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.server_host))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);
    }

    public void setCallback(ModelCallback.LoginRetrofitCallback callback) {
        this.callback = callback;
    }

    public void getLoginInfo(final Member member) {
        Call<Void> call = retrofitService.getLoginInfo(member);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d(TAG, "code : " + response.code());
                if (response.code() == 401) {
                    callback.onFailure("아이디 비밀번호를 확인해주세요.");
                    return;
                }

                if (response.code() != 200) {
                    callback.onFailure(ERROR_MESSAGE);
                    return;
                }

                callback.onSuccess(member);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure(ERROR_MESSAGE);
                t.printStackTrace();
            }
        });
    }

}
