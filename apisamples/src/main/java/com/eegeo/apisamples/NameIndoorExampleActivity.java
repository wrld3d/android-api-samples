package com.eegeo.apisamples;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.indoors.IndoorMap;
import com.eegeo.mapapi.indoors.OnIndoorEnteredListener;
import com.eegeo.mapapi.indoors.OnIndoorExitedListener;
import com.eegeo.mapapi.map.OnMapReadyCallback;

public class NameIndoorExampleActivity extends WrldExampleActivity {

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setContentView(R.layout.name_indoor_example_activity);
        m_mapView = (MapView) findViewById(R.id.name_indoor_mapview);
        m_mapView.onCreate(savedInstanceState);

        final Button exitButton = (Button) findViewById(R.id.name_exit_indoor_button);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;
                IndoorEventListener listener = new IndoorEventListener(exitButton, m_eegeoMap, NameIndoorExampleActivity.this);
                map.addOnIndoorEnteredListener(listener);
                map.addOnIndoorExitedListener(listener);
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
        private EegeoMap m_map;
        private Context m_context;

        IndoorEventListener(Button button, EegeoMap map, Context context) {
            this.m_button = button;
            this.m_map = map;
            this.m_context = context;
        }

        @Override
        public void onIndoorEntered() {
            IndoorMap indoorMap = m_map.getActiveIndoorMap();
            new AlertDialog.Builder(m_context)
                    .setTitle("Entered Indoor Map:")
                    .setMessage(indoorMap.name)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            m_button.setEnabled(true);
        }

        @Override
        public void onIndoorExited() {
            m_button.setEnabled(false);
        }
    }
}
