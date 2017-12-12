package com.eegeo.apisamples;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;
import java.util.ArrayList;

import com.eegeo.indoors.IndoorMapView;
import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.map.OnMapReadyCallback;

import com.eegeo.mapapi.geometry.LatLng;
import com.eegeo.mapapi.services.routing.OnRoutingQueryCompletedListener;
import com.eegeo.mapapi.services.routing.RoutingService;
import com.eegeo.mapapi.services.routing.RoutingQuery;
import com.eegeo.mapapi.services.routing.RoutingQueryOptions;
import com.eegeo.mapapi.services.routing.RoutingQueryResponse;
import com.eegeo.mapapi.services.routing.Route;
import com.eegeo.mapapi.widgets.RouteView;
import com.eegeo.mapapi.widgets.RouteViewOptions;


public class RouteViewStyleActivity extends SoftBackButtonActivity implements OnRoutingQueryCompletedListener {

    private static int BLUE = Color.argb(255, 0,113,188);
    private static int WHITE = Color.argb(128, 255, 255, 255);
    private static float THICK = 20.0f;
    private static float THIN = 10.0f;

    private MapView m_mapView;
    private IndoorMapView m_indoorMapView = null;
    private EegeoMap m_eegeoMap = null;
    private Handler m_timerHandler = new Handler();
    private List<RouteView> m_routeViews = new ArrayList<RouteView>();
    private boolean m_styleToggle = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EegeoApi.init(this, getString(R.string.eegeo_api_key));
        setContentView(R.layout.routing_indoors_example_activity);

        m_mapView = (MapView) findViewById(R.id.routing_example_mapview);
        m_mapView.onCreate(savedInstanceState);

        final OnRoutingQueryCompletedListener listener = this;

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;

                RelativeLayout uiContainer = (RelativeLayout) findViewById(R.id.eegeo_ui_container);
                m_indoorMapView = new IndoorMapView(m_mapView, uiContainer, m_eegeoMap);

                RoutingService routingService = map.createRoutingService();

                routingService.findRoutes(new RoutingQueryOptions()
                        .addIndoorWaypoint(new LatLng(56.461231653264029, -2.983122836389253), 2)
                        .addIndoorWaypoint(new LatLng(56.4600344, -2.9783117), 2)
                        .onRoutingQueryCompletedListener(listener));

                m_timerHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (m_eegeoMap != null) {
                            m_styleToggle = !m_styleToggle;
                            int color = (m_styleToggle) ? WHITE : BLUE;
                            float width = (m_styleToggle) ? THIN : THICK;

                            for (RouteView routeView: m_routeViews) {
                                routeView.setColor(color);
                                routeView.setWidth(width);
                            }

                            m_timerHandler.postDelayed(this, 2000);
                        }
                    }
                }, 2000);
            }
        });
    }

    @Override
    public void onRoutingQueryCompleted(RoutingQuery query, RoutingQueryResponse response) {
        Toast.makeText(RouteViewStyleActivity.this, "Found routes", Toast.LENGTH_LONG).show();

        for (Route route: response.getResults()) {
            RouteViewOptions options = new RouteViewOptions()
                .color(Color.argb(128, 255, 0, 0))
                .width(8.0f);
            RouteView routeView = new RouteView(m_eegeoMap, route, options);
            m_routeViews.add(routeView);
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

        if (m_eegeoMap != null) {
            for (RouteView routeView: m_routeViews) {
                routeView.removeFromMap();
            }
        }

        m_mapView.onDestroy();
    }
}

