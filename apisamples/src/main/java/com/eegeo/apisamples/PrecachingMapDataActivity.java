package com.eegeo.apisamples;

import android.app.AlertDialog;
import android.os.Bundle;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.geometry.LatLng;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.precaching.OnPrecacheOperationCompletedListener;
import com.eegeo.mapapi.precaching.PrecacheOperationResult;

import java.util.Locale;

public class PrecachingMapDataActivity extends WrldExampleActivity
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
                // Precache a 2000 meter radius around this point
                m_eegeoMap.precache(
                        new LatLng(37.7952, -122.4028),
                        2000.0,
                        PrecachingMapDataActivity.this);
            }
        });
    }

    @Override
    public void onPrecacheOperationCompleted(PrecacheOperationResult precacheOperationResult) {
        AlertDialog dialog = new AlertDialog.Builder(PrecachingMapDataActivity.this)
                .setTitle("Precaching complete")
                .setMessage(String.format(Locale.getDefault(), "Precaching %s.",
                        precacheOperationResult.succeeded() ? "complete" : "failed"))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .create();
        dialog.show();
    }
}
