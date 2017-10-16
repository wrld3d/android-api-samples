package com.eegeo.apisamples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.graphics.Point;
import android.widget.TextView;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.camera.CameraPosition;
import com.eegeo.mapapi.camera.CameraUpdateFactory;
import com.eegeo.mapapi.geometry.LatLng;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.positioner.Positioner;
import com.eegeo.mapapi.positioner.PositionerOptions;
import com.eegeo.mapapi.positioner.OnPositionerChangedListener;

public class AddPositionerActivity extends AppCompatActivity {

    private MapView m_mapView;
    TextView m_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialise the EegeoApi with your api key - this needs calling either in the application
        // instance, or in the activity that contains the MapView
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        // The MapView is contained in a layout xml
        setContentView(R.layout.add_positioner_activity);

        m_mapView = (MapView) findViewById(R.id.add_positioner_mapview);
        m_mapView.onCreate(savedInstanceState);

        m_textView = (TextView) findViewById(R.id.add_positioner_textview);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {

                //Add a Positioner to a point on the map
                map.addPositioner(new PositionerOptions()
                        .position(new LatLng(37.802115, -122.405592))
                        .positionerChangedListener(new OnScreenPointChangedListener(m_textView)
                    )
                );

                CameraPosition position = new CameraPosition.Builder()
                        .target(37.802355, -122.405848)
                        .zoom(19)
                        .build();
                map.moveCamera(CameraUpdateFactory.newCameraPosition(position));
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

    private class OnScreenPointChangedListener implements OnPositionerChangedListener {

        private TextView m_textView;

        OnScreenPointChangedListener(@NonNull TextView textView)
        {
            m_textView = textView;
        }

        //This will be called each time the screen coordinate of the positioner changes.
        @UiThread
        public void onPositionerChanged(Positioner positioner)
        {
            Point screenPoint = positioner.getScreenPoint();

            String string= String.format(getString(R.string.message_add_positioner_activity), screenPoint.x, screenPoint.y);
            m_textView.setText(string);
        }
    }
}
