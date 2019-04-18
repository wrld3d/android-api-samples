package com.eegeo.apisamples;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.geometry.LatLng;
import com.eegeo.mapapi.geometry.WeightedLatLngAlt;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.heatmaps.Heatmap;
import com.eegeo.mapapi.heatmaps.HeatmapOptions;
import com.eegeo.mapapi.polygons.PolygonOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AddHeatmapActivity extends WrldExampleActivity {

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;
    private Heatmap m_heatmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setContentView(R.layout.add_heatmap_activity);
        m_mapView = (MapView) findViewById(R.id.add_heatmap_mapview);
        m_mapView.onCreate(savedInstanceState);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;

                final PolygonOptions polygonOptions = new PolygonOptions()
                    .add(
                            new LatLng(51.508604, -0.104459),
                            new LatLng(51.508443, -0.096146),
                            new LatLng(51.507262, -0.091732),
                            new LatLng(51.506585, -0.084959),
                            new LatLng(51.504438, -0.076184),
                            new LatLng(51.502556, -0.070538),
                            new LatLng(51.495054, -0.076442),
                            new LatLng(51.494416, -0.085674),
                            new LatLng(51.494161, -0.094522),
                            new LatLng(51.495264, -0.100525),
                            new LatLng(51.498727, -0.104775)
                            );


                final LatLng sw = new LatLng(51.493606, -0.105091);
                final LatLng ne = new LatLng(51.508994, -0.068091);

                final WeightedLatLngAlt[] data = generateRandomDataSequential(1000, sw, ne);

                m_heatmap = m_eegeoMap.addHeatmap(
                    new HeatmapOptions()
                        .polygon(polygonOptions)
                        .add(data)
                        .addDensityStop(0.0f, 10.0, 1.0)
                        .addDensityStop(1.0f, 20.0, 1.0)
                        .interpolateDensityByZoom(14.0, 17.0)
                );
            }
        });
    }

    private WeightedLatLngAlt[] generateRandomDataSequential(int count, LatLng sw, LatLng ne) {
        Random random = new Random(1);
        WeightedLatLngAlt points[] = new WeightedLatLngAlt[count];

        for (int i = 0; i < count; ++i) {
            double lat = random.nextDouble() * (ne.latitude - sw.latitude) + sw.latitude;
            double lng = random.nextDouble() * (ne.longitude - sw.longitude) + sw.longitude;
            points[i] = new WeightedLatLngAlt(lat, lng, 1.0);
        }

        return points;
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
            m_eegeoMap.removeHeatmap(m_heatmap);
        }

        m_mapView.onDestroy();
    }

}
