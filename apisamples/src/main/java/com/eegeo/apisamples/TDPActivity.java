package com.eegeo.apisamples;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;

import com.eegeo.mapapi.geometry.LatLng;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.picking.PickResult;
import com.eegeo.mapapi.positioner.Positioner;
import com.eegeo.mapapi.positioner.PositionerOptions;
import com.eegeo.mapapi.util.Promise;
import com.eegeo.mapapi.util.Ready;

public class TDPActivity extends AppCompatActivity {

    private MapView m_mapView;
    private Button m_myButton;
    private EegeoMap m_eegeoMap = null;
    private Positioner m_positioner = null;
    private Handler m_timerHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialise the EegeoApi with your api key - this needs calling either in the application
        // instance, or in the activity that contains the MapView
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        // The MapView is contained in a layout xml
        setContentView(R.layout.tdp_activity);

        m_mapView = (MapView) findViewById(R.id.tdp_mapview);
        m_mapView.onCreate(savedInstanceState);

        m_myButton = (Button) findViewById(R.id.tdp_button);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;
                m_positioner = m_eegeoMap.addPositioner(new PositionerOptions()
                        .position(new LatLng(37.802115, -122.405592))
                );

                m_timerHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Point screenPoint = m_positioner.getScreenPoint();
                        m_myButton.setX(screenPoint.x);
                        m_myButton.setY(screenPoint.y);
                        m_timerHandler.postDelayed(this, 20);
                    }
                }, 2000);
            }
        });
    }

    public void onClickMapCollapse(View view) {
        if (m_eegeoMap != null) {
            m_eegeoMap.setMapCollapsed(!m_eegeoMap.isMapCollapsed());
        }
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
            m_eegeoMap.removePositioner(m_positioner);
        }

        m_mapView.onDestroy();
    }
}
