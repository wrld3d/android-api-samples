package com.eegeo.apisamples;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.camera.CameraPosition;
import com.eegeo.mapapi.camera.CameraUpdateFactory;
import com.eegeo.mapapi.indoorentities.IndoorMapEntity;
import com.eegeo.mapapi.indoorentities.IndoorMapEntityInformation;
import com.eegeo.mapapi.indoorentities.OnIndoorMapEntityInformationChangedListener;
import com.eegeo.mapapi.map.OnMapReadyCallback;

import java.util.List;


public class QueryIndoorMapEntityInformation extends WrldExampleActivity {
    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;
    private IndoorMapEntityInformation m_indoorMapEntityInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EegeoApi.init(this, getString(R.string.eegeo_api_key));
        setContentView(R.layout.query_indoor_map_entity_information);
        m_mapView = (MapView) findViewById(R.id.query_inderior_highlights_mapview);
        m_mapView.onCreate(savedInstanceState);
        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;
                CameraPosition position = new CameraPosition.Builder()
                        .target(56.459801, -2.977928)
                        .zoom(18)
                        .build();
                map.moveCamera(CameraUpdateFactory.newCameraPosition(position));
            }
        });
    }

    public void onAddIndoorMapEntityInformationPressed(View view) {
        OnIndoorMapEntityInformationChangedListener indoorMapEntityInformationChangedListener
                = new OnIndoorMapEntityInformationChangedListener() {
            @Override
            public void onIndoorMapEntityInformationChanged(
                    IndoorMapEntityInformation indoorMapEntityInformation) {
                Toast.makeText(
                        QueryIndoorMapEntityInformation.this,
                        String.format(  "IndoorMapEntityInformation for %s " + "\n " +
                                        "load state: %s \n " +
                                        "entities: %d",
                                indoorMapEntityInformation.getIndoorMapId(),
                                indoorMapEntityInformation.getLoadState(),
                                indoorMapEntityInformation.getIndoorMapEntities().size()
                        ),
                        Toast.LENGTH_LONG
                ).show();

                printAllEntityIds();
            }
        };

        m_indoorMapEntityInformation = m_eegeoMap.addIndoorMapEntityInformation(
                "westport_house",
                indoorMapEntityInformationChangedListener);
    }

    public void onRemoveIndoorMapEntityInformationPressed(View view) {
        m_eegeoMap.removeIndoorMapEntityInformation(m_indoorMapEntityInformation);
    }

    public void printAllEntityIds()
    {
        List<IndoorMapEntity> indoorMapEntities = m_indoorMapEntityInformation.getIndoorMapEntities();

        Log.i("IndoorMap: Load State: ", m_indoorMapEntityInformation.getLoadState().toString());

        for(IndoorMapEntity indoorMapEntity : indoorMapEntities)
        {
            Log.i("IndoorMapEntity: Id: ", indoorMapEntity.indoorMapEntityId);
        }
    }
}