package com.depromeet.donkey.main.view;

import android.content.Intent;
import android.graphics.BitmapFactory;
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
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.annimon.stream.Collectors;
import com.depromeet.donkey.R;
import com.depromeet.donkey.content_edit.data.Post;
import com.depromeet.donkey.content_edit.view.ContentEditActivity;
import com.depromeet.donkey.content_list.view.ContentsListActivity;
import com.depromeet.donkey.login.data.Member;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.logging.SimpleFormatter;
import java.util.stream.Stream;

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
    HashMap<String, String> addressHash = new HashMap<>();

    private MainContract.Presenter presenter;
    private Member member;

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

        if (!getIntent().hasExtra("Member")) {
            toast("알 수 없는 오류입니다.");
            return;
        }

        member = (Member) getIntent().getSerializableExtra("Member");
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
            isGPSEnable = true;
        } else {
            gpsImg.setImageDrawable(getDrawable(R.drawable.gps_off));
            isGPSEnable = false;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    /***********************************TMap OnClickLinstenerCallback**********************************************/
    @Override
    public void onLongPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint) {
        Log.d(TAG, "lat " + tMapPoint.getLatitude() + "/ lon " + tMapPoint.getLongitude());
        if (!isGPSEnable) {
            toast("gps를 켜주세요.");
            return;
        }
        Marker item = new Marker(123, 99,
                Double.parseDouble(String.format("%.2f", tMapPoint.getLatitude())),
                Double.parseDouble(String.format("%.2f", tMapPoint.getKatechLon())),
                null, null, null, 100, null, null);

        Intent intent = new Intent(MainActivity.this, ContentEditActivity.class);
        intent.putExtra("Marker", item);
        intent.putExtra("Member", member);
        intent.putExtra("Address", addressHash);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200) {
            /*final Marker item = (Marker) data.getSerializableExtra("NewMarker");
            TMapMarkerItem mapMarkerItem = new TMapMarkerItem();
            mapMarkerItem.setTMapPoint(new TMapPoint(item.getLat(), item.getLng()));
            mapMarkerItem.setName(item.getTitle());
            mapMarkerItem.setCanShowCallout(true);
            mapMarkerItem.setPosition((float) 0.5, (float) 1.0);
            mapMarkerItem.setIcon(BitmapFactory.decodeResource(getResources(), R.drawable.location_icon));
            mapMarkerItem.setCalloutRightButtonImage(BitmapFactory.decodeResource(getResources(), R.drawable.speech_bubble));

            tMapView.addMarkerItem(String.valueOf(item.getPostNo()), mapMarkerItem);
            tMapView.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
                @Override
                public void onCalloutRightButton(TMapMarkerItem tMapMarkerItem) {
                    Intent intent = new Intent(MainActivity.this, ContentsListActivity.class);
                    intent.putExtra("Markers", new ArrayList<Marker>().add(item));
                    intent.putExtra("Member", member);
                    intent.putExtra("Address", addressHash);
                    startActivity(intent);
                }
            });*/
        }
    }

    /***********************************TMap LocationListener**********************************************/
    @Override
    public void onLocationChanged(final Location location) {
        lon = location.getLongitude();
        lat = location.getLatitude();
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

            addressHash = new HashMap<>();
            addressHash.put("si", addTmp.getSi());
            addressHash.put("gu", addTmp.getGu());
            addressHash.put("dong", addTmp.getDong());
            presenter.requestLocationItem(addressHash);
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
    public void paintMarkers(ArrayList<Marker> items) {
        tMapView.removeAllMarkerItem();


        LinkedHashSet<Pair<Float, Float>> points = new LinkedHashSet<>();
        items.forEach(item -> points.add(
                new Pair(item.getLat(), item.getLng())));

        /*com.annimon.stream.Stream.of(points)
                .filter(post ->
                        post.getLat() == floatFloatPair.getKey()
                                && post.getLng() == floatFloatPair.getValue()).collect(Collectors.toList())
        ))
                .collect(Collectors.toList());

        points.stream().map(floatFloatPair -> new MappedPost(floatFloatPair,
                mappedList.stream().filter(post ->
                        post.getLat() == floatFloatPair.getKey()
                                && post.getLng() == floatFloatPair.getValue()).collect(Collectors.toList())
        ))
                .collect(Collectors.toList());*/

        ArrayList<ArrayList<Marker>> markersList = new ArrayList();
        for (int i = 0 ; i < items.size() ; i ++) {
            markersList.add(new ArrayList<Marker>());
        }


        ArrayList<Marker> itemsClone = (ArrayList) items.clone();
        Iterator<Marker> iterator = items.iterator();

        int i = 0;
        while (iterator.hasNext()) {
            Marker marker = iterator.next();
            markersList.get(i).add(marker);
            itemsClone.remove(marker);

            ArrayList<Marker> clone = (ArrayList) itemsClone.clone();
            for (Marker item : clone) {
                if (marker.getLng() == item.getLng() &&
                        marker.getLat() == item.getLat()) {
                    markersList.get(i).add(item);
                    itemsClone.remove(item);
                }
            }
            i++;
        }

        for (ArrayList<Marker> markers : markersList) {
            for (Marker marker : markers) {
                Log.i(TAG, marker.getLat() + "/" + marker.getLng());
                Log.i(TAG, marker.getContent() + "/" + marker.getContent());
            }
            Log.i(TAG, "----------------------");
        }


        for (final ArrayList<Marker> markers : markersList) {
            TMapMarkerItem mapMarkerItem = new TMapMarkerItem();
            mapMarkerItem.setTMapPoint(new TMapPoint(markers.get(0).getLat(), markers.get(0).getLng()));
            mapMarkerItem.setName(markers.get(0).getTitle());
            mapMarkerItem.setCanShowCallout(true);
            mapMarkerItem.setPosition((float) 0.5, (float) 1.0);
            mapMarkerItem.setIcon(BitmapFactory.decodeResource(getResources(), R.drawable.location_icon));
            mapMarkerItem.setCalloutRightButtonImage(BitmapFactory.decodeResource(getResources(), R.drawable.speech_bubble));

            tMapView.addMarkerItem(String.valueOf(markers.get(0).getPostNo()), mapMarkerItem);
            tMapView.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
                @Override
                public void onCalloutRightButton(TMapMarkerItem tMapMarkerItem) {
                    Intent intent = new Intent(MainActivity.this, ContentsListActivity.class);
                    intent.putExtra("Markers", markers);
                    intent.putExtra("Member", member);
                    intent.putExtra("Address", addressHash);
                    startActivity(intent);
                }
            });
        }
    }

    /*public void onClick(View v) {
        switch (v. getId()) {
            case R.id.floating_add_map :
                Intent intent = new Intent(MainActivity.this, ContentEditActivity.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lon);
                intent.putExtra("si", addressHash.get("si").toString());
                intent.putExtra("gu", addressHash.get("gu").toString());
                intent.putExtra("dong", addressHash.get("dong").toString());
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
        }
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
