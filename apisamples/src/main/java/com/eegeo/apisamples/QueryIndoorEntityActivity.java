package com.eegeo.apisamples;

import android.os.Bundle;
import android.view.View;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.camera.CameraPosition;
import com.eegeo.mapapi.camera.CameraUpdateFactory;
import com.eegeo.mapapi.indoorentities.IndoorEntityPickedMessage;
import com.eegeo.mapapi.indoorentities.OnIndoorEntityPickedListener;
import com.eegeo.mapapi.map.OnMapReadyCallback;

import java.util.ArrayList;

public class QueryIndoorEntityActivity extends WrldExampleActivity {

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setContentView(R.layout.query_indoor_entities_example_activity);
        m_mapView = (MapView) findViewById(R.id.query_indoor_entities_mapview);
        m_mapView.onCreate(savedInstanceState);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;
                CameraPosition position = new CameraPosition.Builder()
                        .target(56.459801, -2.977928)
                        .indoor("westport_house", 2)
                        .zoom(15)
                        .build();
                map.moveCamera(CameraUpdateFactory.newCameraPosition(position));

                map.addOnIndoorEntityPickedListener(new EntityPickedListener());
            }
        });

    }

    protected void onClearHighlightsButtonPressed(View view)
    {
        m_eegeoMap.clearAllIndoorEntityHighlights();
    }

    public class EntityPickedListener implements OnIndoorEntityPickedListener {
        @Override
        public void onIndoorEntityPicked(IndoorEntityPickedMessage message) {
            m_eegeoMap.setIndoorEntityHighlights(message.indoorMapId(), message.indoorMapEntityIds(), 0x77ff00ff);
        }
    }
}
