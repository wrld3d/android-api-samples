package com.eegeo.apisamples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.camera.CameraUpdateFactory;
import com.eegeo.mapapi.geometry.LatLng;
import com.eegeo.mapapi.geometry.LatLngBounds;
import com.eegeo.mapapi.map.OnInitialStreamingCompleteListener;
import com.eegeo.mapapi.map.OnMapReadyCallback;

public class FrameCameraActivity extends AppCompatActivity {

    private MapView m_mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setContentView(R.layout.frame_camera_example_activity);
        m_mapView = (MapView) findViewById(R.id.frame_camera_mapview);
        m_mapView.onCreate(savedInstanceState);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
            map.addInitialStreamingCompleteListener(new OnInitialStreamingCompleteListener() {
                @Override
                public void onInitialStreamingComplete() {
                    map.moveCamera(CameraUpdateFactory.newLatLngBounds(
                            new LatLngBounds.Builder()
                                    .include(new LatLng(37.798886, -122.395116))
                                    .include(new LatLng(37.786647, -122.407015))
                                    .build()
                            , 0));
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
