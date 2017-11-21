package com.eegeo.apisamples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.map.OnMapReadyCallback;

import com.eegeo.mapapi.geometry.LatLng;
import com.eegeo.mapapi.services.routing.OnRoutingQueryCompletedListener;
import com.eegeo.mapapi.services.routing.RoutingService;
import com.eegeo.mapapi.services.routing.RoutingQueryOptions;
import com.eegeo.mapapi.services.routing.RoutingQueryResponse;
import com.eegeo.mapapi.services.routing.RoutingQuery;


public class CancelRoutingQueryActivity extends SoftBackButtonActivity implements OnRoutingQueryCompletedListener {

    private MapView m_mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EegeoApi.init(this, getString(R.string.eegeo_api_key));
        setContentView(R.layout.basic_map_activity);

        m_mapView = (MapView) findViewById(R.id.basic_mapview);
        m_mapView.onCreate(savedInstanceState);

        final OnRoutingQueryCompletedListener listener = this;

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                RoutingService routingService = map.createRoutingService();

                RoutingQuery query = routingService.findRoutes(new RoutingQueryOptions()
                        .addWaypoint(new LatLng(56.460918, -2.981560))
                        .addWaypoint(new LatLng(56.461207, -2.977993))
                        .onRoutingQueryCompletedListener(listener));

                Toast.makeText(CancelRoutingQueryActivity.this, "Cancelling query", Toast.LENGTH_LONG).show();
                query.cancel();
            }
        });
    }

    @Override
    public void onRoutingQueryCompleted(RoutingQueryResponse response) {
        Toast.makeText(CancelRoutingQueryActivity.this, "This toast should never show!", Toast.LENGTH_LONG).show();
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
