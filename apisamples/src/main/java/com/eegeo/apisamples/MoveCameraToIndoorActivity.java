package com.eegeo.apisamples;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.camera.CameraPosition;
import com.eegeo.mapapi.camera.CameraUpdateFactory;
import com.eegeo.mapapi.indoors.OnIndoorEnteredListener;
import com.eegeo.mapapi.indoors.OnIndoorExitedListener;
import com.eegeo.mapapi.map.OnInitialStreamingCompleteListener;
import com.eegeo.mapapi.map.OnMapReadyCallback;

public class MoveCameraToIndoorActivity extends SoftBackButtonActivity {

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;
    private boolean m_indoors = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setContentView(R.layout.move_camera_to_indoor_example_activity);
        m_mapView = (MapView) findViewById(R.id.move_camera_to_indoor_mapview);
        m_mapView.onCreate(savedInstanceState);

        final Button button = (Button) findViewById(R.id.exit_indoor_button);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;
                map.addInitialStreamingCompleteListener(new OnInitialStreamingCompleteListener() {
                    @Override
                    public void onInitialStreamingComplete() {
                        IndoorEventListener listener = new IndoorEventListener(button);
                        map.addOnIndoorEnteredListener(listener);
                        map.addOnIndoorExitedListener(listener);

                        CameraPosition position = new CameraPosition.Builder()
                                .target(37.782332,  -122.404667)
                                .indoor("intercontinental_hotel_8628", 2)
                                .zoom(19)
                                .bearing(270)
                                .build();
                        map.moveCamera(CameraUpdateFactory.newCameraPosition(position));

                    }
                });
            }
        });
    }

    public void onClick(View view) {
        if (m_indoors) {
            m_eegeoMap.exitIndoorMap();
        }
    }

    public class IndoorEventListener implements OnIndoorEnteredListener, OnIndoorExitedListener {
        private Button m_button;

        IndoorEventListener(Button button) {
            this.m_button = button;
        }

        @Override
        public void onIndoorEntered() {
            m_indoors = true;
            m_button.setEnabled(true);
        }

        @Override
        public void onIndoorExited() {
            m_indoors = false;
            m_button.setEnabled(false);
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
