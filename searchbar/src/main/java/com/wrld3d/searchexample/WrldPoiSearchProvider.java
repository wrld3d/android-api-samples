package com.wrld3d.searchexample;

import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.camera.CameraPosition;
import com.eegeo.mapapi.camera.CameraUpdateFactory;
import com.eegeo.mapapi.geometry.LatLng;
import com.eegeo.mapapi.markers.MarkerOptions;
import com.eegeo.mapapi.services.poi.OnPoiSearchCompletedListener;
import com.eegeo.mapapi.services.poi.PoiSearchResponse;
import com.eegeo.mapapi.services.poi.PoiSearchResult;
import com.eegeo.mapapi.services.poi.PoiService;
import com.eegeo.mapapi.services.poi.TagSearchOptions;
import com.eegeo.mapapi.services.poi.TextSearchOptions;
import com.wrld.widgets.search.model.DefaultSearchResult;
import com.wrld.widgets.search.model.SearchProvider;
import com.wrld.widgets.search.model.SearchProviderResultsReadyCallback;
import com.wrld.widgets.search.model.SearchResult;
import com.wrld.widgets.search.model.SearchResultProperty;
import com.wrld.widgets.search.model.SearchResultPropertyString;
import com.wrld.widgets.search.model.SearchResultSelectedListener;
import com.wrld.widgets.search.view.DefaultSearchResultViewFactory;
import com.wrld.widgets.search.view.ISearchResultViewFactory;

import java.util.ArrayList;
import java.util.List;

public class WrldPoiSearchProvider implements SearchProvider, OnPoiSearchCompletedListener, SearchResultSelectedListener {

    EegeoMap m_map;
    PoiService m_poiService;
    private List<SearchProviderResultsReadyCallback> m_callbacks;
    private final DefaultSearchResultViewFactory m_viewFactory;

    public WrldPoiSearchProvider(EegeoMap map) {
        m_map = map;
        m_poiService = map.createPoiService();
        m_callbacks = new ArrayList<SearchProviderResultsReadyCallback>();
        m_viewFactory = new DefaultSearchResultViewFactory(R.layout.search_result);
    }

    @Override
    public String getTitle() {
        return "Search";
    }

    @Override
    public void getSearchResults(String queryText, Object queryContext) {
        m_poiService.searchText(
                new TextSearchOptions(queryText, m_map.getCameraPosition().target)
                        .onPoiSearchCompletedListener(this));

    }

    @Override
    public void onPoiSearchCompleted(PoiSearchResponse response) {
        List<SearchResult> results = new ArrayList<>();
        for (PoiSearchResult poi : response.getResults()) {
            MarkerOptions options = new MarkerOptions()
                    .labelText(poi.title)
                    .position(poi.latLng)
                    .iconKey("pin");

            if (poi.indoor) {
                options.indoor(poi.indoorId, poi.floorId);
            }

            m_map.addMarker(options);

            DefaultSearchResult searchResult = new DefaultSearchResult(poi.title, new SearchResultPropertyString("Description", poi.subtitle), new SearchResultPropertyPoiSearchResult("Poi", poi));
            searchResult.setSelectedListener(this);
            results.add(searchResult);
        }

        if (results.size() > 0) {
            for(SearchProviderResultsReadyCallback callback : m_callbacks)
            {
                SearchResult resultArray[] = new SearchResult[results.size()];
                callback.onQueryCompleted(results.toArray(resultArray), true);
            }
        } else {
            for(SearchProviderResultsReadyCallback callback : m_callbacks)
            {
                callback.onQueryCompleted(null, false);
            }
        }
    }

    @Override
    public void cancelSearch() {

    }

    @Override
    public void addSearchCompletedCallback(SearchProviderResultsReadyCallback callback) {
        m_callbacks.add(callback);
    }

    @Override
    public void removeSearchCompletedCallback(SearchProviderResultsReadyCallback callback) {
        m_callbacks.remove(callback);
    }

    @Override
    public ISearchResultViewFactory getResultViewFactory() {
        return m_viewFactory;
    }

    @Override
    public void onSearchResultSelected(SearchResult searchResult) {
        PoiSearchResult poiResult =  (PoiSearchResult)searchResult.getProperty("Poi").getValue();

        CameraPosition position = new CameraPosition.Builder()
                .target(poiResult.latLng)
                .zoom(19)
                .build();

        m_map.moveCamera(CameraUpdateFactory.newCameraPosition(position));

    }
}
