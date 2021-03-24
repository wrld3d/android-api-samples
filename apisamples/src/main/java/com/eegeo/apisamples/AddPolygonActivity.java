package com.eegeo.apisamples;

import android.graphics.Color;
import android.os.Bundle;
import androidx.core.graphics.ColorUtils;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.geometry.LatLng;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.polygons.Polygon;
import com.eegeo.mapapi.polygons.PolygonOptions;

public class AddPolygonActivity extends WrldExampleActivity {

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;
    private Polygon m_polygon = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setContentView(R.layout.add_polygon_activity);
        m_mapView = (MapView) findViewById(R.id.add_polygon_mapview);
        m_mapView.onCreate(savedInstanceState);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;
                m_polygon = m_eegeoMap.addPolygon(new PolygonOptions()
                        .add(
                                new LatLng(37.786617, -122.404654),
                                new LatLng(37.797843, -122.407057),
                                new LatLng(37.798962, -122.398260),
                                new LatLng(37.794299, -122.395234))
                        .fillColor(ColorUtils.setAlphaComponent(Color.BLUE, 128))
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
            m_eegeoMap.removePolygon(m_polygon);
        }

        m_mapView.onDestroy();
    }

}
