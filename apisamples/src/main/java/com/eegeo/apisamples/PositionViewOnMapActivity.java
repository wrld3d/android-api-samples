package com.eegeo.apisamples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.graphics.Point;
import android.graphics.PointF;
import android.view.View;
import android.widget.Button;

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

import static com.eegeo.mapapi.geometry.ElevationMode.HeightAboveGround;

public class PositionViewOnMapActivity extends AppCompatActivity {

    private MapView m_mapView;
    private Button m_layoutCreatedButton;
    private EegeoMap m_eegeoMap = null;

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

        m_layoutCreatedButton = (Button) findViewById(R.id.position_view_on_map_button);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;

                map.addPositioner(new PositionerOptions()
                        .position(new LatLng(37.802355, -122.405848))
                        .elevation(10.0)
                        .elevationMode(HeightAboveGround)
                        .positionerChangedListener(new ViewAnchorAdapter(m_layoutCreatedButton, 0.5f, 0.5f))
                );

                CameraPosition position = new CameraPosition.Builder()
                        .target(37.802355, -122.405848)
                        .zoom(19)
                        .build();
                map.moveCamera(CameraUpdateFactory.newCameraPosition(position));
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
        private PointF m_uv;

        ViewAnchorAdapter(@NonNull View view, float u, float v)
        {
            m_view = view;
            m_uv = new PointF(u, v);
        }

        @UiThread
        public void onPositionerChanged(Positioner positioner)
        {
            Point screenPoint = positioner.getScreenPoint();
            m_view.setX(screenPoint.x - (m_view.getWidth ()*m_uv.x));
            m_view.setY(screenPoint.y - (m_view.getHeight()*m_uv.y));
        }
    }
}
