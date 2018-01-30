package com.eegeo.apisamples;

import android.os.Bundle;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.geometry.LatLng;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.markers.Marker;
import com.eegeo.mapapi.markers.MarkerOptions;

public class MarkerWithElevationActivity extends WrldExampleActivity {

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;
    private Marker m_marker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setContentView(R.layout.marker_with_elevation_activity);
        m_mapView = (MapView) findViewById(R.id.marker_with_elevation_mapview);
        m_mapView.onCreate(savedInstanceState);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;
                m_marker = createAndAddMarker();
            }
        });
    }

    private Marker createAndAddMarker() {
        return m_eegeoMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.7952, -122.4028))
                .elevation(260.0)
                .labelText("Transamerica Pyramid"));
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
            m_eegeoMap.removeMarker(m_marker);
        }

        m_mapView.onDestroy();
    }

}


