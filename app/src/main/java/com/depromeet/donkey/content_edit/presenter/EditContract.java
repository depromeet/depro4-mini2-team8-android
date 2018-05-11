package com.depromeet.donkey.content_edit.presenter;

import com.depromeet.donkey.content_edit.data.Post;
import com.depromeet.donkey.login.data.Member;
import com.depromeet.donkey.main.data.Marker;

public interface EditContract {
    interface View {
        void toast(String msg);
        void onPostSuccess(String msg);
    }

    interface Presenter {
        void attachView(View view);
        void detachView();
        void updatePost(Post post);
    }
}
