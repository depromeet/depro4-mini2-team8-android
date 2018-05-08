package com.depromeet.donkey.join.presenter;

import android.content.Context;

import com.depromeet.donkey.join.data.Member;
import com.depromeet.donkey.join.model.Callback.ModelCallback;
import com.depromeet.donkey.join.model.JoinRetrofitModel;

public class JoinPresenter implements JoinContract.Presenter, ModelCallback.JoinRetrofitCallback {
    private static final String TAG = JoinPresenter.class.getSimpleName();

    private Context context;
    private JoinContract.View view;
    private JoinRetrofitModel retrofitModel;

    public JoinPresenter(Context context) {
        this.context = context;
        this.retrofitModel = new JoinRetrofitModel(context);
        this.retrofitModel.setCallback(this);
    }

    @Override
    public void attachView(JoinContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void connect(Member member) {
        retrofitModel.putMember(member);
    }

    @Override
    public void onSuccess() {
        view.joinSuccess();
    }

    @Override
    public void onFailure(String msg) {
        view.toast(msg);
    }
}
