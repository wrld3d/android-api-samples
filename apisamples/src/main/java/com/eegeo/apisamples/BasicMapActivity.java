package com.eegeo.apisamples;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.services.poi.PoiService;
import com.wrld.searchproviders.AlertErrorHandler;
import com.wrld.searchproviders.WrldPoiSearchProvider;
import com.wrld.searchproviders.YelpSearchProvider;
import com.wrld.widgets.searchbox.WrldSearchWidget;
import com.wrld.widgets.searchbox.model.SearchWidgetSearchModel;

public class BasicMapActivity extends WrldExampleActivity {

    private MapView m_mapView;

    WrldPoiSearchProvider m_provider;
    YelpSearchProvider m_yelpProvider;
    SearchWidgetSearchModel m_searchWidgetSearchModel;

    private AlertErrorHandler m_errorHandler;
    private RequestQueue m_requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialise the EegeoApi with your api key - this needs calling either in the application
        // instance, or in the activity that contains the MapView
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        // The MapView is contained in a layout xml
        setContentView(R.layout.basic_map_activity);

        m_mapView = (MapView) findViewById(R.id.basic_mapview);
        m_mapView.onCreate(savedInstanceState);

        m_errorHandler = new AlertErrorHandler(m_mapView.getContext());
        m_requestQueue = Volley.newRequestQueue(m_mapView.getContext());
        m_requestQueue.start();

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                Toast.makeText(BasicMapActivity.this, "Welcome to WRLD Maps", Toast.LENGTH_LONG).show();

                PoiService poiService = map.createPoiService();

                // SEARCH WINDUP
                WrldSearchWidget widget = (WrldSearchWidget)getSupportFragmentManager().findFragmentById (R.id.basic_searchwidget);

                m_provider = new WrldPoiSearchProvider(m_mapView.getContext(), poiService, map);
                m_yelpProvider = new YelpSearchProvider(m_mapView.getContext(), m_requestQueue, map, m_errorHandler);
                m_yelpProvider.authenticate(getString(R.string.yelp_api_key));

                widget.addSearchProvider(m_provider);
                widget.addSearchProvider(m_yelpProvider);
                widget.addSuggestionProvider(m_yelpProvider);


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
        m_mapView.onDestroy();
    }

}
