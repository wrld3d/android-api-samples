package com.wrld.androidpoiexample;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.markers.Marker;
import com.eegeo.mapapi.markers.OnMarkerClickListener;
import com.eegeo.mapapi.services.poi.PoiService;
import com.eegeo.mapapi.services.poi.TextSearchOptions;
import com.eegeo.mapapi.services.poi.OnPoiSearchCompletedListener;
import com.eegeo.mapapi.services.poi.PoiSearchResponse;
import com.eegeo.mapapi.services.poi.PoiSearchResult;
import com.eegeo.mapapi.markers.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity  implements OnPoiSearchCompletedListener {

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;
    private int m_failedSearches = 0;
    private OnMarkerClickListener m_markerTappedListener = new MarkerClickListenerImpl();

    PoiView m_poiView = null;
    List<PoiSearchResult> m_searchResults;
    HashMap<String, PoiSearchResult> m_resultsMap = new HashMap<>();
    Map<String,String> m_iconKeyTagDict = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EegeoApi.init(this, getString(R.string.wrld_api_key));

        setContentView(R.layout.activity_main);

        m_poiView = new PoiView(this);

        m_mapView = (MapView)findViewById(R.id.mapView);
        m_mapView.onCreate(savedInstanceState);

        final OnPoiSearchCompletedListener listener = this;

        // Icon/Tag mapping, see:
        // https://github.com/wrld3d/wrld-icon-tools/blob/master/data/search_tags.json
        m_iconKeyTagDict = new HashMap<String, String>() {
            {
                put("general", "general");
                put("zoo", "zoo");
            }
        };

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;
                m_eegeoMap.addMarkerClickListener(m_markerTappedListener);

                PoiService poiService = map.createPoiService();

                poiService.searchText(
                        new TextSearchOptions("Test", map.getCameraPosition().target)
                                .radius(1000.0)
                                .number(60)
                                .onPoiSearchCompletedListener(listener));
            }
        });
    }

    @Override
    public void onPoiSearchCompleted(PoiSearchResponse response) {
        List<PoiSearchResult> results = response.getResults();
        m_searchResults = results;
        // Storing search results in a map with their title as the key.
        for (int i = 0; i < m_searchResults.size(); ++i) {
            m_resultsMap.put(m_searchResults.get(i).title, m_searchResults.get(i));
        }

        if (response.succeeded() && results.size() > 0) {
            for (PoiSearchResult poi : results) {

                String iconKey = "pin";
                String[] tags = poi.tags.split(" ");
                for (String tag : tags) {
                    if (m_iconKeyTagDict.containsKey(tag)) {
                        iconKey = m_iconKeyTagDict.get(tag);
                    }
                }

                MarkerOptions options = new MarkerOptions()
                        .labelText(poi.title)
                        .position(poi.latLng)
                        .iconKey(iconKey)
                        .userData(poi.userData);

                if (poi.indoor) {
                    options.indoor(poi.indoorId, poi.floorId);
                }

                m_eegeoMap.addMarker(options);
            }
        }
        else {
            m_failedSearches += 1;

            if (m_failedSearches >= 3) {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.no_pois_found_title)
                        .setMessage(R.string.no_pois_found_message)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
    }

    public int dipAsPx(float dip)
    {
        float density = getResources().getDisplayMetrics().density;
        int px = (int)((dip * density) + 0.5f);
        return px;
    }

    public void recursiveDisableSplitMotionEvents(ViewGroup group)
    {
        group.setMotionEventSplittingEnabled(false);

        for(int i = 0; i < group.getChildCount(); i++)
        {
            View child = group.getChildAt(i);

            if(child instanceof ViewGroup)
            {
                recursiveDisableSplitMotionEvents((ViewGroup)child);
            }
        }
    }

    private class MarkerClickListenerImpl implements OnMarkerClickListener {
        public void onMarkerClick(Marker marker) {
            DisplayMarkerPoi(marker);
        }

        public void DisplayMarkerPoi(Marker marker)
        {
            PoiSearchResult poi = m_resultsMap.get(marker.getTitle());
            JSONObject jsonObj = new JSONObject();
            try {
                jsonObj = new JSONObject(poi.userData);

                String address = "";
                String description = "";
                String phone = "";
                String web = "";
                String image_url = "";
                String facebook = "";
                String twitter = "";
                String email = "";
                String customView = "";
                int customViewHeight = -1;

                if (jsonObj.has("address")) {
                    address = jsonObj.optString("address");
                }
                if (jsonObj.has("description")) {
                    description = jsonObj.optString("description");
                }
                if (jsonObj.has("phone")) {
                    phone = jsonObj.optString("phone");
                }
                if (jsonObj.has("web")) {
                    web = jsonObj.optString("web");
                }
                if (jsonObj.has("image_url")) {
                    image_url = jsonObj.optString("image_url");
                }
                if (jsonObj.has("facebook")) {
                    facebook = jsonObj.optString("facebook");
                }
                if (jsonObj.has("twitter")) {
                    twitter = jsonObj.optString("twitter");
                }
                if (jsonObj.has("email")) {
                    email = jsonObj.optString("email");
                }
                if (jsonObj.has("custom_view")) {
                    customView = jsonObj.optString("custom_view");
                }
                if (jsonObj.has("custom_view_height")) {
                    customViewHeight = jsonObj.optInt("custom_view_height");
                }
                // Here you could show your own custom view instead that uses the poi info.
                String[] tagCollection = poi.tags.split("\\s+");
                m_poiView.displayPoiInfo(poi.title, poi.subtitle, address, description, phone, web, tagCollection[0], tagCollection, image_url,  facebook, twitter, email, customView, customViewHeight);
            } catch (JSONException e) {

            }
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        m_mapView.onResume();
    }

    @Override
    protected void onPause()
    {
        m_mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        m_mapView.onDestroy();
    }
}