package com.depromeet.donkey.main.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.depromeet.donkey.R;
import com.skt.Tmap.TMapView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tmap_layout)
    LinearLayout tmapLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        ButterKnife.bind(this);
        TMapView tMapView = new TMapView(this);

        tMapView.setSKTMapApiKey(getString(R.string.tmap_key));
        tmapLayout.addView(tMapView);
    }
}
