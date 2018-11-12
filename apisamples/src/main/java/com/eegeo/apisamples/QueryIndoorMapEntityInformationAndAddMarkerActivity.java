package com.eegeo.apisamples;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.camera.CameraPosition;
import com.eegeo.mapapi.camera.CameraUpdateFactory;
import com.eegeo.mapapi.indoorentities.IndoorMapEntity;
import com.eegeo.mapapi.indoorentities.IndoorMapEntityInformation;
import com.eegeo.mapapi.indoorentities.OnIndoorMapEntityInformationChangedListener;
import com.eegeo.mapapi.map.OnInitialStreamingCompleteListener;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.markers.Marker;
import com.eegeo.mapapi.markers.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class QueryIndoorMapEntityInformationAndAddMarkerActivity extends WrldExampleActivity {
    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;
    private List<Marker> m_markers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EegeoApi.init(this, getString(R.string.eegeo_api_key));
        setContentView(R.layout.query_indoor_map_entity_information_and_add_marker_activity);
        m_mapView = (MapView) findViewById(R.id.query_indoor_map_entity_information_and_add_marker_mapview);
        m_mapView.onCreate(savedInstanceState);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;

                map.addIndoorMapEntityInformation("westport_house",
                    new OnIndoorMapEntityInformationChangedListener() {
                        @Override
                        public void onIndoorMapEntityInformationChanged(IndoorMapEntityInformation indoorMapEntityInformation) {
                            removeMarkers();
                            addMarkers(indoorMapEntityInformation);
                        }
                });

                map.addInitialStreamingCompleteListener(new OnInitialStreamingCompleteListener() {
                    @Override
                    public void onInitialStreamingComplete() {

                        CameraPosition position = new CameraPosition.Builder()
                                .target(56.459801, -2.977928)
                                .indoor("westport_house", 2)
                                .zoom(18)
                                .build();
                        map.moveCamera(CameraUpdateFactory.newCameraPosition(position));
                    }
                });
            }
        });
    }

    private void removeMarkers() {
        for (Marker marker : m_markers) {
            m_eegeoMap.removeMarker(marker);
        }
        m_markers.clear();
    }

    private void addMarkers(@NonNull final IndoorMapEntityInformation indoorMapEntityInformation)
    {
        for (IndoorMapEntity indoorMapEntity : indoorMapEntityInformation.getIndoorMapEntities()) {
            m_markers.add(m_eegeoMap.addMarker(
                    new MarkerOptions()
                            .indoor(indoorMapEntityInformation.getIndoorMapId(), indoorMapEntity.indoorMapFloorId)
                            .position(indoorMapEntity.position)
                            .elevation(2.0)
                            .labelText(indoorMapEntity.indoorMapEntityId))
            );
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