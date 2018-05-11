package com.depromeet.donkey.content_edit.presenter;

import android.content.Context;

import com.depromeet.donkey.content_edit.data.Post;
import com.depromeet.donkey.content_edit.model.EditRetrofitModel;
import com.depromeet.donkey.content_edit.model.callback.ModelCallback;
import com.depromeet.donkey.login.data.Member;
import com.depromeet.donkey.main.data.Marker;

public class EditPresenter implements EditContract.Presenter, ModelCallback.RetrofitCallback {
    private static final String TAG = EditPresenter.class.getSimpleName();

    private Context context;
    private EditRetrofitModel retrofitModel;

    private EditContract.View view;

    public EditPresenter(Context context) {
        this.context = context;
        this.retrofitModel = new EditRetrofitModel(context);
        retrofitModel.setCallback(this);
    }

    @Override
    public void attachView(EditContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void updatePost(Post post) {
        retrofitModel.postPost(post);
    }

    @Override
    public void onSuccess(String msg) {
        view.onPostSuccess(msg);
    }

    @Override
    public void onFailure(String msg) {
        view.toast(msg);
    }
}
