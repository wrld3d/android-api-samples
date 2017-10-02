package com.eegeo.apisamples;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.buildings.BuildingContour;
import com.eegeo.mapapi.buildings.BuildingDimensions;
import com.eegeo.mapapi.buildings.BuildingHighlight;
import com.eegeo.mapapi.buildings.BuildingHighlightOptions;
import com.eegeo.mapapi.buildings.BuildingInformation;
import com.eegeo.mapapi.buildings.OnBuildingInformationReceivedListener;
import com.eegeo.mapapi.geometry.ElevationMode;
import com.eegeo.mapapi.geometry.LatLng;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.markers.MarkerOptions;
import com.eegeo.mapapi.polylines.PolylineOptions;

public class QueryBuildingInformationActivity extends AppCompatActivity {

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;
    private BuildingHighlight m_highlight = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setContentView(R.layout.query_building_information_activity);
        m_mapView = (MapView) findViewById(R.id.query_building_information_mapview);
        m_mapView.onCreate(savedInstanceState);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;
                m_highlight = m_eegeoMap.addBuildingHighlight(new BuildingHighlightOptions()
                        .highlightBuildingAtLocation(new LatLng(37.784079, -122.396762))
                        .informationOnly()
                        .buildingInformationReceivedListener(new OnBuildingInformationReceivedListener() {
                            @Override
                            public void onBuildingInformationReceived(BuildingHighlight buildingHighlight) {
                                BuildingInformation buildingInformation = buildingHighlight.getBuildingInformation();
                                if (buildingInformation == null) {
                                    Toast.makeText(QueryBuildingInformationActivity.this, String.format("No building information was received for building highlight"), Toast.LENGTH_LONG).show();
                                    return;
                                }

                                Toast.makeText(QueryBuildingInformationActivity.this, buildingInformation.buildingId, Toast.LENGTH_LONG).show();

                                BuildingDimensions buildingDimensions = buildingInformation.buildingDimensions;
                                double buildingHeight = buildingDimensions.topAltitude - buildingDimensions.baseAltitude;
                                String title = String.format("Height: %1$.2f m", buildingHeight);
                                m_eegeoMap.addMarker(new MarkerOptions()
                                        .labelText(title)
                                        .position(buildingDimensions.centroid)
                                        .elevation(buildingDimensions.topAltitude)
                                        .elevationMode(ElevationMode.HeightAboveSeaLevel)
                                );

                                for (BuildingContour contour : buildingInformation.contours)
                                {
                                    m_eegeoMap.addPolyline(new PolylineOptions()
                                            .add(contour.points)
                                            .add(contour.points[0])
                                            .elevationMode(ElevationMode.HeightAboveSeaLevel)
                                            .elevation(contour.topAltitude)
                                            .color(Color.BLUE)
                                    );

                                }
                            }
                        })

                );

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

        if (m_eegeoMap != null && m_highlight != null) {
            m_eegeoMap.removeBuildingHighlight(m_highlight);
        }

        m_mapView.onDestroy();
    }

}
