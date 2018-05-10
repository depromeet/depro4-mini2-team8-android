package com.depromeet.donkey.login.presenter;

import android.content.Context;

import com.depromeet.donkey.login.data.Member;
import com.depromeet.donkey.login.model.LoginRetrofitModel;
import com.depromeet.donkey.login.model.callback.ModelCallback;

public class LoginPresenter implements LoginContract.Presenter, ModelCallback.LoginRetrofitCallback {
    private static final String TAG = LoginPresenter.class.getSimpleName();

    private Context context;
    private LoginRetrofitModel retrofitModel;
    private LoginContract.View view;

    public LoginPresenter(Context context) {
        this.context = context;
        this.retrofitModel = new LoginRetrofitModel(context);
        this.retrofitModel.setCallback(this);
    }

    @Override
    public void attachView(LoginContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void requestLoginInfo(Member member) {
        retrofitModel.getLoginInfo(member);
    }

    @Override
    public void onSuccess(Member member) {
        view.loginSuccess(member);
    }

    @Override
    public void onFailure(String msg) {
        view.loginFailure(msg);
    }
}
