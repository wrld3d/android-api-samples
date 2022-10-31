package com.eegeo.apisamples;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.geometry.LatLng;
import com.eegeo.mapapi.geometry.WeightedLatLngAlt;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.heatmaps.Heatmap;
import com.eegeo.mapapi.heatmaps.HeatmapOptions;
import com.eegeo.mapapi.polygons.PolygonOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SetLabelsEnabledActivity extends WrldExampleActivity {

    private static final long DELAY_INTERVAL = 2000;

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;
    private Handler m_handler = null;
    private Runnable m_runnable = null;
    private boolean m_labelsEnabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        super.onCreate(savedInstanceState);
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setContentView(R.layout.set_labels_enabled_activity);
        m_mapView = (MapView) findViewById(R.id.set_layers_enabled_mapview);
        m_mapView.onCreate(savedInstanceState);

        m_handler = new Handler();
        m_runnable = new Runnable() {
            @Override
            public void run() {
                m_labelsEnabled = !m_labelsEnabled;
                m_eegeoMap.setLabelsEnabled(m_labelsEnabled);
                m_handler.postDelayed(this, DELAY_INTERVAL);
            }
        };

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;
                m_eegeoMap.setLabelsEnabled(true);
                m_handler.postDelayed(m_runnable, DELAY_INTERVAL);
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

        m_handler.removeCallbacks(m_runnable);

        m_mapView.onDestroy();
    }

}
