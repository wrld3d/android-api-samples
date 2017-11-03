package com.eegeo.apisamples;

import android.os.Bundle;
import android.widget.Toast;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.markers.MarkerOptions;
import com.eegeo.mapapi.services.poi.AutocompleteOptions;
import com.eegeo.mapapi.services.poi.OnPoiSearchCompletedListener;
import com.eegeo.mapapi.services.poi.PoiSearchResponse;
import com.eegeo.mapapi.services.poi.PoiSearchResult;
import com.eegeo.mapapi.services.poi.PoiService;
import com.eegeo.mapapi.services.poi.TagSearchOptions;
import com.eegeo.mapapi.services.poi.TextSearchOptions;

import java.util.List;

public class SearchExampleActivity extends SoftBackButtonActivity implements OnPoiSearchCompletedListener {

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;

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
                m_eegeoMap = map;

                PoiService poiService = map.createPoiService();

                poiService.searchText(
                        new TextSearchOptions("free", map.getCameraPosition().target.toLatLng())
                        .radius(1000.0)
                        .number(60)
                        .onPoiSearchCompletedListener(listener));

                poiService.searchTag(
                        new TagSearchOptions("coffee", map.getCameraPosition().target.toLatLng())
                        .onPoiSearchCompletedListener(listener));

                poiService.searchAutocomplete(
                        new AutocompleteOptions("auto", map.getCameraPosition().target.toLatLng())
                        .onPoiSearchCompletedListener(listener));
            }
        });
    }

    @Override
    public void onPoiSearchCompleted(PoiSearchResponse response) {
        if (response.succeeded()) {
            List<PoiSearchResult> results = response.getResults();

            if (results.size() == 0) {
                Toast.makeText(SearchExampleActivity.this, "No POIs found!", Toast.LENGTH_LONG).show();
            }

            for (PoiSearchResult poi: results) {
                m_eegeoMap.addMarker(new MarkerOptions()
                        .labelText(poi.title)
                        .position(poi.latLng)
                        .iconKey(poi.tags));
            }
        }
        else {
            Toast.makeText(SearchExampleActivity.this, "POI search failed!", Toast.LENGTH_LONG).show();
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
