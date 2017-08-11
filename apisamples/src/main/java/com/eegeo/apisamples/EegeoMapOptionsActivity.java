package com.eegeo.apisamples;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.camera.CameraPosition;
import com.eegeo.mapapi.map.EegeoMapOptions;
import com.eegeo.mapapi.map.OnMapReadyCallback;

public class EegeoMapOptionsActivity extends AppCompatActivity {

    private MapView m_mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialise the EegeoApi with your api key - this needs calling either in the application
        // instance, or in the activity that contains the MapView
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        EegeoMapOptions eegeoMapOptions = new EegeoMapOptions();
        eegeoMapOptions.camera(
                new CameraPosition.Builder()
                        .target(37.8099, -122.4766)
                        .zoom(17)
                        .bearing(-30.0)
                        .build());

        // create MapView programmatically (rather than via layout .xml)
        m_mapView = new MapView(this, eegeoMapOptions);

        m_mapView.onCreate(savedInstanceState);

        setContentView(m_mapView);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                Toast.makeText(EegeoMapOptionsActivity.this, "WRLD start position configured", Toast.LENGTH_LONG).show();
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

}

