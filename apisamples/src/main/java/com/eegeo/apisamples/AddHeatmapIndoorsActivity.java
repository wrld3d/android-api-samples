package com.eegeo.apisamples;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.graphics.ColorUtils;
import android.widget.RelativeLayout;

import com.eegeo.indoors.IndoorMapView;
import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.geometry.LatLng;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.heatmaps.Heatmap;
import com.eegeo.mapapi.heatmaps.HeatmapOptions;

import java.util.ArrayList;
import java.util.List;

public class AddHeatmapIndoorsActivity extends WrldExampleActivity {

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;
    private IndoorMapView m_indoorMapView = null;
    private List<Heatmap> m_heatmaps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setLockedOrientation();

        setContentView(R.layout.add_heatmap_indoors_activity);
        m_mapView = (MapView) findViewById(R.id.add_heatmap_indoors_mapview);
        m_mapView.onCreate(savedInstanceState);

        final int transparentRed = ColorUtils.setAlphaComponent(Color.RED, 128);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;

                RelativeLayout uiContainer = (RelativeLayout) findViewById(R.id.eegeo_ui_container);
                m_indoorMapView = new IndoorMapView(m_mapView, uiContainer, m_eegeoMap);

                final int numberOfFloors = 5;
                for (int floorId = 0; floorId < numberOfFloors; ++floorId) {
                    Heatmap heatmap = m_eegeoMap.addHeatmap(new HeatmapOptions()
                            .add(
                                    new LatLng(37.782084, -122.404578),
                                    new LatLng(37.782126, -122.404530),
                                    new LatLng(37.782057, -122.404440),
                                    new LatLng(37.782012, -122.404491))
                            .fillColor(transparentRed)
                            .indoor("intercontinental_hotel_8628", floorId));
                    m_heatmaps.add(heatmap);
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
            for (Heatmap heatmap : m_heatmaps) {
                m_eegeoMap.removeHeatmap(heatmap);
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
