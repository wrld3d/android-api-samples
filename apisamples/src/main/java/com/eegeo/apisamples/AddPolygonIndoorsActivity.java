package com.eegeo.apisamples;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.graphics.ColorUtils;
import android.widget.RelativeLayout;

import com.eegeo.indoors.IndoorMapView;
import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.geometry.LatLng;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.polygons.Polygon;
import com.eegeo.mapapi.polygons.PolygonOptions;

import java.util.ArrayList;
import java.util.List;

public class AddPolygonIndoorsActivity extends WrldExampleActivity {

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;
    private IndoorMapView m_indoorMapView = null;
    private List<Polygon> m_polygons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setLockedOrientation();

        setContentView(R.layout.add_polygon_indoors_activity);
        m_mapView = (MapView) findViewById(R.id.add_polygon_indoors_mapview);
        m_mapView.onCreate(savedInstanceState);

        final int transparentBlue = ColorUtils.setAlphaComponent(Color.BLUE, 128);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;

                RelativeLayout uiContainer = (RelativeLayout) findViewById(R.id.eegeo_ui_container);
                m_indoorMapView = new IndoorMapView(m_mapView, uiContainer, m_eegeoMap);

                final int numberOfFloors = 5;
                for (int floorId = 0; floorId < numberOfFloors; ++floorId) {
                    Polygon polygon = m_eegeoMap.addPolygon(new PolygonOptions()
                            .add(
                                    new LatLng(37.782084, -122.404578),
                                    new LatLng(37.782126, -122.404530),
                                    new LatLng(37.782057, -122.404440),
                                    new LatLng(37.782012, -122.404491))
                            .fillColor(transparentBlue)
                            .indoor("intercontinental_hotel_8628", floorId));
                    m_polygons.add(polygon);
                }

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
            for (Polygon polygon : m_polygons) {
                m_eegeoMap.removePolygon(polygon);
            }
        }

        m_mapView.onDestroy();
    }

    private void setLockedOrientation() {
        if (getResources().getBoolean(R.bool.is_large_device)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        } else {
            // force portrait on phone
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

}
