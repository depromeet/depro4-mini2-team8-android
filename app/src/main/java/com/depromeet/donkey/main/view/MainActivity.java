package com.depromeet.donkey.main.view;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.depromeet.donkey.R;
import com.depromeet.donkey.contents.view.ContentEditActivity;
import com.depromeet.donkey.main.data.Marker;
import com.depromeet.donkey.main.presenter.MainContract;
import com.depromeet.donkey.main.presenter.MainPresenter;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements TMapView.OnLongClickListenerCallback, LocationListener, MainContract.View,
                    TMapView.OnCalloutRightButtonClickCallback, NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.tmap_layout)
    LinearLayout tmapLayout;
    @BindView(R.id.main_toolbar)
    Toolbar toolbar;
    @BindView(R.id.main_drawer)
    DrawerLayout drawerLayout;
    @BindView(R.id.main_gps_stauts)
    ImageView gpsImg;

    private TMapView tMapView;
    private ActionBarDrawerToggle drawerToggle;
    private LocationManager locationManager;

    private String address;
    private boolean isGPSEnable = false;

    private double lat, lon;
    HashMap<String, String> items = new HashMap<>();

    private MainContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        ButterKnife.bind(this);
        tMapView = new TMapView(this);

        tMapView.setSKTMapApiKey(getString(R.string.tmap_key));
        tmapLayout.addView(tMapView);

        presenter = new MainPresenter(this);
        presenter.attachView(this);

        initToolbar();
        initGps();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");
        drawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        drawerToggle.setHomeAsUpIndicator(R.drawable.search);
    }

    private void initGps() {
        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            gpsImg.setImageDrawable(getDrawable(R.drawable.gps_on));
        } else {
            gpsImg.setImageDrawable(getDrawable(R.drawable.gps_off));
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    /***********************************TMap OnClickLinstenerCallback**********************************************/
    @Override
    public void onLongPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint) {
        Log.d(TAG, "long...");
    }

    /***********************************TMap LocationListener**********************************************/
    @Override
    public void onLocationChanged(final Location location) {
        double lon = location.getLongitude();
        double lat = location.getLatitude();
        isGPSEnable = true;
        Log.d(TAG, "lon : " + lon + "/lat : " + lat);
        tMapView.setCenterPoint(lon, lat);
        new GetMarkerThread(lat, lon).start();
    }

    private class GetMarkerThread extends Thread {
        private double lat;
        private double lon;

        public GetMarkerThread(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }

        @Override
        public void run() {
            com.depromeet.donkey.main.data.Address addTmp = separateAddress(lat, lon);
            String result = addTmp.getSi() + addTmp.getGu() + addTmp.getDong();
            if (address != null && !result.equals(address))
                return;
            address = result;

            items = new HashMap<>();
            items.put("si", addTmp.getSi());
            items.put("gu", addTmp.getGu());
            items.put("dong", addTmp.getDong());
            presenter.requestLocationItem(items);
        }
    }

    private class UpdateMarkerThread extends Thread {

        public UpdateMarkerThread(double lati, double longi) {
            lat = lati;
            lon = longi;
        }

        @Override
        public void run() {
                com.depromeet.donkey.main.data.Address addTmp = separateAddress(lat, lon);
                String result = addTmp.getSi() + addTmp.getGu() + addTmp.getDong();
                if (address != null && result.equals(address)) {
                    toast("현재 동이 아닙니다.");
                    return;
                }
        }
    }

    private com.depromeet.donkey.main.data.Address separateAddress(double lat, double lon) {
        String si = null;
        String gu = null;
        String dong = null;
        try {
            TMapData tMapData = new TMapData();
            String currentAddress = tMapData.convertGpsToAddress(lat, lon);
            //서울 한정으로 작업했음. 타지 X
            //시 구 동이 같으면 요청 X
            String[] separationAddress = currentAddress.split(" ");
            si = separationAddress[0];
            gu = separationAddress[1];
            dong = separationAddress[2];
            Log.d(TAG, currentAddress + "/" + si + gu + dong);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return new com.depromeet.donkey.main.data.Address(si, gu, dong);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.d(TAG, "onStatusChanged");
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.d(TAG, "onProviderEnabled");
        gpsImg.setImageDrawable(getDrawable(R.drawable.gps_on));
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.d(TAG, "onProviderDisabled");
        gpsImg.setImageDrawable(getDrawable(R.drawable.gps_off));
    }

    @Override
    public void onCalloutRightButton(TMapMarkerItem tMapMarkerItem) {
        Log.d(TAG, "onCalloutRightButton" + tMapMarkerItem.getCalloutTitle());
    }

    /***********************************View Callback**********************************************/
    @Override
    public void toast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void paintMarkers(List<Marker> items) {
        tMapView.removeAllMarkerItem();
        for (final Marker item : items) {
            TMapMarkerItem mapMarkerItem = new TMapMarkerItem();
            mapMarkerItem.setTMapPoint(new TMapPoint(item.getLat(), item.getLng()));
            mapMarkerItem.setName(item.getTitle());
            mapMarkerItem.setCanShowCallout(true);
            mapMarkerItem.setPosition((float) 0.5, (float) 1.0);
            mapMarkerItem.setIcon(BitmapFactory.decodeResource(getResources(), R.drawable.location_icon));
            mapMarkerItem.setCalloutRightButtonImage(BitmapFactory.decodeResource(getResources(), R.drawable.poi_popup));
            tMapView.addMarkerItem(String.valueOf(item.getPostNo()), mapMarkerItem);
            tMapView.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
                @Override
                public void onCalloutRightButton(TMapMarkerItem tMapMarkerItem) {
                    Log.d(TAG, tMapMarkerItem.getName());
                    // add startActivity here.
                    /*Intent intent = new Intent(MainActivity.this, null);
                    intent.putExtra("Marker", item);
                    startActivity(intent);*/
                }
            });
        }
    }

    public void onClick(View v) {
        switch (v. getId()) {
            case R.id.floating_add_map :
                Intent intent = new Intent(MainActivity.this, ContentEditActivity.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lon);
                intent.putExtra("si", items.get("si").toString());
                intent.putExtra("gu", items.get("gu").toString());
                intent.putExtra("dong", items.get("dong").toString());
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
