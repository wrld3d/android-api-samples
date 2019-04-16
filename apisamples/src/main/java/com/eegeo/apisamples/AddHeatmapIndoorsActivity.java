package com.eegeo.apisamples;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.graphics.ColorUtils;
import android.util.Log;
import android.widget.RelativeLayout;

import com.eegeo.indoors.IndoorMapView;
import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.camera.CameraPosition;
import com.eegeo.mapapi.camera.CameraUpdateFactory;
import com.eegeo.mapapi.geometry.LatLng;
import com.eegeo.mapapi.geometry.WeightedLatLngAlt;
import com.eegeo.mapapi.map.OnInitialStreamingCompleteListener;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.heatmaps.Heatmap;
import com.eegeo.mapapi.heatmaps.HeatmapOptions;
import com.eegeo.mapapi.polygons.PolygonOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AddHeatmapIndoorsActivity extends WrldExampleActivity {

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;
    private IndoorMapView m_indoorMapView = null;
    private List<Heatmap> m_heatmaps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setLockedOrientation();

        setContentView(R.layout.add_heatmap_indoors_activity);
        m_mapView = (MapView) findViewById(R.id.add_heatmap_indoors_mapview);
        m_mapView.onCreate(savedInstanceState);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;

                RelativeLayout uiContainer = (RelativeLayout) findViewById(R.id.eegeo_ui_container);
                m_indoorMapView = new IndoorMapView(m_mapView, uiContainer, m_eegeoMap);

                map.addInitialStreamingCompleteListener(new OnInitialStreamingCompleteListener() {
                    @Override
                    public void onInitialStreamingComplete() {
                        CameraPosition position = new CameraPosition.Builder()
                                .target(56.459801, -2.977928)
                                .indoor("westport_house", 2)
                                .zoom(18)
                                .build();
                        map.moveCamera(CameraUpdateFactory.newCameraPosition(position));
                    }
                });
                map.addOnCameraMoveListener(new EegeoMap.OnCameraMoveListener() {
                    @Override
                    public void onCameraMove() {
                        final double zoom = map.getCameraPosition().zoom;
                        final double zoomMax = 21.0;
                        final double zoomMin = 18.5;

                        double t = (zoom - zoomMin) / (zoomMax - zoomMin);
                        t = Math.min(Math.max(t, 0.0), 1.0);

                        for (Heatmap heatmap : m_heatmaps) {
                            heatmap.setRadiusBlend((float)(1.0 - t));
                        }

                    }
                });


                Heatmap heatmap = m_eegeoMap.addHeatmap(
                        new HeatmapOptions()
                                .polygon(new PolygonOptions().indoor("westport_house", 2))
                                .add(getExampleOccupancyPerDayData())
                                .textureBorder(0.2f)
                                .weightMin(-4.0)
                                .weightMax(12.0)
                                // aligns normative value with mid-point of color gradient
                                .intensityBias(0.5f)
                                .addHeatmapRadius(0.6, 0.0f)
                                .addHeatmapRadius(1.3, 0.5f)
                                .addHeatmapRadius(2.1, 1.0f)
                                .gradient(
                                        new int[]{0x4575b4ff,0x91bfdbff,0xe0f3f8ff,0xffffff00,0xfee090ff,0xfc8d59ff,0xd73027ff},
                                        new float[]{0.f, 0.1f, 0.4f, 0.5f, 0.6f, 0.9f, 1.f})
                                .opacity(0.8f)
                );
                m_heatmaps.add(heatmap);
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
            for (Heatmap heatmap : m_heatmaps) {
                m_eegeoMap.removeHeatmap(heatmap);
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

    private WeightedLatLngAlt[] getExampleOccupancyPerDayData() {
        WeightedLatLngAlt[] points = new WeightedLatLngAlt[] {
                new WeightedLatLngAlt(56.460035, -2.978334, 7.78),
                new WeightedLatLngAlt(56.459999, -2.978309, 0.0),
                new WeightedLatLngAlt(56.460003, -2.978280, 8.2),
                new WeightedLatLngAlt(56.459990, -2.978305, 7.91),
                new WeightedLatLngAlt(56.459994, -2.978276, 7.36),
                new WeightedLatLngAlt(56.459960, -2.978294, 0.0),
                new WeightedLatLngAlt(56.459963, -2.978265, 7.89),
                new WeightedLatLngAlt(56.459951, -2.978290, 7.57),
                new WeightedLatLngAlt(56.459954, -2.978261, 7.84),
                new WeightedLatLngAlt(56.459918, -2.978280, 8.01),
                new WeightedLatLngAlt(56.459921, -2.978250, 7.72),
                new WeightedLatLngAlt(56.459909, -2.978276, 0.0),
                new WeightedLatLngAlt(56.459912, -2.978246, 0.0),
                new WeightedLatLngAlt(56.459883, -2.978266, 7.42),
                new WeightedLatLngAlt(56.459886, -2.978237, 7.9),
                new WeightedLatLngAlt(56.459874, -2.978262, 0.0),
                new WeightedLatLngAlt(56.459877, -2.978233, 0.0),
                new WeightedLatLngAlt(56.459851, -2.978255, 7.68),
                new WeightedLatLngAlt(56.459855, -2.978225, 0.0),
                new WeightedLatLngAlt(56.459843, -2.978250, 0.0),
                new WeightedLatLngAlt(56.459846, -2.978221, 0.0),
                new WeightedLatLngAlt(56.459849, -2.978172, 0.0),
                new WeightedLatLngAlt(56.459858, -2.978176, 0.0),
                new WeightedLatLngAlt(56.459897, -2.978170, 9.56),
                new WeightedLatLngAlt(56.459906, -2.978174, 0.0),
                new WeightedLatLngAlt(56.459909, -2.978145, 7.52),
                new WeightedLatLngAlt(56.459932, -2.978164, 7.52),
                new WeightedLatLngAlt(56.459941, -2.978168, 0.99),
                new WeightedLatLngAlt(56.459935, -2.978135, 7.49),
                new WeightedLatLngAlt(56.459944, -2.978139, 7.71),
                new WeightedLatLngAlt(56.459944, -2.978043, 7.9),
                new WeightedLatLngAlt(56.459961, -2.978196, 0.73),
                new WeightedLatLngAlt(56.459983, -2.978059, 7.84),
                new WeightedLatLngAlt(56.459970, -2.978200, 0.07),
                new WeightedLatLngAlt(56.460001, -2.978211, 2.97),
                new WeightedLatLngAlt(56.459900, -2.978141, 7.66),
                new WeightedLatLngAlt(56.460010, -2.978215, 7.79),
                new WeightedLatLngAlt(56.460008, -2.978190, 7.80),
                new WeightedLatLngAlt(56.459981, -2.978076, 7.55),
                new WeightedLatLngAlt(56.460195, -2.978007, 7.21),
                new WeightedLatLngAlt(56.460197, -2.977982, 0.44),
                new WeightedLatLngAlt(56.460206, -2.977988, 7.23),
                new WeightedLatLngAlt(56.460203, -2.978013, 7.81),
                new WeightedLatLngAlt(56.460186, -2.978083, 7.07),
                new WeightedLatLngAlt(56.460195, -2.978090, 8.74),
                new WeightedLatLngAlt(56.460189, -2.978058, 7.22),
                new WeightedLatLngAlt(56.460198, -2.978064, 7.39),
                new WeightedLatLngAlt(56.460180, -2.978134, 7.18),
                new WeightedLatLngAlt(56.460189, -2.978140, 0.0),
                new WeightedLatLngAlt(56.460183, -2.978109, 7.72),
                new WeightedLatLngAlt(56.460192, -2.978115, 7.16),
                new WeightedLatLngAlt(56.460224, -2.978018, 7.62),
                new WeightedLatLngAlt(56.460227, -2.977993, 7.61),
                new WeightedLatLngAlt(56.460236, -2.977999, 7.03),
                new WeightedLatLngAlt(56.460233, -2.978025, 7.34),
                new WeightedLatLngAlt(56.460216, -2.978095, 7.9),
                new WeightedLatLngAlt(56.460225, -2.978101, 0.15),
                new WeightedLatLngAlt(56.460219, -2.978069, 8.14),
                new WeightedLatLngAlt(56.460227, -2.978075, 7.93),
                new WeightedLatLngAlt(56.460210, -2.978145, 8.57),
                new WeightedLatLngAlt(56.460219, -2.978151, 7.61),
                new WeightedLatLngAlt(56.460213, -2.978120, 7.02),
                new WeightedLatLngAlt(56.460222, -2.978126, 7.91),
                new WeightedLatLngAlt(56.460254, -2.978029, 7.34),
                new WeightedLatLngAlt(56.460257, -2.978004, 7.03),
                new WeightedLatLngAlt(56.460266, -2.978010, 7.52),
                new WeightedLatLngAlt(56.460263, -2.978035, 7.19),
                new WeightedLatLngAlt(56.460245, -2.978106, 7.81),
                new WeightedLatLngAlt(56.460254, -2.978112, 7.85),
                new WeightedLatLngAlt(56.460248, -2.978080, 6.17),
                new WeightedLatLngAlt(56.460257, -2.978086, 7.68),
                new WeightedLatLngAlt(56.460240, -2.978156, 8.47),
                new WeightedLatLngAlt(56.460248, -2.978162, 7.44),
                new WeightedLatLngAlt(56.460242, -2.978131, 9.75),
                new WeightedLatLngAlt(56.460251, -2.978137, 7.99),
                new WeightedLatLngAlt(56.460284, -2.978040, 7.31),
                new WeightedLatLngAlt(56.460286, -2.978015, 7.12),
                new WeightedLatLngAlt(56.460295, -2.978021, 7.13),
                new WeightedLatLngAlt(56.460292, -2.978046, 7.1),
                new WeightedLatLngAlt(56.460275, -2.978117, 0.0),
                new WeightedLatLngAlt(56.460284, -2.978123, 7.06),
                new WeightedLatLngAlt(56.460278, -2.978091, 7.23),
                new WeightedLatLngAlt(56.460287, -2.978097, 1.06),
                new WeightedLatLngAlt(56.460269, -2.978167, 9.02),
                new WeightedLatLngAlt(56.460278, -2.978173, 7.01),
                new WeightedLatLngAlt(56.460272, -2.978142, 7.37),
                new WeightedLatLngAlt(56.460281, -2.978148, 7.4),
                new WeightedLatLngAlt(56.460313, -2.978051, 0.0),
                new WeightedLatLngAlt(56.460316, -2.978026, 7.99),
                new WeightedLatLngAlt(56.460325, -2.978032, 7.03),
                new WeightedLatLngAlt(56.460322, -2.978057, 7.98),
                new WeightedLatLngAlt(56.460305, -2.978127, 7.69),
                new WeightedLatLngAlt(56.460313, -2.978134, 7.38),
                new WeightedLatLngAlt(56.460307, -2.978102, 0.76),
                new WeightedLatLngAlt(56.460316, -2.978108, 7.32),
                new WeightedLatLngAlt(56.460299, -2.978178, 0.0),
                new WeightedLatLngAlt(56.460308, -2.978184, 7.95),
                new WeightedLatLngAlt(56.460302, -2.978153, 0.0),
                new WeightedLatLngAlt(56.460311, -2.978159, 7.63),
                new WeightedLatLngAlt(56.460303, -2.978253, 7.97),
                new WeightedLatLngAlt(56.460312, -2.978259, 7.97),
                new WeightedLatLngAlt(56.460306, -2.978227, 7.59),
                new WeightedLatLngAlt(56.460315, -2.978233, 7.13),
                new WeightedLatLngAlt(56.460274, -2.978242, 7.84),
                new WeightedLatLngAlt(56.460282, -2.978248, 7.18),
                new WeightedLatLngAlt(56.460276, -2.978216, 7.9),
                new WeightedLatLngAlt(56.460285, -2.978222, 7.74),
                new WeightedLatLngAlt(56.460194, -2.978269, 7.15),
                new WeightedLatLngAlt(56.460206, -2.978250, 7.18),
                new WeightedLatLngAlt(56.460203, -2.978275, 7.51),
                new WeightedLatLngAlt(56.460197, -2.978244, 7.07),
                new WeightedLatLngAlt(56.460200, -2.978218, 0.09),
                new WeightedLatLngAlt(56.460208, -2.978224, 0.0),
                new WeightedLatLngAlt(56.460162, -2.978257, 7.85),
                new WeightedLatLngAlt(56.460174, -2.978238, 7.23),
                new WeightedLatLngAlt(56.460171, -2.978263, 5.65),
                new WeightedLatLngAlt(56.460165, -2.978232, 7.99),
                new WeightedLatLngAlt(56.460168, -2.978207, 7.01),
                new WeightedLatLngAlt(56.460177, -2.978213, 7.95),
                new WeightedLatLngAlt(56.460270, -2.978498, 7.82),
                new WeightedLatLngAlt(56.460262, -2.978491, 0.0),
                new WeightedLatLngAlt(56.460268, -2.978523, 7.76),
                new WeightedLatLngAlt(56.460259, -2.978517, 7.91),
                new WeightedLatLngAlt(56.460267, -2.978441, 7.02),
                new WeightedLatLngAlt(56.460273, -2.978472, 7.8),
                new WeightedLatLngAlt(56.460265, -2.978466, 7.54),
                new WeightedLatLngAlt(56.460276, -2.978447, 7.42),
                new WeightedLatLngAlt(56.460276, -2.978365, 7.03),
                new WeightedLatLngAlt(56.460282, -2.978396, 7.99),
                new WeightedLatLngAlt(56.460273, -2.978390, 7.43),
                new WeightedLatLngAlt(56.460285, -2.978371, 8.1),
                new WeightedLatLngAlt(56.460241, -2.978487, 7.01),
                new WeightedLatLngAlt(56.460232, -2.978481, 4.66),
                new WeightedLatLngAlt(56.460238, -2.978512, 7.23),
                new WeightedLatLngAlt(56.460229, -2.978506, 7.66),
                new WeightedLatLngAlt(56.460238, -2.978430, 7.37),
                new WeightedLatLngAlt(56.460244, -2.978461, 8.33),
                new WeightedLatLngAlt(56.460235, -2.978455, 7.75),
                new WeightedLatLngAlt(56.460246, -2.978436, 7.12),
                new WeightedLatLngAlt(56.460246, -2.978354, 7.53),
                new WeightedLatLngAlt(56.460252, -2.978385, 7.44),
                new WeightedLatLngAlt(56.460243, -2.978379, 3.01),
                new WeightedLatLngAlt(56.460255, -2.978360, 7.81),
                new WeightedLatLngAlt(56.460213, -2.978368, 7.76),
                new WeightedLatLngAlt(56.460217, -2.978425, 8.17),
                new WeightedLatLngAlt(56.460205, -2.978444, 7.58),
                new WeightedLatLngAlt(56.460214, -2.978450, 7.03),
                new WeightedLatLngAlt(56.460208, -2.978419, 7.67),
                new WeightedLatLngAlt(56.460199, -2.978495, 7.68),
                new WeightedLatLngAlt(56.460208, -2.978501, 7.72),
                new WeightedLatLngAlt(56.460202, -2.978470, 7.11),
                new WeightedLatLngAlt(56.460211, -2.978476, 3.98),
                new WeightedLatLngAlt(56.460222, -2.978374, 7.58),
                new WeightedLatLngAlt(56.460184, -2.978357, 7.96),
                new WeightedLatLngAlt(56.460187, -2.978414, 7.03),
                new WeightedLatLngAlt(56.460176, -2.978434, 0.0),
                new WeightedLatLngAlt(56.460184, -2.978440, 7.01),
                new WeightedLatLngAlt(56.460178, -2.978408, 0.0),
                new WeightedLatLngAlt(56.460170, -2.978484, 7.08),
                new WeightedLatLngAlt(56.460179, -2.978490, 7.94),
                new WeightedLatLngAlt(56.460173, -2.978459, 7.69),
                new WeightedLatLngAlt(56.460182, -2.978465, 7.69),
                new WeightedLatLngAlt(56.460193, -2.978364, 7.94),
                new WeightedLatLngAlt(56.460152, -2.978454, 0.0),
                new WeightedLatLngAlt(56.460143, -2.978448, 7.02),
                new WeightedLatLngAlt(56.460149, -2.978479, 0.0),
                new WeightedLatLngAlt(56.460140, -2.978473, 0.0),
                new WeightedLatLngAlt(56.460149, -2.978397, 0.0),
                new WeightedLatLngAlt(56.460155, -2.978429, 0.0),
                new WeightedLatLngAlt(56.460146, -2.978423, 7.41),
                new WeightedLatLngAlt(56.460157, -2.978403, 7.73),
                new WeightedLatLngAlt(56.460157, -2.978321, 0.0),
                new WeightedLatLngAlt(56.460163, -2.978353, 7.6),
                new WeightedLatLngAlt(56.460154, -2.978347, 7.79),
                new WeightedLatLngAlt(56.460166, -2.978327, 7.77),
                new WeightedLatLngAlt(56.460108, -2.978438, 7.23),
                new WeightedLatLngAlt(56.460099, -2.978432, 7.36),
                new WeightedLatLngAlt(56.460105, -2.978463, 7.78),
                new WeightedLatLngAlt(56.460096, -2.978457, 3.05),
                new WeightedLatLngAlt(56.460105, -2.978381, 7.8),
                new WeightedLatLngAlt(56.460111, -2.978413, 7.90),
                new WeightedLatLngAlt(56.460102, -2.978407, 7.55),
                new WeightedLatLngAlt(56.460114, -2.978387, 7.33),
                new WeightedLatLngAlt(56.460113, -2.978305, 0.0),
                new WeightedLatLngAlt(56.460119, -2.978337, 7.27),
                new WeightedLatLngAlt(56.460111, -2.978331, 7.02),
                new WeightedLatLngAlt(56.460122, -2.978311, 7.22),
                new WeightedLatLngAlt(56.460008, -2.978435, 6.99),
                new WeightedLatLngAlt(56.459999, -2.978429, 7.64),
                new WeightedLatLngAlt(56.460005, -2.978378, 6.69),
                new WeightedLatLngAlt(56.460011, -2.978409, 7.48),
                new WeightedLatLngAlt(56.460002, -2.978403, 7.34),
                new WeightedLatLngAlt(56.460014, -2.978384, 0.0),
                new WeightedLatLngAlt(56.459990, -2.978508, 0.0),
                new WeightedLatLngAlt(56.459996, -2.978539, 0.0),
                new WeightedLatLngAlt(56.459987, -2.978533, 0.0),
                new WeightedLatLngAlt(56.459999, -2.978514, 0.0),
                new WeightedLatLngAlt(56.459978, -2.978424, 0.0),
                new WeightedLatLngAlt(56.459969, -2.978418, 0.0),
                new WeightedLatLngAlt(56.459975, -2.978367, 0.0),
                new WeightedLatLngAlt(56.459981, -2.978399, 0.0),
                new WeightedLatLngAlt(56.459972, -2.978393, 0.0),
                new WeightedLatLngAlt(56.459984, -2.978373, 0.0),
                new WeightedLatLngAlt(56.459961, -2.978497, 0.0),
                new WeightedLatLngAlt(56.459967, -2.978529, 0.0),
                new WeightedLatLngAlt(56.459958, -2.978522, 0.0),
                new WeightedLatLngAlt(56.459969, -2.978503, 0.0),
                new WeightedLatLngAlt(56.459975, -2.978449, 0.0),
                new WeightedLatLngAlt(56.459967, -2.978443, 0.0),
                new WeightedLatLngAlt(56.459948, -2.978413, 0.0),
                new WeightedLatLngAlt(56.459940, -2.978407, 0.0),
                new WeightedLatLngAlt(56.459945, -2.978356, 0.0),
                new WeightedLatLngAlt(56.459951, -2.978388, 0.0),
                new WeightedLatLngAlt(56.459942, -2.978382, 0.0),
                new WeightedLatLngAlt(56.459954, -2.978362, 0.0),
                new WeightedLatLngAlt(56.459931, -2.978486, 0.0),
                new WeightedLatLngAlt(56.459937, -2.978518, 0.0),
                new WeightedLatLngAlt(56.459928, -2.978512, 0.0),
                new WeightedLatLngAlt(56.459940, -2.978492, 0.0),
                new WeightedLatLngAlt(56.459946, -2.978438, 0.0),
                new WeightedLatLngAlt(56.459937, -2.978432, 0.0),
                new WeightedLatLngAlt(56.459910, -2.978481, 0.0),
                new WeightedLatLngAlt(56.459898, -2.978501, 0.0),
                new WeightedLatLngAlt(56.459907, -2.978507, 0.0),
                new WeightedLatLngAlt(56.459901, -2.978475, 0.0),
                new WeightedLatLngAlt(56.459924, -2.978352, 0.0),
                new WeightedLatLngAlt(56.459913, -2.978371, 0.0),
                new WeightedLatLngAlt(56.459922, -2.978377, 0.0),
                new WeightedLatLngAlt(56.459916, -2.978346, 0.0),
                new WeightedLatLngAlt(56.459910, -2.978396, 0.0),
                new WeightedLatLngAlt(56.459919, -2.978402, 0.0),
                new WeightedLatLngAlt(56.459871, -2.978464, 0.0),
                new WeightedLatLngAlt(56.459877, -2.978496, 0.0),
                new WeightedLatLngAlt(56.459868, -2.978490, 0.0),
                new WeightedLatLngAlt(56.459880, -2.978471, 0.0),
                new WeightedLatLngAlt(56.459841, -2.978456, 0.0),
                new WeightedLatLngAlt(56.459829, -2.978475, 0.0),
                new WeightedLatLngAlt(56.459838, -2.978481, 0.0),
                new WeightedLatLngAlt(56.459832, -2.978450, 0.0),
                new WeightedLatLngAlt(56.459826, -2.978501, 0.0),
                new WeightedLatLngAlt(56.459832, -2.978532, 0.0),
                new WeightedLatLngAlt(56.459823, -2.978526, 0.0),
                new WeightedLatLngAlt(56.459835, -2.978507, 0.0),
                new WeightedLatLngAlt(56.459908, -2.978120, 0.0),
                new WeightedLatLngAlt(56.459920, -2.978090, 0.0),
                new WeightedLatLngAlt(56.459915, -2.978068, 0.0),
                new WeightedLatLngAlt(56.459907, -2.978080, 0.0),
                new WeightedLatLngAlt(56.459969, -2.978175, 0.0)
        };

        return points;
    }

}
