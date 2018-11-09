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
import java.util.HashMap;

public class PickingIndoorEntityActivity extends WrldExampleActivity {

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;
    private HashMap<String, Integer> m_entityIdsToColorIndex;
    private ArrayList<Integer> m_colors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m_entityIdsToColorIndex = new HashMap<String, Integer>();
        m_colors = new ArrayList<Integer>();

        m_colors.add(0x7fff0000);
        m_colors.add(0x7f00ff00);
        m_colors.add(0x7f0000ff);

        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setContentView(R.layout.picking_indoor_entities_example_activity);
        m_mapView = (MapView) findViewById(R.id.picking_indoor_entities_mapview);
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

    public void onClearHighlightsButtonPressed(View view)
    {
        m_eegeoMap.clearAllIndoorEntityHighlights();
        m_entityIdsToColorIndex.clear();
    }

    public class EntityPickedListener implements OnIndoorEntityPickedListener {
        @Override
        public void onIndoorEntityPicked(IndoorEntityPickedMessage message) {

            for (String indoorMapEntityId : message.indoorMapEntityIds) {
                int currentColorIdx = m_entityIdsToColorIndex.containsKey(indoorMapEntityId) ? m_entityIdsToColorIndex.get(indoorMapEntityId) + 1 : 0;
                if(currentColorIdx == m_colors.size())
                {
                    currentColorIdx = 0;
                }
                m_entityIdsToColorIndex.put(indoorMapEntityId, currentColorIdx);
            }

            m_eegeoMap.setIndoorEntityHighlights(message.indoorMapId, message.indoorMapEntityIds, m_colors.get(m_entityIdsToColorIndex.get(message.indoorMapEntityIds.get(0))));
        }
    }
}
