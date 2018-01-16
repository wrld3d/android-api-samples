package com.eegeo.apisamples;

import android.os.Bundle;
import android.widget.Toast;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.services.poi.OnPoiSearchCompletedListener;
import com.eegeo.mapapi.services.poi.PoiSearch;
import com.eegeo.mapapi.services.poi.PoiSearchResponse;
import com.eegeo.mapapi.services.poi.PoiService;
import com.eegeo.mapapi.services.poi.TextSearchOptions;


public class CancelSearchActivity extends WrldExampleActivity implements OnPoiSearchCompletedListener {

    private MapView m_mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setContentView(R.layout.basic_map_activity);
        m_mapView = (MapView) findViewById(R.id.basic_mapview);
        m_mapView.onCreate(savedInstanceState);

        final OnPoiSearchCompletedListener listener = this;

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                PoiService poiService = map.createPoiService();

                PoiSearch search = poiService.searchText(
                        new TextSearchOptions("poi", map.getCameraPosition().target.toLatLng())
                        .radius(1000.0)
                        .number(60)
                        .onPoiSearchCompletedListener(listener));

                search.cancel();
                Toast.makeText(CancelSearchActivity.this, "Search cancelled", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onPoiSearchCompleted(PoiSearchResponse response) {
        // Search is cancelled and never completes
        Toast.makeText(CancelSearchActivity.this, "This Toast should not be shown.", Toast.LENGTH_LONG).show();
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
