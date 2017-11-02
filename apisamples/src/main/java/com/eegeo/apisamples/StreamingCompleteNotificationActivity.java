package com.eegeo.apisamples;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.map.OnInitialStreamingCompleteListener;
import com.eegeo.mapapi.map.OnMapReadyCallback;

public class StreamingCompleteNotificationActivity extends SoftBackButtonActivity {

    private MapView m_mapView;

    private OnInitialStreamingCompleteListener m_initialStreamingCompleteListener = new InitialStreamingCompleteListenerImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setContentView(R.layout.initial_streaming_complete_example_activity);

        m_mapView = (MapView) findViewById(R.id.streaming_complete_notification_activity);
        m_mapView.onCreate(savedInstanceState);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {

                map.addInitialStreamingCompleteListener(m_initialStreamingCompleteListener);
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

    private class InitialStreamingCompleteListenerImpl implements OnInitialStreamingCompleteListener {
        @Override
        public void onInitialStreamingComplete() {
            new AlertDialog.Builder(StreamingCompleteNotificationActivity.this)
                    .setTitle("Ready")
                    .setMessage("Initial map streaming completed.")
                    .setIcon(android.R.drawable.ic_dialog_map)
                    .show();
        }
    }

}
