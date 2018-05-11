package com.depromeet.donkey.main.presenter;

import android.content.Context;

import com.depromeet.donkey.main.Model.MainRetrofitModel;
import com.depromeet.donkey.main.Model.callback.ModelCallback;
import com.depromeet.donkey.main.data.Marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainPresenter implements MainContract.Presenter, ModelCallback.RetrofitCallback {
    private static final String TAG = MainPresenter.class.getSimpleName();

    private Context context;
    private MainContract.View view;
    private MainRetrofitModel retrofitModel;

    public MainPresenter(Context context) {
        this.context = context;
        this.retrofitModel = new MainRetrofitModel(context);
        this.retrofitModel.setCallback(this);
    }

    @Override
    public void attachView(MainContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void requestLocationItem(HashMap items) {
        retrofitModel.getMarkers(items);
    }

    @Override
    public void updateMarker(Marker marker) {
        retrofitModel.postMarker(marker);
    }

    @Override
    public void onGetMarkersSuccess(ArrayList<Marker> items) {
        view.paintMarkers(items);
    }

    @Override
    public void onFailure(String msg) {
        view.toast(msg);
    }
}
