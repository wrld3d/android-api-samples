package com.eegeo.apisamples;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.annotation.UiThread;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.buildings.BuildingHighlight;
import com.eegeo.mapapi.buildings.BuildingHighlightOptions;
import com.eegeo.mapapi.geometry.MapFeatureType;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.picking.PickResult;
import com.eegeo.mapapi.util.Ready;

@RequiresApi(api = Build.VERSION_CODES.M)
public class PickingBuildingsActivity extends SoftBackButtonActivity {

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;
    private Handler m_timerHandler = new Handler();
    private GestureDetectorCompat m_detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setContentView(R.layout.picking_buildings_activity);
        m_mapView = (MapView) findViewById(R.id.picking_buildings_mapview);
        m_mapView.onCreate(savedInstanceState);

        m_detector = new GestureDetectorCompat(this, new TouchTapListener());

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;
            }
        });

        m_mapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                m_detector.onTouchEvent(event);
                return false;
            }
        });

    }


    private class TouchTapListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            if (m_eegeoMap == null) {
                return false;
            }
            final Point screenPoint = new Point((int) event.getX(), (int) event.getY());
            m_eegeoMap.pickFeatureAtScreenPoint(screenPoint)
                    .then(new Ready<PickResult>() {
                        @UiThread
                        @Override
                        public void ready(PickResult pickResult) {
                            Toast.makeText(PickingBuildingsActivity.this, String.format("Picked map feature: %s", pickResult.mapFeatureType.name()), Toast.LENGTH_SHORT).show();

                            if (pickResult.mapFeatureType == MapFeatureType.Building) {

                                final BuildingHighlight highlight = m_eegeoMap.addBuildingHighlight(new BuildingHighlightOptions()
                                        .highlightBuildingAtScreenPoint(screenPoint)
                                        .color(ColorUtils.setAlphaComponent(Color.YELLOW, 128))
                                );

                                m_timerHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        m_eegeoMap.removeBuildingHighlight(highlight);
                                    }
                                }, 3000);
                            }
                        }
                    });

            return false;
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

}
