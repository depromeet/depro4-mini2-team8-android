package com.depromeet.donkey.main.Model;

import retrofit2.Retrofit;

public class RetrofitModel {
    public RetrofitModel() {

    }

    public void getMarkers() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("url")
                .build();
    }
}
