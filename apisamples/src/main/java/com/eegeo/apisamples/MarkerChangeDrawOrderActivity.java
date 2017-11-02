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

public class MarkerChangeDrawOrderActivity extends SoftBackButtonActivity {

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;
    private Marker m_markerA = null;
    private Marker m_markerB = null;
    private boolean m_drawOrderToggle = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setContentView(R.layout.marker_change_draw_order_activity);
        m_mapView = (MapView) findViewById(R.id.marker_change_draw_order_mapview);
        m_mapView.onCreate(savedInstanceState);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;

                final int highPriorityDrawOrder = 0;
                final int midPriorityDrawOrder = 1;
                final int lowPriorityDrawOrder = 2;

                m_markerA = m_eegeoMap.addMarker(new MarkerOptions()
                        .position(new LatLng(37.784560, -122.402256))
                        .labelText("Marker A")
                        .drawOrder(midPriorityDrawOrder)
                );

                m_markerB = m_eegeoMap.addMarker(new MarkerOptions()
                        .position(new LatLng(37.784560, -122.402016))
                        .labelText("Marker B")
                        .drawOrder(lowPriorityDrawOrder)
                );

                final Handler handler = new Handler();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (m_eegeoMap != null) {
                            m_drawOrderToggle = !m_drawOrderToggle;
                            m_markerB.setDrawOrder(m_drawOrderToggle ? highPriorityDrawOrder : lowPriorityDrawOrder);
                            handler.postDelayed(this, 2000);
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
            m_eegeoMap.removeMarker(m_markerA);
            m_eegeoMap.removeMarker(m_markerB);
            m_eegeoMap = null;
        }

        m_mapView.onDestroy();
    }

}
