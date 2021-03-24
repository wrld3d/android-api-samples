package com.eegeo.apisamples;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;
import android.widget.Toast;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.camera.CameraPosition;
import com.eegeo.mapapi.camera.CameraUpdateFactory;
import com.eegeo.mapapi.geometry.LatLng;
import com.eegeo.mapapi.indooroutlines.IndoorMapFloorOutlineInformation;
import com.eegeo.mapapi.indooroutlines.IndoorMapFloorOutlinePolygon;
import com.eegeo.mapapi.indooroutlines.IndoorMapFloorOutlinePolygonRing;
import com.eegeo.mapapi.indooroutlines.OnIndoorMapFloorOutlineInformationLoadedListener;
import com.eegeo.mapapi.indoors.OnIndoorEnteredListener;
import com.eegeo.mapapi.map.OnInitialStreamingCompleteListener;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.polygons.PolygonOptions;

import java.util.Locale;

public class QueryIndoorMapFloorOutlineInformationActivity extends WrldExampleActivity {
    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EegeoApi.init(this, getString(R.string.eegeo_api_key));
        setContentView(R.layout.query_indoor_map_floor_outline_information_activity);
        m_mapView = this.findViewById(R.id.query_indoor_map_floor_outline_information_mapview);
        m_mapView.onCreate(savedInstanceState);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;

                final int transparentBlue = ColorUtils.setAlphaComponent(Color.BLUE, 128);

                final OnIndoorMapFloorOutlineInformationLoadedListener listener = new OnIndoorMapFloorOutlineInformationLoadedListener() {
                    @Override
                    public void onIndoorMapFloorOutlineInformationLoaded(IndoorMapFloorOutlineInformation indoorMapFloorOutlineInformation) {
                        for (IndoorMapFloorOutlinePolygon outlinePolygon:indoorMapFloorOutlineInformation.getIndoorMapFloorOutlinePolygons()) {
                            PolygonOptions options = new PolygonOptions();

                            for (LatLng latLng:outlinePolygon.outerRing.latLngPoints) {
                                options.add(latLng);
                            }

                            for (IndoorMapFloorOutlinePolygonRing innerContour:outlinePolygon.innerRings) {
                                options.addHole(innerContour.latLngPoints);
                            }

                            options.fillColor(transparentBlue);
                            options.indoor(indoorMapFloorOutlineInformation.getIndoorMapId(), indoorMapFloorOutlineInformation.getIndoorMapFloorId());
                            m_eegeoMap.addPolygon(options);
                        }

                        displayToastMessage(indoorMapFloorOutlineInformation);
                    }
                };



                map.addInitialStreamingCompleteListener(new OnInitialStreamingCompleteListener() {
                    @Override
                    public void onInitialStreamingComplete() {
                        CameraPosition position = new CameraPosition.Builder()
                                .target(56.460078, -2.978325)
                                .indoor("westport_house", 2)
                                .zoom(18)
                                .tilt(0)
                                .build();
                        map.moveCamera(CameraUpdateFactory.newCameraPosition(position));
                    }
                });

                map.addOnIndoorEnteredListener(new OnIndoorEnteredListener() {
                    @Override
                    public void onIndoorEntered() {
                        m_eegeoMap.addIndoorMapFloorOutlineInformation("westport_house", 2, listener);
                    }
                });
            }
        });
    }

    private void displayToastMessage(IndoorMapFloorOutlineInformation indoorFloorOutlineInformation) {
        Toast.makeText(this,
                String.format(Locale.getDefault(), "IndoorMapMapFloorOutlineInformation for %s floor %d has finished loading.",
                        indoorFloorOutlineInformation.getIndoorMapId(),
                        indoorFloorOutlineInformation.getIndoorMapFloorId()
                ),
                Toast.LENGTH_LONG
        ).show();
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
