package com.depromeet.donkey.login.model.callback;

import com.depromeet.donkey.login.data.Member;

public interface ModelCallback {
    interface LoginRetrofitCallback {
        void onSuccess(Member member);
        void onFailure(String msg);
    }
}
