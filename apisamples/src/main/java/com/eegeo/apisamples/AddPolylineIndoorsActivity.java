package com.eegeo.apisamples;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.eegeo.indoors.IndoorMapView;
import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.geometry.LatLng;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.polylines.Polyline;
import com.eegeo.mapapi.polylines.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class AddPolylineIndoorsActivity extends AppCompatActivity {

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;
    private IndoorMapView m_indoorMapView = null;
    private List<Polyline> m_polylines = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setContentView(R.layout.add_polyline_indoors_activity);
        m_mapView = (MapView) findViewById(R.id.add_polyline_indoors_mapview);
        m_mapView.onCreate(savedInstanceState);

        final int transparentRed = ColorUtils.setAlphaComponent(Color.RED, 128);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;

                RelativeLayout uiContainer = (RelativeLayout) findViewById(R.id.eegeo_ui_container);
                m_indoorMapView = new IndoorMapView(m_mapView, uiContainer, m_eegeoMap);

                final int numberOfFloors = 5;
                for (int floorId = 0; floorId < numberOfFloors; ++floorId) {
                    Polyline polyline = m_eegeoMap.addPolyline(new PolylineOptions()
                            .add(
                                    new LatLng(37.782084, -122.404578),
                                    new LatLng(37.782126, -122.404530),
                                    new LatLng(37.782057, -122.404440),
                                    new LatLng(37.782012, -122.404491))
                            .color(transparentRed)
                            .indoor("intercontinental_hotel_8628", floorId));
                    m_polylines.add(polyline);
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
            for (Polyline polyline : m_polylines) {
                m_eegeoMap.removePolyline(polyline);
            }
        }

        m_mapView.onDestroy();
    }

}
