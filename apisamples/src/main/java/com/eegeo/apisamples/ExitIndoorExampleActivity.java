package com.eegeo.apisamples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.indoors.OnIndoorEnteredListener;
import com.eegeo.mapapi.indoors.OnIndoorExitedListener;
import com.eegeo.mapapi.map.OnMapReadyCallback;

public class ExitIndoorExampleActivity extends AppCompatActivity {

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setContentView(R.layout.exit_indoor_example_activity);
        m_mapView = (MapView) findViewById(R.id.exit_indoor_mapview);
        m_mapView.onCreate(savedInstanceState);

        final Button exitButton = (Button) findViewById(R.id.exit_indoor_button);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                IndoorEventListener listener = new IndoorEventListener(exitButton);
                map.addOnIndoorEnteredListener(listener);
                map.addOnIndoorExitedListener(listener);
                m_eegeoMap = map;
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

    public void onExit(View view) {
        m_eegeoMap.onExitIndoorClicked();
    }

    public class IndoorEventListener implements OnIndoorEnteredListener, OnIndoorExitedListener {
        private Button m_button;

        IndoorEventListener(Button button) {
            this.m_button = button;
        }

        @Override
        public void onIndoorEntered() {
            m_button.setEnabled(true);
        }

        @Override
        public void onIndoorExited() {
            m_button.setEnabled(false);
        }
    }
}
