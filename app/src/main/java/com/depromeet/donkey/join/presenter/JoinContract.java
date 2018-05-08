package com.depromeet.donkey.join.presenter;

import com.depromeet.donkey.join.data.Member;

public interface JoinContract {
    interface View {
        void toast(String msg);
        void joinSuccess();
    }

    interface Presenter {
        void attachView(View view);
        void detachView();
        void connect(Member member);
    }
}
