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

public class MarkerChangeTextActivity extends AppCompatActivity {

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;
    private Marker m_marker = null;
    private boolean m_titleToggle = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setContentView(R.layout.marker_change_text_activity);
        m_mapView = (MapView) findViewById(R.id.marker_change_text_mapview);
        m_mapView.onCreate(savedInstanceState);

        final String titleA = "Initial marker text";
        final String titleB = "Altered marker text";


        final Handler timerHandler = new Handler();

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;
                m_marker = m_eegeoMap.addMarker(new MarkerOptions()
                        .position(new LatLng(37.784560, -122.402092))
                        .labelText(titleA));

                timerHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (m_eegeoMap != null) {
                            m_marker.setTitle(m_titleToggle ? titleA : titleB);
                            m_titleToggle = !m_titleToggle;
                        }
                        timerHandler.postDelayed(this, 2000);
                    }
                }, 5000);

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

