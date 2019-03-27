package com.eegeo.apisamples;

import android.os.Bundle;

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
                        new LatLng(37.786617, -122.404654),
                        new LatLng(37.795051, -122.403969),
                        new LatLng(37.797843, -122.407057),
                        new LatLng(37.798962, -122.398260),
                        new LatLng(37.794299, -122.395234),
                        new LatLng(37.792201, -122.400580)
                            )
                    .fillColor(0);

                final int pointCount = 100000;
                final LatLng sw = new LatLng(37.787, -122.4071);
                final LatLng ne = new LatLng(37.799, -122.3952);

                WeightedLatLngAlt[] data = generateRandomData(
                        pointCount,
                        sw,
                        ne,
                        1.0,
                        10.0
                );

                m_heatmap = m_eegeoMap.addHeatmap(
                    new HeatmapOptions()
                        .polygon(polygonOptions)
                        .resolution(512)
                        .blurRadiusMeters(25.f)
                        .add(data)
                );
            }
        });
    }


    private WeightedLatLngAlt[] generateRandomData(int count, LatLng sw, LatLng ne, double minWeight, double maxWeight) {
        Random random = new Random(1);
        ArrayList<WeightedLatLngAlt> points = new ArrayList<>();

        for (int i = 0; i < count; ++i) {

            double lat = random.nextDouble() * (ne.latitude - sw.latitude) + sw.latitude;
            double lng = random.nextDouble() * (ne.longitude - sw.longitude) + sw.longitude;
            // non-uniform distribution
            double r = random.nextDouble();
            double weight = r * r * (maxWeight - minWeight) + minWeight;

            points.add(new WeightedLatLngAlt(lat, lng, weight));
        }

        return points.toArray(new WeightedLatLngAlt[0]);

    }

    private WeightedLatLngAlt[] getData() {
        WeightedLatLngAlt data[] = {
                // transamerica
                //new WeightedLatLngAlt(37.795109, -122.402789)

                new WeightedLatLngAlt(37.795538, -122.403318),
                new WeightedLatLngAlt(37.795550, -122.403191),
                new WeightedLatLngAlt(37.795566, -122.403061),
                new WeightedLatLngAlt(37.795594, -122.402902),
                new WeightedLatLngAlt(37.795610, -122.402810),
                new WeightedLatLngAlt(37.795606, -122.402647),
                new WeightedLatLngAlt(37.795645, -122.403457),
                new WeightedLatLngAlt(37.795659, -122.403345),
                new WeightedLatLngAlt(37.795874, -122.403793),
                new WeightedLatLngAlt(37.795792, -122.403661),
                new WeightedLatLngAlt(37.795519, -122.403466),
                new WeightedLatLngAlt(37.795515, -122.403513),
                new WeightedLatLngAlt(37.795508, -122.403614),
                new WeightedLatLngAlt(37.795447, -122.403303),
                new WeightedLatLngAlt(37.795368, -122.403292),
                new WeightedLatLngAlt(37.795288, -122.403295),
                new WeightedLatLngAlt(37.795148, -122.403229),
                new WeightedLatLngAlt(37.795876, -122.400523),
                new WeightedLatLngAlt(37.795866, -122.400603),
                new WeightedLatLngAlt(37.795883, -122.400431),
                new WeightedLatLngAlt(37.795837, -122.400499),
                new WeightedLatLngAlt(37.795947, -122.400537),
                new WeightedLatLngAlt(37.795907, -122.400502)
        };
        return data;
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
