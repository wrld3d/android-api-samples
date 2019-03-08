package com.eegeo.apisamples;

import android.os.Bundle;
import android.widget.Toast;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.indoors.OnIndoorMapLoadedListener;
import com.eegeo.mapapi.indoors.OnIndoorMapUnloadedListener;
import com.eegeo.mapapi.map.OnMapReadyCallback;

public class IndoorMapLoadingNotificationActivity extends WrldExampleActivity {

    private MapView m_mapView;

    private OnIndoorMapLoadedListener m_indoorMapLoadedListener = new IndoorMapLoadedListenerImpl();
    private OnIndoorMapUnloadedListener m_indoorMapUnloadedListener = new IndoorMapUnloadedListenerImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setContentView(R.layout.indoor_map_loading_notification_activity);

        m_mapView = (MapView) findViewById(R.id.indoor_map_loading_notification_activity);
        m_mapView.onCreate(savedInstanceState);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {

                map.addOnIndoorMapLoadedListener(m_indoorMapLoadedListener);
                map.addOnIndoorMapUnloadedListener(m_indoorMapUnloadedListener);
            }
        });
    }

    private class IndoorMapLoadedListenerImpl implements OnIndoorMapLoadedListener {
        @Override
        public void onIndoorMapLoaded(final String indoorMapId) {
            Toast.makeText(
                IndoorMapLoadingNotificationActivity.this,
                String.format("Loaded indoor map: %s", indoorMapId),
                Toast.LENGTH_LONG).show();
        }
    }

    private class IndoorMapUnloadedListenerImpl implements OnIndoorMapUnloadedListener {
        @Override
        public void onIndoorMapUnloaded(final String indoorMapId) {
            Toast.makeText(
                IndoorMapLoadingNotificationActivity.this,
                String.format("Unloaded indoor map: %s", indoorMapId),
                Toast.LENGTH_LONG).show();
        }
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
