package com.eegeo.apisamples;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.eegeo.indoors.IndoorMapView;
import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.camera.CameraPosition;
import com.eegeo.mapapi.camera.CameraUpdateFactory;
import com.eegeo.mapapi.map.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;

public class GetInteriorHighlightsActivity extends WrldExampleActivity {

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;
    private IndoorMapView m_interiorView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setContentView(R.layout.get_interior_highlights_activity);
        m_mapView = (MapView) findViewById(R.id.get_inderior_highlights_mapview);
        m_mapView.onCreate(savedInstanceState);

        m_mapView.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(final EegeoMap map) {
                RelativeLayout uiContainer = (RelativeLayout) findViewById(R.id.eegeo_ui_container);
                m_interiorView = new IndoorMapView(m_mapView, uiContainer, map);
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

    public void onAllHighlightsPressed(View view)
    {
        highlightAllEntities();
    }

    public void onAllHighlightsOnFloorPressed(View view)
    {
        highlightAllEntitiesCurrentFloor();
    }

    public void onClearHighlightsButtonPressed(View view)
    {
        m_eegeoMap.clearAllIndoorEntityHighlights();
    }

    private void highlightAllEntities()
    {
        String[] result = m_eegeoMap.getActiveIndoorMap().entityIds;

        ArrayList<Integer> ColorList = new ArrayList<Integer>();
        ColorList.add(0x7fff0000);
        ColorList.add(0x7fffff00);
        ColorList.add(0x7f00ff00);
        ColorList.add(0x7f00ffff);
        ColorList.add(0x7f0000ff);
        ColorList.add(0x7fff00ff);

        int colorCounter = 0;

        for(String highlight : result )
        {
            ArrayList<String> highlightColection = new ArrayList<String>();
            highlightColection.add(highlight);
            m_eegeoMap.setIndoorEntityHighlights(InteriorID, highlightColection, ColorList.get(colorCounter % ColorList.size()));
            colorCounter++;
        }
    }

    private void highlightAllEntitiesCurrentFloor()
    {
        String InteriorID = m_eegeoMap.getActiveIndoorMap().id;

        List<String> result = m_eegeoMap.getInteriorHighlightsOnFloor(InteriorID, m_eegeoMap.getCurrentFloorIndex());

        ArrayList<Integer> ColorList = new ArrayList<Integer>();
        ColorList.add(0x7fff0000);
        ColorList.add(0x7fffff00);
        ColorList.add(0x7f00ff00);
        ColorList.add(0x7f00ffff);
        ColorList.add(0x7f0000ff);
        ColorList.add(0x7fff00ff);

        int colorCounter = 0;

        for(String highlight : result )
        {
            ArrayList<String> highlightColection = new ArrayList<String>();
            highlightColection.add(highlight);
            m_eegeoMap.setIndoorEntityHighlights(InteriorID, highlightColection, ColorList.get(colorCounter % ColorList.size()));
            colorCounter++;
        }
    }
}
