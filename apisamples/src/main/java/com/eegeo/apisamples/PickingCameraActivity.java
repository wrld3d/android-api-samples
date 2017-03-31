package com.eegeo.apisamples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.geometry.LatLngAlt;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.markers.MarkerOptions;

public class PickingCameraActivity extends AppCompatActivity {

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

        OnMapClickedHandler(EegeoMap eegeoMap) {
            this.m_eegeoMap = eegeoMap;
        }

        @Override
        public void onMapClick(LatLngAlt point) {
            m_eegeoMap.addMarker(new MarkerOptions()
                    .position(point.toLatLng())
                    .labelText("Picked"));
        }
    }
}
