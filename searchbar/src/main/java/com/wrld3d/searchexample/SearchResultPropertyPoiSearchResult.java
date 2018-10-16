package com.wrld3d.searchexample;

import com.eegeo.mapapi.services.poi.PoiSearchResult;
import com.wrld.widgets.search.model.SearchResultProperty;

public class SearchResultPropertyPoiSearchResult implements SearchResultProperty<PoiSearchResult> {
    private String m_key;
    private PoiSearchResult m_value;

    /**
     * Constructor
     * @param key String Unique key to identify the property.
     * @param value String value of the property.
     */
    public SearchResultPropertyPoiSearchResult(String key, PoiSearchResult value) {
        m_key = key;
        m_value = value;
    }

    @Override
    public String getKey() {
        return m_key;
    }

    @Override
    public PoiSearchResult getValue() {
        return m_value;
    }

    @Override
    public int compareTo(SearchResultProperty<PoiSearchResult> searchResultProperty) {
        return Double.compare(m_value.id, searchResultProperty.getValue().id);
    }
}
