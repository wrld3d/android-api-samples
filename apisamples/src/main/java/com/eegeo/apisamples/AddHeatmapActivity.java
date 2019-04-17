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
        final float[] Stops;
        final int[] Colors;
        final float IntensityBias;

        Gradient(
            float[] stops,
            int[] colors,
            float intensityBias
        )
        {
            Stops = stops;
            Colors = colors;
            IntensityBias = intensityBias;
        }
    }

    class HeatmapDensitySet
    {
        final float[] Stops;
        final double[] Radii;
        final double[] Densities;

        HeatmapDensitySet(
            float[] stops,
            double[] radii,
            double[] densities

        )
        {
            Stops = stops;
            Radii = radii;
            Densities = densities;
        }

    }

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;
    private Heatmap m_heatmap = null;
    private float m_minIntensityScalePower = -12.f;
    private float m_maxIntensityScalePower = 12.f;
    private float m_intensityScalePower = 0.f;
    private boolean m_occlusionEnabled = false;
    private int m_occlusionFlags = HeatmapOptions.OCCLUSION_BUILDINGS | HeatmapOptions.OCCLUSION_TREES;
    private List<DataSet> m_dataSets = new ArrayList<>();
    private List<Gradient> m_gradients = new ArrayList<>();

    private int m_currentDataIndex = 0;
    private int m_currentGradientIndex = 0;
    private int m_currentResolutionIndex = 4;
    private int[] m_resolutions = { 32, 64, 128, 256, 512, 1024 };

    private boolean m_useApproximation = true;

    private int m_currentRadiusIndex = 0;
    private List<HeatmapDensitySet> m_heatmapDensitySets = new ArrayList<>();

    private TextView m_intensityScaleLabel;
    private Button m_useApproximationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setContentView(R.layout.add_heatmap_activity);
        m_mapView = (MapView) findViewById(R.id.add_heatmap_mapview);
        m_mapView.onCreate(savedInstanceState);


        m_intensityScaleLabel = (TextView)findViewById(R.id.intensity_scale_label);
        m_useApproximationButton = (Button) findViewById(R.id.approximation_toggle_button);

        incrementIntensityScalePower(0.f);


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
                    generateRandomDataSequential(1000, sw, ne,0.0,1.0),
                    0.0,
                    3.0
                ));

                m_dataSets.add(new DataSet(
                    generateRandomDataSequential(1000, sw, ne,1.0,100.0),
                    0.0,
                    1000.0
                ));

                m_dataSets.add(new DataSet(
                        generateRandomDataDiverging(1000, sw, ne,-1.0,0.0, 1.0),
                        -1.0,
                        1.0
                ));

                m_dataSets.add(new DataSet(
                        generateRandomDataFromSet(1000, sw, ne,new double[]{-1.0, 1.0}),
                        -1.0,
                        1.0
                ));

                // > 8 bit dynamic range
                m_dataSets.add(new DataSet(
                        generateRandomDataFromSet(1000, sw, ne,new double[]{0.0001,0.001,0.1,1.0,10.0}),
                        0.0,
                        1.0
                ));

                // dense, false origin to visualise peaks
                m_dataSets.add(new DataSet(
                        generateRandomDataSequential(500000, sw, ne,0.0,1.0),
                        2.0,
                        50.0
                ));

                // Suitable for sequential data, with transparency near zero, similar to
                // http://colorbrewer2.org/#type=sequential&scheme=GnBu&n=5
                m_gradients.add(new Gradient(
                    new float[]{0.f, 0.2f, 0.4f, 0.6f, 0.8f, 1.f},
                    new int[]{0xffffff00, 0xf0f9e8ff,0xbae4bcff,0x7bccc4ff,0x43a2caff,0x0868acff},
                    0.0f
                ));

                // Suitable for sequential data, with transparency near zero, similar to
                // http://colorbrewer2.org/#type=sequential&scheme=YlOrRd&n=5
                m_gradients.add(new Gradient(
                    new float[]{0.f, 0.2f, 0.4f, 0.6f, 0.8f, 1.f},
                    new int[]{0xffffff00,0xffffb2ff,0xfecc5cff,0xfd8d3cff,0xf03b20ff,0xbd0026ff},
                    0.0f
                ));

                // Suitable for diverging data, with transparency around center, similar to
                // http://colorbrewer2.org/#type=diverging&scheme=RdYlBu&n=6
                m_gradients.add(new Gradient(
                    new float[]{0.f, 0.2f, 0.4f, 0.5f, 0.6f, 0.8f, 1.f},
                    new int[]{0xd73027ff,0xfc8d59ff,0xfee090ff,0xffffff00,0xe0f3f8ff,0x91bfdbff,0x4575b4ff},
                    0.5f
                ));

                m_gradients.add(new Gradient(
                        new float[]{0.f, 1.f},
                        new int[]{0x000000ff,0xffffffff},
                        0.0f
                ));

                m_gradients.add(new Gradient(
                        new float[]{0.f, 0.5f, 1.f},
                        new int[]{0x000000ff,0xffffffff,0x000000ff},
                        0.5f
                ));

                m_gradients.add(new Gradient(
                        new float[]{0.f, 0.5f, 1.f},
                        new int[]{0x0000ffff,0x000000ff,0xff0000ff},
                        0.5f
                ));

                m_heatmapDensitySets.add(new HeatmapDensitySet(
                        new float[]{0.f, 1.0f},
                        new double[]{ 5.0, 25.0 },
                        new double[]{ 1.0, 1.0 }
                ));

                m_heatmapDensitySets.add(new HeatmapDensitySet(
                    new float[]{0.f, 0.25f, 0.5f, 0.75f, 1.0f},
                    new double[]{ 3.0, 5.0, 8.0, 13.0, 21.f },
                    new double[]{ 1.0, 1.0, 1.0, 1.0, 1.f }
                ));

                m_heatmap = m_eegeoMap.addHeatmap(
                    new HeatmapOptions()
                        //.polygon(polygonOptions)
                        .resolution(getResolution())
                        .add(m_dataSets.get(m_currentDataIndex).WeightedPoints)
                        .weightMin(m_dataSets.get(m_currentDataIndex).WeightMin)
                        .weightMax(m_dataSets.get(m_currentDataIndex).WeightMax)
                        .setDensityStops(getDensitySet().Stops, getDensitySet().Radii, getDensitySet().Densities)
//                            .heatmapRadius(20.0)
//                        .addDensityStop(0.75f, 13.0, 1.0)
//                        .addDensityStop(0.0f, 3.0, 1.0)
//                        .addDensityStop(0.25f, 5.0, 1.0)
//                        .addDensityStop(0.5f, 8.0, 1.0)
//                        .addDensityStop(1.0f, 21.0, 1.0)
                        .densityBlend(0.0f)
                        .opacity(1.0f)
                            //.textureBorder(0.3f)
                        .occludedFeatures(getOccludedFeatures())
                        .gradient(m_gradients.get(m_currentGradientIndex).Stops, m_gradients.get(m_currentGradientIndex).Colors)
                        .intensityBias(m_gradients.get(m_currentGradientIndex).IntensityBias)
                        .intensityScale(getIntensityScale())
                );
            }
        });
    }

    private int getResolution() {
        return m_resolutions[m_currentResolutionIndex];
    }

    private HeatmapDensitySet getDensitySet() {
        return m_heatmapDensitySets.get(m_currentRadiusIndex);
    }

    private float getIntensityScale() {
        return (float)Math.pow(2.0, m_intensityScalePower);
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

    private WeightedLatLngAlt[] generateRandomDataFromSet(int count, LatLng sw, LatLng ne, double[] weights) {
        Random random = new Random(1);
        ArrayList<WeightedLatLngAlt> points = new ArrayList<>();

        for (int i = 0; i < count; ++i) {

            double lat = random.nextDouble() * (ne.latitude - sw.latitude) + sw.latitude;
            double lng = random.nextDouble() * (ne.longitude - sw.longitude) + sw.longitude;
            int index = random.nextInt(weights.length);
            points.add(new WeightedLatLngAlt(lat, lng, weights[index]));
        }

        return points.toArray(new WeightedLatLngAlt[0]);

    }


    public void onClickDensityBlendIncr(View view) {
        float densityBlend = Math.min(m_heatmap.getDensityBlend() + 0.05f, 1.0f);
        m_heatmap.setDensityBlend(densityBlend);
    }

    public void onClickDensityBlendDecr(View view) {
        float densityBlend = Math.max(m_heatmap.getDensityBlend() - 0.05f, 0.0f);
        m_heatmap.setDensityBlend(densityBlend);
    }

    public void incrementIntensityScalePower(float powerDelta) {
        m_intensityScalePower = Math.max(Math.min(m_intensityScalePower + powerDelta, m_maxIntensityScalePower), m_minIntensityScalePower);
        String text = String.format("x%.3f", getIntensityScale());
        m_intensityScaleLabel.setText(text);
    }

    public void onClickIntensityIncr(View view) {
        incrementIntensityScalePower(0.25f);
        m_heatmap.setIntensityScale(getIntensityScale());
    }

    public void onClickIntensityDecr(View view) {
        incrementIntensityScalePower(-0.25f);
        m_heatmap.setIntensityScale(getIntensityScale());
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
            m_gradients.get(m_currentGradientIndex).Stops
        );

        m_heatmap.setIntensityBias(m_gradients.get(m_currentGradientIndex).IntensityBias);
    }

    private int getOccludedFeatures() {
        return m_occlusionEnabled ? m_occlusionFlags : HeatmapOptions.OCCLUSION_NONE;
    }
    public void onClickOcclusionToggle(View view) {
        m_occlusionEnabled = !m_occlusionEnabled;
        m_heatmap.setOccludedFeatures(getOccludedFeatures());
    }

    public void onClickApproximationToggle(View view) {
        m_useApproximation = !m_useApproximation;
        m_heatmap.setUseApproximation(m_useApproximation);

        m_useApproximationButton.setText(m_useApproximation ? "Approx" : "Exact");

    }

    public void onClickResolutionCycleDown(View view) {
        m_currentResolutionIndex--;
        if (m_currentResolutionIndex < 0) {
            m_currentResolutionIndex = m_resolutions.length - 1;
        }
        m_heatmap.setResolution(getResolution());
    }

    public void onClickResolutionCycleUp(View view) {
        m_currentResolutionIndex++;
        if (m_currentResolutionIndex >= m_resolutions.length) {
            m_currentResolutionIndex = 0;
        }
        m_heatmap.setResolution(getResolution());
    }

    public void onClickRadiusCycleDown(View view) {
        m_currentRadiusIndex--;
        if (m_currentRadiusIndex < 0) {
            m_currentRadiusIndex = m_heatmapDensitySets.size() - 1;
        }

        m_heatmap.setHeatmapDensities(getDensitySet().Stops, getDensitySet().Radii, getDensitySet().Densities);
    }

    public void onClickRadiusCycleUp(View view) {
        m_currentRadiusIndex++;
        if (m_currentRadiusIndex >= m_heatmapDensitySets.size()) {
            m_currentRadiusIndex = 0;
        }

        m_heatmap.setHeatmapDensities(getDensitySet().Stops, getDensitySet().Radii, getDensitySet().Densities);
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
