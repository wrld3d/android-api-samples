package com.eegeo.apisamples;

import android.os.Bundle;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.camera.CameraPosition;
import com.eegeo.mapapi.camera.CameraUpdateFactory;
import com.eegeo.mapapi.map.OnInitialStreamingCompleteListener;
import com.eegeo.mapapi.map.OnMapReadyCallback;

public class MoveCameraActivity extends WrldExampleActivity {

    private MapView m_mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setContentView(R.layout.move_camera_example_activity);
        m_mapView = (MapView) findViewById(R.id.move_camera_mapview);
        m_mapView.onCreate(savedInstanceState);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                map.addInitialStreamingCompleteListener(new OnInitialStreamingCompleteListener() {
                    @Override
                    public void onInitialStreamingComplete() {
                        CameraPosition position = new CameraPosition.Builder()
                                .target(37.7952, -122.4028)
                                .zoom(19)
                                .build();

                        map.moveCamera(CameraUpdateFactory.newCameraPosition(position));
                    }
                });
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
