package com.eegeo.apisamples;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.buildings.BuildingHighlight;
import com.eegeo.mapapi.buildings.BuildingHighlightOptions;
import com.eegeo.mapapi.geometry.LatLng;
import com.eegeo.mapapi.map.OnMapReadyCallback;

public class RemoveBuildingHighlightActivity extends AppCompatActivity {

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;
    private BuildingHighlight m_highlight = null;
    private Handler m_timerHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setContentView(R.layout.remove_building_highlight_activity);
        m_mapView = (MapView) findViewById(R.id.remove_building_highlight_mapview);
        m_mapView.onCreate(savedInstanceState);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;
                m_timerHandler.postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                if (m_highlight == null) {
                                    m_highlight = m_eegeoMap.addBuildingHighlight(new BuildingHighlightOptions()
                                            .highlightBuildingAtLocation(new LatLng(37.795189, -122.402777))
                                            .color(ColorUtils.setAlphaComponent(Color.YELLOW, 128))
                                    );
                                }
                                else {
                                    m_eegeoMap.removeBuildingHighlight(m_highlight);
                                    m_highlight = null;
                                }

                                m_timerHandler.postDelayed(this, 2000);
                            }
                        },
                        4000
                );

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

        if (m_eegeoMap != null && m_highlight != null) {
            m_eegeoMap.removeBuildingHighlight(m_highlight);
        }

        m_mapView.onDestroy();
    }

}
