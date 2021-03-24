package com.eegeo.apisamples;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.core.graphics.ColorUtils;

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
import com.eegeo.mapapi.paths.PointOnPath;
import com.eegeo.mapapi.polylines.Polyline;
import com.eegeo.mapapi.polylines.PolylineOptions;
import com.eegeo.mapapi.services.routing.OnRoutingQueryCompletedListener;
import com.eegeo.mapapi.services.routing.RoutingService;
import com.eegeo.mapapi.services.routing.RoutingQuery;
import com.eegeo.mapapi.services.routing.RoutingQueryOptions;
import com.eegeo.mapapi.services.routing.RoutingQueryResponse;
import com.eegeo.mapapi.util.Ready;
import com.eegeo.mapapi.widgets.RouteView;
import com.eegeo.mapapi.widgets.RouteViewOptions;


public class FindPointOnPathActivity extends WrldExampleActivity {

    private MapView m_mapView;
    private IndoorMapView m_indoorMapView = null;
    private EegeoMap m_eegeoMap = null;
    private List<Marker> m_markers = new ArrayList<Marker>();
    private Polyline m_polyline = null;
    private List<LatLng> m_path = new ArrayList<LatLng>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EegeoApi.init(this, getString(R.string.eegeo_api_key));
        setContentView(R.layout.find_point_on_path_activity);

        m_mapView = (MapView) findViewById(R.id.find_point_on_path_mapview);
        m_mapView.onCreate(savedInstanceState);

        m_path.add(new LatLng(56.459866, -2.980015));
        m_path.add(new LatLng(56.459727, -2.979299));
        m_path.add(new LatLng(56.461067, -2.979750));
        m_path.add(new LatLng(56.461017, -2.980325));

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;
                m_polyline = m_eegeoMap.addPolyline(new PolylineOptions()
                        .add(
                                m_path.get(0),
                                m_path.get(1),
                                m_path.get(2),
                                m_path.get(3))
                        .color(ColorUtils.setAlphaComponent(Color.RED, 128))
                );

                final LatLng latLng = new LatLng(56.460624, -2.978936);


                m_eegeoMap.getPointOnPath(latLng, m_path)
                        .then(new Ready<PointOnPath>() {
                            @Override
                            public void ready(PointOnPath pointOnPath) {
                                MarkerOptions targetMarkerOptions = new MarkerOptions().position(latLng).labelText("Target Point");
                                final Marker targetMarker = m_eegeoMap.addMarker(targetMarkerOptions);
                                m_markers.add(targetMarker);

                                MarkerOptions resultMarkerOptions = new MarkerOptions().position(pointOnPath.resultPoint).labelText("Result Point: "
                                        + "\n fractionAlongPath: " + Double.toString(pointOnPath.fractionAlongPath));
                                Marker resultMarker = m_eegeoMap.addMarker(resultMarkerOptions);
                                m_markers.add(resultMarker);
                            }
                        });
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
            for (Marker marker: m_markers) {
                m_eegeoMap.removeMarker(marker);
            }

            m_eegeoMap.removePolyline(m_polyline);
        }

        m_mapView.onDestroy();
    }
}

