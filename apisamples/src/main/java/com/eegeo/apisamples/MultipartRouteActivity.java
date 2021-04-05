package com.eegeo.apisamples;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.eegeo.indoors.IndoorMapView;
import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.geometry.LatLng;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.services.routing.OnRoutingQueryCompletedListener;
import com.eegeo.mapapi.services.routing.RoutingQuery;
import com.eegeo.mapapi.services.routing.RoutingQueryOptions;
import com.eegeo.mapapi.services.routing.RoutingQueryResponse;
import com.eegeo.mapapi.services.routing.RoutingService;


public class MultipartRouteActivity extends WrldExampleActivity implements OnRoutingQueryCompletedListener {

    private MapView m_mapView;
    private IndoorMapView m_indoorMapView = null;
    private EegeoMap m_eegeoMap = null;

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
            }
        });
    }

    @Override
    public void onRoutingQueryCompleted(RoutingQuery query, RoutingQueryResponse response) {
        if(response.succeeded()) {
            Toast.makeText(MultipartRouteActivity.this, "Found routes", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(MultipartRouteActivity.this, "Failed to find routes", Toast.LENGTH_LONG).show();
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
