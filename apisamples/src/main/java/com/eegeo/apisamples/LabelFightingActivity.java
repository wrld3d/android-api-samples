package com.eegeo.apisamples;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.eegeo.indoors.IndoorMapView;
import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.camera.CameraPosition;
import com.eegeo.mapapi.camera.CameraUpdateFactory;
import com.eegeo.mapapi.geometry.LatLng;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.markers.Marker;
import com.eegeo.mapapi.markers.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class LabelFightingActivity extends AppCompatActivity {

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;
    private IndoorMapView m_indoorMapView = null;

    private boolean m_titleToggle = false;
    private List<Marker> m_markers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setLockedOrientation();

        setContentView(R.layout.label_fighting_activity);
        m_mapView = (MapView) findViewById(R.id.label_fighting_mapview);
        m_mapView.onCreate(savedInstanceState);

        final String markerLongPrefix = "Marker: ";

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;

                RelativeLayout uiContainer = (RelativeLayout) findViewById(R.id.eegeo_ui_container);
                m_indoorMapView = new IndoorMapView(m_mapView, uiContainer, m_eegeoMap);

                for(int j = 0; j < 8; ++j) {
                    for (int i = 0; i < 8; ++i) {
                        m_markers.add(m_eegeoMap.addMarker(new MarkerOptions()
                                .position(new LatLng(56.460526 + (float)j*0.00005f, -2.972298 + (float) i * 0.00005f))
                                .labelText(markerLongPrefix + Integer.toString(m_markers.size()))
                                .indoor("EIM-e16a94b1-f64f-41ed-a3c6-8397d9cfe607", 1)));
                    }
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        m_mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        m_mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (m_eegeoMap != null) {
            for (Marker marker : m_markers) {
                m_eegeoMap.removeMarker(marker);
            }
        }

        m_mapView.onDestroy();
    }


    private void setLockedOrientation() {
        if (getResources().getBoolean(R.bool.is_large_device)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        } else {
            // force portrait on phone
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
}

