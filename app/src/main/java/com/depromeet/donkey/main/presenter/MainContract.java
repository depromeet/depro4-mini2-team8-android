package com.depromeet.donkey.main.presenter;

import com.depromeet.donkey.main.data.Marker;

import java.util.HashMap;
import java.util.List;

public interface MainContract {
    interface View {
        void toast(String msg);
        void paintMarkers(List<Marker> items);
    }

    interface Presenter {
        void attachView(View view);
        void detachView();
        void requestLocationItem(HashMap items);
        void updateMarker(Marker marker);
    }
}
