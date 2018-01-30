package com.eegeo.apisamples;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.graphics.Point;
import android.graphics.PointF;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

public class PositionViewOnMapActivity extends WrldExampleActivity {

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;
    private OnPositionerChangedListener m_positionerChangedListener = null;

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

        m_calloutView = findViewById(R.id.position_view_on_map_callout);
        m_calloutView.setVisibility(View.INVISIBLE);
        m_screenView = (TextView) findViewById(R.id.screen_text_view_on_map_text);
        m_worldView = (TextView) findViewById(R.id.world_text_view_on_map_text);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;

                m_positionerChangedListener = new OnScreenPointChangedListener(m_calloutView, m_screenView, m_worldView);
                m_eegeoMap.addPositionerChangedListener(m_positionerChangedListener);

                map.addPositioner(new PositionerOptions()
                        .position(new LatLng(37.802355, -122.405848))
                        .elevation(10.0)
                        .elevationMode(HeightAboveGround)
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

        @UiThread
        public void onPositionerChanged(Positioner positioner) {
            String notVisibleString = getString(R.string.not_visible_add_positioner_activity);

            if(positioner.isScreenPointProjectionDefined())
            {
                Point screenPoint = positioner.getScreenPointOrNull();
                if(screenPoint != null)
                {
                    m_screenTextView.setText(String.format(
                            getString(R.string.screen_point_add_positioner_activity),
                            screenPoint.x,
                            screenPoint.y));

                    PointF anchorUV = new PointF(0.5f, 1.0f);
                    ViewAnchor.positionView(m_callout, screenPoint, anchorUV);
                }
                else {
                    m_screenTextView.setText(notVisibleString);
                }

                LatLngAlt transformedPoint = positioner.getTransformedPointOrNull();
                if(transformedPoint != null)
                {
                    m_worldTextView.setText(String.format(
                            getString(R.string.world_point_add_positioner_activity),
                            transformedPoint.latitude,
                            transformedPoint.longitude,
                            transformedPoint.altitude));
                }
                else {
                    m_worldTextView.setText(notVisibleString);
                }

                m_callout.setVisibility(View.VISIBLE);
            }
            else {
                m_screenTextView.setText(notVisibleString);
                m_worldTextView.setText(notVisibleString);
                m_callout.setVisibility(View.INVISIBLE);
            }
        }
    }


}
