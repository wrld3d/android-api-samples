package com.eegeo.apisamples;

import android.app.AlertDialog;
import android.os.Bundle;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.geometry.LatLng;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.precaching.OnPrecacheOperationCompletedListener;
import com.eegeo.mapapi.precaching.PrecacheOperation;
import com.eegeo.mapapi.precaching.PrecacheOperationResult;

import java.util.Locale;

public class CancelPrecachingMapDataActivity extends WrldExampleActivity
        implements OnPrecacheOperationCompletedListener {

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
                // Start precaching a 2000 meter radius around this point
                PrecacheOperation precacheOperation = m_eegeoMap.precache(
                        new LatLng(37.7952, -122.4028),
                        2000.0,
                        CancelPrecachingMapDataActivity.this);
                // Cancel the precache operation
                precacheOperation.cancel();
            }
        });
    }

    @Override
    public void onPrecacheOperationCompleted(PrecacheOperationResult precacheOperationResult) {
        AlertDialog dialog = new AlertDialog.Builder(CancelPrecachingMapDataActivity.this)
                .setTitle("Precache status")
                .setMessage(String.format(Locale.getDefault(), "Precaching %s",
                        precacheOperationResult.succeeded() ? "complete" : "cancelled"))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .create();
        dialog.show();
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
