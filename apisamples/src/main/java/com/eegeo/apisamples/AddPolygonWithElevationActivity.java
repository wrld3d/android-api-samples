package com.eegeo.apisamples;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.graphics.ColorUtils;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.geometry.LatLng;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.polygons.Polygon;
import com.eegeo.mapapi.polygons.PolygonOptions;

public class AddPolygonWithElevationActivity extends WrldExampleActivity {

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;
    private Polygon m_lowerPolygon = null;
    private Polygon m_upperPolygon = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setContentView(R.layout.add_polygon_with_elevation_activity);
        m_mapView = (MapView) findViewById(R.id.add_polygon_with_elevation_mapview);
        m_mapView.onCreate(savedInstanceState);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;
                LatLng[] points = {
                        new LatLng(55.945976, -3.162339),
                        new LatLng(55.943671, -3.158673),
                        new LatLng(55.941648, -3.159911),
                        new LatLng(55.943248, -3.163275)
                };
                m_lowerPolygon = m_eegeoMap.addPolygon(new PolygonOptions()
                        .add(points)
                        .fillColor(ColorUtils.setAlphaComponent(Color.BLUE, 128)));
                m_upperPolygon = m_eegeoMap.addPolygon(new PolygonOptions()
                        .add(points)
                        .elevation(200.0)
                        .fillColor(ColorUtils.setAlphaComponent(Color.RED, 128))
                );
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

        if (m_eegeoMap != null) {
            m_eegeoMap.removePolygon(m_lowerPolygon);
            m_eegeoMap.removePolygon(m_upperPolygon);
        }

        m_mapView.onDestroy();
    }

}
