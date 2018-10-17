package com.eegeo.apisamples;

import android.os.Bundle;
import android.view.View;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.camera.CameraPosition;
import com.eegeo.mapapi.camera.CameraUpdateFactory;

import com.eegeo.mapapi.map.OnMapReadyCallback;

import java.util.ArrayList;

public class HighlightIndoorEntityActivity extends WrldExampleActivity {

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setContentView(R.layout.highlight_indoor_entities_example_activity);
        m_mapView = (MapView) findViewById(R.id.highlight_indoor_entities_mapview);
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
            }
        });

    }

    public void onHighlightEntitiesButtonPressed(View view)
    {
        highlightEntities();
    }

    public void onClearHighlightsButtonPressed(View view)
    {
        m_eegeoMap.clearAllIndoorEntityHighlights();
    }

    private void highlightEntities()
    {
        ArrayList<String> indoorEntityIdsRed = new ArrayList<String>();
        indoorEntityIdsRed.add("0007");
        indoorEntityIdsRed.add("Meeting Room Small");

        m_eegeoMap.setIndoorEntityHighlights("westport_house", indoorEntityIdsRed, 0x7fff0000);

        ArrayList<String> indoorEntityIdsBlue = new ArrayList<String>();
        indoorEntityIdsBlue.add("0002");
        indoorEntityIdsBlue.add("Meeting Room Large");

        m_eegeoMap.setIndoorEntityHighlights("westport_house", indoorEntityIdsBlue, 0x7f0000ff);

        ArrayList<String> indoorEntityIdsGreen = new ArrayList<String>();
        indoorEntityIdsGreen.add("0033");

        m_eegeoMap.setIndoorEntityHighlights("westport_house", indoorEntityIdsGreen, 0x7f00ff00);
    }
}
