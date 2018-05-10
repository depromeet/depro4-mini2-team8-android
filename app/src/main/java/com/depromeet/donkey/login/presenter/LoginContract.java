package com.depromeet.donkey.login.presenter;

import com.depromeet.donkey.login.data.Member;

public interface LoginContract {
    interface View {
        void loginFailure(String msg);
        void loginSuccess(Member member);
    }

    interface Presenter {
        void attachView(View view);
        void detachView();
        void requestLoginInfo(Member member);
    }
}
