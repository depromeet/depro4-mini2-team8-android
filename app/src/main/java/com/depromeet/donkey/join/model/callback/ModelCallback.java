package com.depromeet.donkey.join.model.callback;

public class ModelCallback {
    public interface JoinRetrofitCallback {
        void onSuccess();
        void onFailure(String msg);
    }
}
