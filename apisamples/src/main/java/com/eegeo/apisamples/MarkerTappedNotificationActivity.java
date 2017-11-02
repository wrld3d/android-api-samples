package com.eegeo.apisamples;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.geometry.LatLng;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.markers.OnMarkerClickListener;
import com.eegeo.mapapi.markers.Marker;
import com.eegeo.mapapi.markers.MarkerOptions;

import java.util.Locale;

public class MarkerTappedNotificationActivity extends SoftBackButtonActivity {

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;
    private Marker m_markerA = null;
    private Marker m_markerB = null;
    private OnMarkerClickListener m_markerTappedListener = new MarkerClickListenerImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setContentView(R.layout.marker_tapped_notification_activity);
        m_mapView = (MapView) findViewById(R.id.marker_tapped_notification_mapview);
        m_mapView.onCreate(savedInstanceState);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;
                m_eegeoMap.addMarkerClickListener(m_markerTappedListener);

                m_markerA = m_eegeoMap.addMarker(new MarkerOptions()
                        .position(new LatLng(37.784560, -122.402092))
                        .labelText("Marker A")
                );

                m_markerB = m_eegeoMap.addMarker(new MarkerOptions()
                        .position(new LatLng(37.783372, -122.400834))
                        .labelText("Marker B")
                );
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
            m_eegeoMap.removeMarker(m_markerA);
            m_eegeoMap.removeMarker(m_markerB);
        }

        m_mapView.onDestroy();
    }

    private class MarkerClickListenerImpl implements OnMarkerClickListener {
        public void onMarkerClick(Marker marker) {
            String message = String.format(Locale.getDefault(), "'%1s' [%2$.6f, %3$.6f]",
                    marker.getTitle(),
                    marker.getPosition().latitude,
                    marker.getPosition().longitude);

            Context context = MarkerTappedNotificationActivity.this;

            new AlertDialog.Builder(context)
                    .setTitle("Marker tapped")
                    .setMessage(message)
                    .setIcon(android.R.drawable.ic_dialog_map)
                    .show();
        }
    }

}

