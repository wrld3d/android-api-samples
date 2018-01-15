package com.eegeo.apisamples;

import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ImageView;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.geometry.LatLng;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.positioner.OnPositionerChangedListener;
import com.eegeo.mapapi.positioner.Positioner;
import com.eegeo.mapapi.positioner.PositionerOptions;
import com.eegeo.ui.util.ViewAnchor;

public class PositionCodeCreatedViewOnMapActivity extends WrldExampleActivity {

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;
    private OnPositionerChangedListener m_positionerChangedListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialise the EegeoApi with your api key - this needs calling either in the application
        // instance, or in the activity that contains the MapView
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        // The MapView is contained in a layout xml
        setContentView(R.layout.position_code_created_view_on_map_activity);

        m_mapView = (MapView) findViewById(R.id.position_code_created_view_on_map_mapview);
        m_mapView.onCreate(savedInstanceState);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;

                ImageView imageView = new ImageView(PositionCodeCreatedViewOnMapActivity.this);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                imageView.setImageResource(R.drawable.wrld3d);
                m_mapView.addView(imageView);
                imageView.setVisibility(View.INVISIBLE);

                m_positionerChangedListener = new ViewAnchorAdapter(imageView, 0.5f, 0.5f);
                m_eegeoMap.addPositionerChangedListener(m_positionerChangedListener);

                m_eegeoMap.addPositioner(new PositionerOptions()
                        .position(new LatLng(37.802355, -122.405848))
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

    private class ViewAnchorAdapter implements OnPositionerChangedListener {

        private View m_view;
        private PointF m_anchorUV;

        ViewAnchorAdapter(@NonNull View view, float u, float v)
        {
            m_view = view;
            m_anchorUV = new PointF(u, v);
        }

        @UiThread
        public void onPositionerChanged(Positioner positioner) {
            if(positioner.isScreenPointProjectionDefined()) {
                m_view.setVisibility(View.VISIBLE);
                Point screenPoint = positioner.getScreenPointOrNull();
                if(screenPoint != null)
                    ViewAnchor.positionView(m_view, screenPoint, m_anchorUV);
            }
            else {
            m_view.setVisibility(View.INVISIBLE);
        }
        }
    }
}
