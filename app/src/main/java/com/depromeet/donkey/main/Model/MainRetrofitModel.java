package com.depromeet.donkey.main.Model;

import android.content.Context;

import com.depromeet.donkey.R;
import com.depromeet.donkey.main.Model.callback.ModelCallback;
import com.depromeet.donkey.main.data.Marker;
import com.depromeet.donkey.retrofit.RetrofitService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainRetrofitModel {
    private static final String TAG = MainRetrofitModel.class.getSimpleName();
    private static final String ERROR_MESSAGE = "서버 응답이 없습니다.\n 다시 시도해주세요.";
    private Context context;

    private Retrofit retrofit;
    private RetrofitService retrofitService;
    private ModelCallback.RetrofitCallback callback;

    public MainRetrofitModel(Context context) {
        this.context = context;

        retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.server_host))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);
    }

    public void setCallback(ModelCallback.RetrofitCallback callback) {
        this.callback = callback;
    }

    public void getMarkers(HashMap items) {
        Call<List<Marker>> call = retrofitService.getMarkers(items);
        call.enqueue(new Callback<List<Marker>>() {
            @Override
            public void onResponse(Call<List<Marker>> call, Response<List<Marker>> response) {
                List<Marker> markers = response.body();
                if (response.code() != 200) {
                    callback.onFailure(ERROR_MESSAGE);
                    return;
                }
                if (markers == null)
                    return;

                callback.onGetMarkersSuccess(new ArrayList(markers));
            }

            @Override
            public void onFailure(Call<List<Marker>> call, Throwable t) {
                callback.onFailure(ERROR_MESSAGE);
            }
        });
    }

    public void postMarker(Marker marker) {
        Call<Void> call = retrofitService.postMarker(null);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure(ERROR_MESSAGE);
            }
        });
    }
}
