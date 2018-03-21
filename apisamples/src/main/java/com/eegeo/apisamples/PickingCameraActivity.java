package com.eegeo.apisamples;

import android.os.Bundle;
import android.widget.Toast;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.geometry.LatLngAlt;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.markers.Marker;
import com.eegeo.mapapi.markers.MarkerOptions;

public class PickingCameraActivity extends WrldExampleActivity {

    private MapView m_mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setContentView(R.layout.picking_camera_example_activity);
        m_mapView = (MapView) findViewById(R.id.picking_camera_mapview);
        m_mapView.onCreate(savedInstanceState);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                map.addOnMapClickListener(new OnMapClickedHandler(map));
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
        m_mapView.onDestroy();
    }

    public class OnMapClickedHandler implements EegeoMap.OnMapClickListener {
        private EegeoMap m_eegeoMap;
        private Marker m_marker = null;

        OnMapClickedHandler(EegeoMap eegeoMap) {
            this.m_eegeoMap = eegeoMap;
        }

        @Override
        public void onMapClick(LatLngAlt point) {
            if (m_marker != null) {
                m_eegeoMap.removeMarker(m_marker);
                m_marker = null;
            }

            MarkerOptions markerOptions = new MarkerOptions()
                                            .position(point.toLatLng())
                                            .labelText("Picked");

            if (m_eegeoMap.getActiveIndoorMap() != null) {
                markerOptions.indoor(m_eegeoMap.getActiveIndoorMap().id, m_eegeoMap.getCurrentFloorIndex());
            }

            m_marker = m_eegeoMap.addMarker(markerOptions);

            Toast.makeText(PickingCameraActivity.this, String.format("LatLng [%f, %f]; altitude %f m",
                    point.latitude, point.longitude, point.altitude), Toast.LENGTH_LONG).show();
        }
    }
}
