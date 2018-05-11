package com.depromeet.donkey.content_edit.model;

import android.content.Context;

import com.depromeet.donkey.R;
import com.depromeet.donkey.content_edit.data.Post;
import com.depromeet.donkey.content_edit.model.callback.ModelCallback;
import com.depromeet.donkey.retrofit.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditRetrofitModel {
    private static final String POST_SUCCESS_MSG = "작성 완료 되었습니다.";
    private static final String SERVER_ERROR_MSG = "서버 오류입니다. 다시 시도해주세요.";
    private static final String UNKNOWN_ERROR_MSG = "알수 없는오류입니다. 다시 시도해주세요.";

    private Context context;
    private Retrofit retrofit;
    private RetrofitService retrofitService;
    private ModelCallback.RetrofitCallback callback;

    public EditRetrofitModel(Context context) {
        retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.server_host))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);
    }

    public void setCallback(ModelCallback.RetrofitCallback callback) {
        this.callback = callback;
    }

    public void postPost(Post post) {
        Call<Void> call = retrofitService.postMarker(post);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() != 200) {
                    callback.onFailure(SERVER_ERROR_MSG);
                    return;
                }

                callback.onSuccess(POST_SUCCESS_MSG);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure(UNKNOWN_ERROR_MSG);
            }
        });
    }
}
