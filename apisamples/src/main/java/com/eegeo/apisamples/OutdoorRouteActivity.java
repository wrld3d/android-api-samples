package com.eegeo.apisamples;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.graphics.ColorUtils;
import android.widget.Toast;

import java.util.List;
import java.util.ArrayList;

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
import com.eegeo.mapapi.services.routing.RouteSection;
import com.eegeo.mapapi.services.routing.RouteStep;
import com.eegeo.mapapi.polylines.Polyline;
import com.eegeo.mapapi.polylines.PolylineOptions;


public class OutdoorRouteActivity extends WrldExampleActivity implements OnRoutingQueryCompletedListener {

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;
    private List<Polyline> m_routeLines = new ArrayList<Polyline>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EegeoApi.init(this, getString(R.string.eegeo_api_key));
        setContentView(R.layout.routing_example_activity);

        m_mapView = (MapView) findViewById(R.id.routing_example_mapview);
        m_mapView.onCreate(savedInstanceState);

        final OnRoutingQueryCompletedListener listener = this;

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;

                RoutingService routingService = map.createRoutingService();

                routingService.findRoutes(new RoutingQueryOptions()
                        .addWaypoint(new LatLng(56.460918, -2.981560))
                        .addWaypoint(new LatLng(56.461207, -2.977993))
                        .onRoutingQueryCompletedListener(listener));
            }
        });
    }

    @Override
    public void onRoutingQueryCompleted(RoutingQuery query, RoutingQueryResponse response) {
        Toast.makeText(OutdoorRouteActivity.this, "Found routes", Toast.LENGTH_LONG).show();

        for (Route route: response.getResults()) {
            for (RouteSection section: route.sections) {
                for (RouteStep step: section.steps) {
                    if (step.path.size() < 2) {
                        continue;
                    }

                    PolylineOptions options = new PolylineOptions().color(ColorUtils.setAlphaComponent(Color.RED, 128));

                    for (LatLng point: step.path) {
                        options.add(point);
                    }

                    Polyline routeLine = m_eegeoMap.addPolyline(options);
                    m_routeLines.add(routeLine);
                }
            }
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
            for (Polyline polyline: m_routeLines) {
                m_eegeoMap.removePolyline(polyline);
            }
        }

        m_mapView.onDestroy();
    }

}
