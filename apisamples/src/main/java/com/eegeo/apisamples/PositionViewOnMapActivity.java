package com.eegeo.apisamples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.graphics.Point;
import android.graphics.PointF;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.geometry.LatLng;
import com.eegeo.mapapi.geometry.LatLngAlt;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.positioner.Positioner;
import com.eegeo.mapapi.positioner.PositionerOptions;
import com.eegeo.mapapi.positioner.OnPositionerChangedListener;
import com.eegeo.ui.util.ViewAnchor;

import static com.eegeo.mapapi.geometry.ElevationMode.HeightAboveGround;

public class PositionViewOnMapActivity extends AppCompatActivity {

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;

    View m_calloutView;
    TextView m_screenView;
    TextView m_worldView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialise the EegeoApi with your api key - this needs calling either in the application
        // instance, or in the activity that contains the MapView
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        // The MapView is contained in a layout xml
        setContentView(R.layout.position_view_on_map_activity);

        m_mapView = (MapView) findViewById(R.id.position_view_on_map_mapview);
        m_mapView.onCreate(savedInstanceState);

        //The coordinate will be printed in the top left corner of the screen
        m_calloutView = findViewById(R.id.position_view_on_map_callout);
        m_calloutView.setVisibility(View.INVISIBLE);
        m_screenView = (TextView) findViewById(R.id.screen_text_view_on_map_text);
        m_worldView = (TextView) findViewById(R.id.world_text_view_on_map_text);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;

                map.addPositioner(new PositionerOptions()
                        .position(new LatLng(37.802355, -122.405848))
                        .elevation(10.0)
                        .elevationMode(HeightAboveGround)
                        .positionerChangedListener(new OnScreenPointChangedListener(m_calloutView, m_screenView, m_worldView))
                );
            }
        });
    }

    public void onClickMapCollapse(View view) {
        if (m_eegeoMap != null) {
            m_eegeoMap.setMapCollapsed(!m_eegeoMap.isMapCollapsed());
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

    private class OnScreenPointChangedListener implements OnPositionerChangedListener {

        private View m_callout;
        private TextView m_screenTextView;
        private TextView m_worldTextView;

        OnScreenPointChangedListener(@NonNull View callout,
                                     @NonNull TextView screenTextView,
                                     @NonNull TextView worldTextView)
        {
            m_callout = callout;
            m_screenTextView = screenTextView;
            m_worldTextView = worldTextView;
        }

        //This will be called each time the screen coordinate of the positioner changes.
        @UiThread
        public void onPositionerChanged(Positioner positioner) {
            if(positioner.isScreenPointValid())
            {
                Point screenPoint = positioner.getScreenPoint();
                LatLngAlt transformedPoint = positioner.getTransformedPoint();

                //Print out the screen coordinate.
                m_screenTextView.setText(String.format(
                        getString(R.string.screen_point_add_positioner_activity),
                        screenPoint.x,
                        screenPoint.y));

                m_worldTextView.setText(String.format(
                        getString(R.string.world_point_add_positioner_activity),
                        transformedPoint.latitude,
                        transformedPoint.longitude,
                        transformedPoint.altitude));

                m_callout.setVisibility(View.VISIBLE);

                //We want to anchor the view at the bottom center where the call out tab is.
                PointF anchorUV = new PointF(
                        0.5f, //0.0f=left, 0.5f=hCenter, 1.0f=right
                        1.0f); //0.0f=top, 0.5f=vCenter, 1.0f=bottom

                ViewAnchor.positionView(m_callout, screenPoint, anchorUV);
            }
            else {
                String string = getString(R.string.not_visible_add_positioner_activity);
                m_screenTextView.setText(string);
                m_worldTextView.setText(string);
                m_callout.setVisibility(View.INVISIBLE);
            }
        }
    }
}
