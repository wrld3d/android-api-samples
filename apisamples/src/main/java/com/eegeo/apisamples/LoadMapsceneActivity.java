package com.eegeo.apisamples;

import android.os.Bundle;
import android.widget.Toast;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.services.mapscene.MapsceneRequestOptions;
import com.eegeo.mapapi.services.mapscene.MapsceneRequestResponse;
import com.eegeo.mapapi.services.mapscene.MapsceneService;
import com.eegeo.mapapi.services.mapscene.OnMapsceneRequestCompletedListener;

public class LoadMapsceneActivity extends SoftBackButtonActivity implements OnMapsceneRequestCompletedListener
{

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setContentView(R.layout.basic_map_activity);
        m_mapView = (MapView) findViewById(R.id.basic_mapview);
        m_mapView.onCreate(savedInstanceState);

        final OnMapsceneRequestCompletedListener listener = this;

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;

                MapsceneService mapsceneService = map.createMapsceneService();

                mapsceneService.requestMapscene(
                        new MapsceneRequestOptions("https://wrld.mp/63fcc9b")
                        .onMapsceneRequestCompletedListener(listener)
                );
            }
        });
    }

    @Override
    public void onMapsceneRequestCompleted(MapsceneRequestResponse response) {
        if(response.succeeded())
        {
            String message = "Mapscene '" + response.getMapscene().name + "' loaded";
            Toast.makeText(LoadMapsceneActivity.this, message, Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(LoadMapsceneActivity.this, "Failed to load mapscene", Toast.LENGTH_LONG).show();
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
