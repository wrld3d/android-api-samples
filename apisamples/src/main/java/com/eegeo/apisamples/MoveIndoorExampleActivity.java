package com.eegeo.apisamples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.indoors.IndoorMap;
import com.eegeo.mapapi.indoors.OnIndoorEnteredListener;
import com.eegeo.mapapi.indoors.OnIndoorExitedListener;
import com.eegeo.mapapi.map.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MoveIndoorExampleActivity extends AppCompatActivity {

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setContentView(R.layout.move_floor_indoor_example_activity);
        m_mapView = (MapView) findViewById(R.id.move_floor_indoor_mapview);
        m_mapView.onCreate(savedInstanceState);

        final List<Button> buttons = new ArrayList<Button>();
        buttons.add((Button) findViewById(R.id.topfloor_indoor_button));
        buttons.add((Button) findViewById(R.id.moveup_indoor_button));
        buttons.add((Button) findViewById(R.id.movedown_indoor_button));
        buttons.add((Button) findViewById(R.id.bottomfloor_indoor_button));
        buttons.add((Button) findViewById(R.id.move_floor_exit_indoor_button));

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                IndoorEventListener listener = new IndoorEventListener(buttons);
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

    public void onTopFloor(View view) {
        IndoorMap indoorMap = m_eegeoMap.getActiveIndoorMap();
        m_eegeoMap.setIndoorFloor(indoorMap.floorCount - 1);
    }

    public void onBottomFloor(View view) {
        m_eegeoMap.setIndoorFloor(0);
    }

    public void onMoveUp(View view) {
        m_eegeoMap.moveIndoorUp();
    }

    public void onMoveDown(View view) {
        m_eegeoMap.moveIndoorDown();
    }

    public class IndoorEventListener implements OnIndoorEnteredListener, OnIndoorExitedListener {
        private List<Button> m_buttons;

        IndoorEventListener(List<Button> buttons) {
            this.m_buttons = buttons;
        }

        @Override
        public void onIndoorEntered() {
            SetButtonStates(true);
        }

        @Override
        public void onIndoorExited() {
            SetButtonStates(false);
        }

        private void SetButtonStates(boolean state) {
            for (Iterator<Button> i = m_buttons.iterator(); i.hasNext(); ) {
                Button b = i.next();
                b.setEnabled(state);
            }
        }
    }
}
