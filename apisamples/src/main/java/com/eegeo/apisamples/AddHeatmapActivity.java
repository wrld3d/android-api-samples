package com.eegeo.apisamples;

import android.os.Bundle;
import android.view.View;

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

    class DataSet
    {
        final public WeightedLatLngAlt[] WeightedPoints;
        final public double WeightMin;
        final public double WeightMax;

        DataSet(
            WeightedLatLngAlt[] weightedPoints,
            double weightMin,
            double weightMax
        )
        {
            WeightedPoints = weightedPoints;
            WeightMin = weightMin;
            WeightMax = weightMax;
        }
    };

    class Gradient
    {
        final int[] Colors;
        final float[] StartParams;
        final float IntensityBias;

        Gradient(
            int[] colors,
            float[] startParams,
            float intensityBias
        )
        {
            Colors = colors;
            StartParams = startParams;
            IntensityBias = intensityBias;
        }
    }

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;
    private Heatmap m_heatmap = null;
    private float m_minIntensityScale = 0.2f;
    private float m_maxIntensityScale = 5.0f;
    private boolean m_occlusionEnabled = true;
    private int m_occlusionFlags = HeatmapOptions.OCCLUSION_BUILDINGS | HeatmapOptions.OCCLUSION_TREES;
    private List<DataSet> m_dataSets = new ArrayList<>();
    private List<Gradient> m_gradients = new ArrayList<>();
    private int m_currentDataIndex = 0;
    private int m_currentGradientIndex = 0;


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
                            );

                final LatLng sw = new LatLng(37.787, -122.4071);
                final LatLng ne = new LatLng(37.799, -122.3952);

                m_dataSets.add(new DataSet(
                    generateRandomDataSequential(1000, sw, ne,0.0,50.0),
                    0.0,
                    50.0
                ));

                m_dataSets.add(new DataSet(
                    generateRandomDataSequential(100000, sw, ne,-20.0,35.0),
                    // saturate outliers
                    -10.0,
                    30.0
                ));

                m_dataSets.add(new DataSet(
                        generateRandomDataDiverging(1000, sw, ne,-100.0,0.0, 100.0),
                        -100.0,
                        100.0
                ));

                // Suitable for sequential data, with transparency near zero, similar to
                // http://colorbrewer2.org/#type=sequential&scheme=GnBu&n=5
                m_gradients.add(new Gradient(
                    new int[]{0xffffff00, 0xf0f9e8ff,0xbae4bcff,0x7bccc4ff,0x43a2caff,0x0868acff},
                    new float[]{0.f, 0.2f, 0.4f, 0.6f, 0.8f, 1.f},
                    0.0f
                ));

                // Suitable for sequential data, with transparency near zero, similar to
                // http://colorbrewer2.org/#type=sequential&scheme=YlOrRd&n=5
                m_gradients.add(new Gradient(
                    new int[]{0xffffff00,0xffffb2ff,0xfecc5cff,0xfd8d3cff,0xf03b20ff,0xbd0026ff},
                    new float[]{0.f, 0.2f, 0.4f, 0.6f, 0.8f, 1.f},
                    0.0f
                ));

                // Suitable for diverging data, with transparency around center, similar to
                // http://colorbrewer2.org/#type=diverging&scheme=RdYlBu&n=6
                m_gradients.add(new Gradient(
                    new int[]{0xd73027ff,0xfc8d59ff,0xfee090ff,0xffffff00,0xe0f3f8ff,0x91bfdbff,0x4575b4ff},
                    new float[]{0.f, 0.2f, 0.4f, 0.5f, 0.6f, 0.8f, 1.f},
                    0.5f
                ));


                m_heatmap = m_eegeoMap.addHeatmap(
                    new HeatmapOptions()
                        .polygon(polygonOptions)
                        .resolution(512)
                        .add(m_dataSets.get(m_currentDataIndex).WeightedPoints)
                        .weightMin(m_dataSets.get(m_currentDataIndex).WeightMin)
                        .weightMax(m_dataSets.get(m_currentDataIndex).WeightMax)
                        .radiusMinMeters(5.0)
                        .radiusMaxMeters(25.0)
                        .radiusBlend(0.0f)
                        .opacity(1.0f)
                        .occludedFeatures(m_occlusionFlags)
                        .gradient(m_gradients.get(m_currentGradientIndex).Colors, m_gradients.get(m_currentGradientIndex).StartParams)
                        .intensityScale(1.0f)
                        .intensityBias(m_gradients.get(m_currentGradientIndex).IntensityBias)
                );

                // todo_heatmap mutators
                // resolution
                // radiusMin/max
            }
        });
    }


    private WeightedLatLngAlt[] generateRandomDataSequential(int count, LatLng sw, LatLng ne, double minWeight, double maxWeight) {
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

    private WeightedLatLngAlt[] generateRandomDataDiverging(int count, LatLng sw, LatLng ne, double minWeight, double mid, double maxWeight) {
        Random random = new Random(1);
        ArrayList<WeightedLatLngAlt> points = new ArrayList<>();

        for (int i = 0; i < count; ++i) {

            double lat = random.nextDouble() * (ne.latitude - sw.latitude) + sw.latitude;
            double lng = random.nextDouble() * (ne.longitude - sw.longitude) + sw.longitude;
            double r = random.nextDouble();
            double extreme = random.nextBoolean() ? maxWeight : minWeight;
            double weight = r * r * (extreme - mid) + mid;
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


    public void onClickRadiusIncr(View view) {
        float radiusBlend = Math.min(m_heatmap.getRadiusBlend() + 0.05f, 1.0f);
        m_heatmap.setRadiusBlend(radiusBlend);
    }

    public void onClickRadiusDecr(View view) {
        float radiusBlend = Math.max(m_heatmap.getRadiusBlend() - 0.05f, 0.0f);
        m_heatmap.setRadiusBlend(radiusBlend);
    }

    public void onClickIntensityIncr(View view) {
        float intensityScale = Math.min(m_heatmap.getIntensityScale() + 0.1f, m_maxIntensityScale);
        m_heatmap.setIntensityScale(intensityScale);
    }

    public void onClickIntensityDecr(View view) {
        float intensityScale = Math.max(m_heatmap.getIntensityScale() - 0.1f, m_minIntensityScale);
        m_heatmap.setIntensityScale(intensityScale);
    }

    public void onClickOpacityIncr(View view) {
        float opacity = Math.min(m_heatmap.getOpacity() + 0.1f, 1.0f);
        m_heatmap.setOpacity(opacity);
    }

    public void onClickOpacityDecr(View view) {
        float opacity = Math.max(m_heatmap.getOpacity() - 0.1f, 0.0f);
        m_heatmap.setOpacity(opacity);
    }

    public void onClickDataCycle(View view) {
        m_currentDataIndex++;
        if (m_currentDataIndex >= m_dataSets.size()) {
            m_currentDataIndex = 0;
        }

        DataSet dataSet = m_dataSets.get(m_currentDataIndex);
        m_heatmap.setData(
            Arrays.asList(dataSet.WeightedPoints), dataSet.WeightMin, dataSet.WeightMax
        );
    }

    public void onClickGradientCycle(View view) {
        m_currentGradientIndex++;
        if (m_currentGradientIndex >= m_gradients.size()) {
            m_currentGradientIndex = 0;
        }

        m_heatmap.setGradient(
            m_gradients.get(m_currentGradientIndex).Colors,
            m_gradients.get(m_currentGradientIndex).StartParams
        );

        m_heatmap.setIntensityBias(m_gradients.get(m_currentGradientIndex).IntensityBias);
    }

    public void onClickOcclusionToggle(View view) {
        m_occlusionEnabled = !m_occlusionEnabled;
        final int occludedFeatures = m_occlusionEnabled ? m_occlusionFlags : HeatmapOptions.OCCLUSION_NONE;
        m_heatmap.setOccludedFeatures(occludedFeatures);
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
