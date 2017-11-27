package com.eegeo.apisamples;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.geometry.ElevationMode;
import com.eegeo.mapapi.geometry.LatLng;
import com.eegeo.mapapi.map.OnInitialStreamingCompleteListener;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.markers.MarkerOptions;
import com.eegeo.mapapi.picking.PickResult;
import com.eegeo.mapapi.util.Ready;

public class PlaceObjectsOnBuildingsActivity extends SoftBackButtonActivity implements OnInitialStreamingCompleteListener {

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setContentView(R.layout.place_objects_on_buildings_activity);
        m_mapView = (MapView) findViewById(R.id.place_objects_on_buildings_mapview);
        m_mapView.onCreate(savedInstanceState);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;
                m_eegeoMap.addInitialStreamingCompleteListener(PlaceObjectsOnBuildingsActivity.this);
            }
        });
    }

    @Override
    public void onInitialStreamingComplete() {
        addMarkerAboveBuilding(new LatLng(37.795159, -122.404336), "Point A");
        addMarkerAboveBuilding(new LatLng(37.794817, -122.403543), "Point B");
    }

    private void addMarkerAboveBuilding(final LatLng latLng, final String title) {
        m_eegeoMap.pickFeatureAtLatLng(latLng)
                .then(new Ready<PickResult>() {
                    @UiThread
                    @Override
                    public void ready(PickResult pickResult) {
                        m_eegeoMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .elevation(pickResult.intersectionPoint.altitude)
                                .elevationMode(ElevationMode.HeightAboveSeaLevel)
                                .labelText(title));
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

}
