package com.eegeo.apisamples;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.geometry.LatLng;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.markers.Marker;
import com.eegeo.mapapi.markers.MarkerOptions;
import com.eegeo.indoors.IndoorMapView;

import java.util.ArrayList;
import java.util.List;

public class AddIndoorMarkerActivity extends SoftBackButtonActivity {

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;
    private IndoorMapView m_indoorMapView = null;

    private List<Marker> m_markers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setLockedOrientation();

        setContentView(R.layout.add_indoor_marker_activity);
        m_mapView = (MapView) findViewById(R.id.add_indoor_marker_mapview);
        m_mapView.onCreate(savedInstanceState);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;

                RelativeLayout uiContainer = (RelativeLayout) findViewById(R.id.eegeo_ui_container);
                m_indoorMapView = new IndoorMapView(m_mapView, uiContainer, m_eegeoMap);

                final int numberOfFloors = 7;
                for (int floorId = 0; floorId < numberOfFloors; ++floorId) {
                    Marker marker = m_eegeoMap.addMarker(new MarkerOptions()
                            .position(new LatLng(56.459948, -2.978094))
                            .indoor("westport_house", floorId)
                            .labelText("Marker on floor " + floorId));
                    m_markers.add(marker);
                }

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

        if (m_eegeoMap != null) {
            for (Marker marker : m_markers) {
                m_eegeoMap.removeMarker(marker);
            }
        }

        m_mapView.onDestroy();
    }

    private void setLockedOrientation() {
        if (getResources().getBoolean(R.bool.is_large_device)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        } else {
            // force portrait on phone
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

}

