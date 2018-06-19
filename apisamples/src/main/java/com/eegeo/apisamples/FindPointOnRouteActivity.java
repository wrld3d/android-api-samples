package com.eegeo.apisamples;

import android.graphics.Color;
import android.os.Bundle;
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
import com.eegeo.mapapi.markers.Marker;
import com.eegeo.mapapi.markers.MarkerOptions;
import com.eegeo.mapapi.paths.PointOnRoute;
import com.eegeo.mapapi.paths.PointOnRouteOptions;
import com.eegeo.mapapi.services.routing.OnRoutingQueryCompletedListener;
import com.eegeo.mapapi.services.routing.RoutingService;
import com.eegeo.mapapi.services.routing.RoutingQuery;
import com.eegeo.mapapi.services.routing.RoutingQueryOptions;
import com.eegeo.mapapi.services.routing.RoutingQueryResponse;
import com.eegeo.mapapi.services.routing.Route;
import com.eegeo.mapapi.services.routing.RouteSection;
import com.eegeo.mapapi.services.routing.RouteStep;
import com.eegeo.mapapi.util.Ready;
import com.eegeo.mapapi.widgets.RouteView;
import com.eegeo.mapapi.widgets.RouteViewOptions;


public class FindPointOnRouteActivity extends WrldExampleActivity implements OnRoutingQueryCompletedListener {

    private MapView m_mapView;
    private IndoorMapView m_indoorMapView = null;
    private EegeoMap m_eegeoMap = null;
    private List<RouteView> m_routeViews = new ArrayList<RouteView>();
    private List<Marker> m_markers = new ArrayList<Marker>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EegeoApi.init(this, getString(R.string.eegeo_api_key));
        setContentView(R.layout.find_point_on_path_activity);

        m_mapView = (MapView) findViewById(R.id.find_point_on_path_mapview);
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
            }
        });
    }

    @Override
    public void onRoutingQueryCompleted(RoutingQuery query, RoutingQueryResponse response) {
        Toast.makeText(FindPointOnRouteActivity.this, "Found routes", Toast.LENGTH_LONG).show();

        LatLng latLng = new LatLng(56.460369, -2.980330);
        MarkerOptions targetMarkerOptions = new MarkerOptions().position(latLng).labelText("Target Point");
        final Marker targetMarker = m_eegeoMap.addMarker(targetMarkerOptions);
        m_markers.add(targetMarker);

        for (Route route: response.getResults()) {
            RouteViewOptions options = new RouteViewOptions()
                    .color(Color.argb(128, 255, 0, 0))
                    .width(8.0f);
            RouteView routeView = new RouteView(m_eegeoMap, route, options);
            m_routeViews.add(routeView);

            m_eegeoMap.getPointOnRoute(latLng, route, new PointOnRouteOptions())
                    .then(new Ready<PointOnRoute>() {
                        @Override
                        public void ready(PointOnRoute pointOnRoute) {
                            if(pointOnRoute.validResult)
                            {
                                MarkerOptions resultMarkerOptions = new MarkerOptions().position(pointOnRoute.resultPoint).labelText("Result Point: "
                                        + "\n fractionAlongRoute: " + Double.toString(pointOnRoute.fractionAlongRoute));
                                Marker resultMarker = m_eegeoMap.addMarker(resultMarkerOptions);
                                m_markers.add(resultMarker);
                            }
                            else
                            {
                                targetMarker.setTitle("NO VALID RESULT");
                            }
                        }
                    });
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
            for (Marker marker: m_markers) {
                m_eegeoMap.removeMarker(marker);
            }
        }

        m_mapView.onDestroy();
    }
}

