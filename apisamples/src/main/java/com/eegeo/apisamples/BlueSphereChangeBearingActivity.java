package com.eegeo.apisamples;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.geometry.LatLng;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.bluesphere.BlueSphere;

public class BlueSphereChangeBearingActivity extends SoftBackButtonActivity {

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;
    private BlueSphere m_bluesphere = null;
    private Handler m_timerHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setContentView(R.layout.bluesphere_change_bearing_activity);
        m_mapView = (MapView) findViewById(R.id.bluesphere_change_bearing_mapview);
        m_mapView.onCreate(savedInstanceState);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;
                
                final LatLng startPosition = new LatLng(56.459721, -2.977541);
                m_bluesphere = m_eegeoMap.getBlueSphere();
                m_bluesphere.setEnabled(true);
                m_bluesphere.setPosition(startPosition);
                m_bluesphere.setBearing(0);

                m_timerHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (m_eegeoMap != null) {
                            m_bluesphere.setBearing(m_bluesphere.getBearing() + 45);
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
            m_eegeoMap = null;
        }

        m_mapView.onDestroy();
    }

}

