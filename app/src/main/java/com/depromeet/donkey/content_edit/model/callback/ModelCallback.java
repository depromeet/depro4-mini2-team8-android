package com.depromeet.donkey.content_edit.model.callback;

public interface ModelCallback {
    interface RetrofitCallback {
        void onSuccess(String msg);
        void onFailure(String msg);
    }
}
