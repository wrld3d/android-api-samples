package com.eegeo.apisamples;

import android.graphics.Color;
import android.os.Bundle;
import androidx.core.graphics.ColorUtils;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.buildings.BuildingHighlightOptions;
import com.eegeo.mapapi.geometry.LatLng;
import com.eegeo.mapapi.map.OnInitialStreamingCompleteListener;
import com.eegeo.mapapi.map.OnMapReadyCallback;

public class AddBuildingHighlightActivity extends WrldExampleActivity implements OnInitialStreamingCompleteListener {

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setContentView(R.layout.add_building_highlight_activity);
        m_mapView = (MapView) findViewById(R.id.add_building_highlight_mapview);
        m_mapView.onCreate(savedInstanceState);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;
                m_eegeoMap.addInitialStreamingCompleteListener(AddBuildingHighlightActivity.this);
            }
        });
    }


    @Override
    public void onInitialStreamingComplete() {
        m_eegeoMap.addBuildingHighlight(new BuildingHighlightOptions()
                .highlightBuildingAtLocation(new LatLng(37.795189, -122.402777))
                .color(ColorUtils.setAlphaComponent(Color.YELLOW, 128)));
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

        m_mapView.onDestroy();
    }

}
