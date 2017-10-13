package com.eegeo.apisamples;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.geometry.LatLng;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.positioner.OnPositionerChangedListener;
import com.eegeo.mapapi.positioner.Positioner;
import com.eegeo.mapapi.positioner.PositionerOptions;

import java.util.Hashtable;


public class TDPActivity extends AppCompatActivity {

    private MapView m_mapView;
    private Button m_layoutCreatedButton;
    private EegeoMap m_eegeoMap = null;
    private Positioner m_positioner = null;
    private OnPositionerChangedListener m_positionerChangedListener = new PositionerListener();
    private Hashtable<Positioner, View> m_positionerToView = new Hashtable<>();
    private int m_nextButton = 1;

    private LatLng[] m_positions = {
            new LatLng(37.788126, -122.398103),
            new LatLng(37.784754, -122.402831),
            new LatLng(37.795205, -122.402788),
            new LatLng(37.795658, -122.393962)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialise the EegeoApi with your api key - this needs calling either in the application
        // instance, or in the activity that contains the MapView
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        // The MapView is contained in a layout xml
        setContentView(R.layout.tdp_activity);

        m_mapView = (MapView) findViewById(R.id.tdp_mapview);
        m_mapView.onCreate(savedInstanceState);

        m_layoutCreatedButton = (Button) findViewById(R.id.tdp_button);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;

                addViewAtPosition(m_layoutCreatedButton, new LatLng(37.802115, -122.405592));

                for (LatLng position : m_positions) {
                    View button = createAndAddButtonView();
                    addViewAtPosition(button, position);
                }
            }
        });
    }

    private void addViewAtPosition(View view, LatLng position) {
        Positioner positioner = m_eegeoMap.addPositioner(new PositionerOptions()
                .position(position)
                .positionerChangedListener(m_positionerChangedListener)
        );
        m_positionerToView.put(positioner, view);
    }

    public void onClickMapCollapse(View view) {
        if (m_eegeoMap != null) {
            m_eegeoMap.setMapCollapsed(!m_eegeoMap.isMapCollapsed());
        }
    }

    private Button createAndAddButtonView() {
        Button button = new Button(this);
        button.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        button.setTransformationMethod(null);
        button.setText(String.format("%s %d", getString(R.string.positioner_code_created_view_label), m_nextButton));


        ++m_nextButton;

        m_mapView.addView(button);
        return button;

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
            m_eegeoMap.removePositioner(m_positioner);
        }

        m_mapView.onDestroy();
    }

    private class PositionerListener implements OnPositionerChangedListener {
        @Override
        public void onPositionerChanged(Positioner positioner) {
            if (m_positionerToView.containsKey(positioner)) {
                View button = m_positionerToView.get(positioner);
                Point screenPoint = positioner.getScreenPoint();
                button.setX(screenPoint.x);
                button.setY(screenPoint.y);
            }
        }
    }
}
