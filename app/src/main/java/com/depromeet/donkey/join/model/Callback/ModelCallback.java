package com.depromeet.donkey.join.model.Callback;

public class ModelCallback {
    public interface JoinRetrofitCallback {
        void onSuccess();
        void onFailure(String msg);
    }
}
