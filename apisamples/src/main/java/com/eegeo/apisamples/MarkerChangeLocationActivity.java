package com.eegeo.apisamples;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.geometry.LatLng;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.markers.Marker;
import com.eegeo.mapapi.markers.MarkerOptions;

public class MarkerChangeLocationActivity extends AppCompatActivity {

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;
    private Marker m_marker = null;
    private boolean m_locationToggle = false;
    private Handler m_timerHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setContentView(R.layout.marker_change_location_activity);
        m_mapView = (MapView) findViewById(R.id.marker_change_location_mapview);
        m_mapView.onCreate(savedInstanceState);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;

                final LatLng locationA = new LatLng(37.784560, -122.402092);
                final LatLng locationB = new LatLng(37.783372, -122.400834);

                m_marker = m_eegeoMap.addMarker(new MarkerOptions()
                        .position(locationA));

                m_timerHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (m_eegeoMap != null) {
                            m_locationToggle = !m_locationToggle;
                            LatLng newLocation = m_locationToggle ? locationB : locationA;
                            m_marker.setPosition(newLocation);
                            m_timerHandler.postDelayed(this, 2000);
                        }
                    }
                }, 2000);

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
            m_eegeoMap.removeMarker(m_marker);
            m_eegeoMap = null;
        }

        m_mapView.onDestroy();
    }

}

