package com.eegeo.apisamples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.services.poi.PoiService;
import com.wrld.searchproviders.AlertErrorHandler;
import com.wrld.searchproviders.ErrorHandler;
import com.wrld.searchproviders.YelpSearchProvider;
import com.wrld.searchproviders.YelpSearchResultViewFactory;
import com.wrld.widgets.searchbox.DefaultSearchResultViewFactory;
import com.wrld.widgets.searchbox.SearchBoxMenuGroup;
import com.wrld.widgets.searchbox.SearchModule;
import com.wrld.widgets.searchbox.DefaultSuggestionViewFactory;
import com.wrld.searchproviders.WrldPoiSearchProvider;
import com.wrld.widgets.searchbox.SearchProvider;
import com.wrld.widgets.searchbox.SearchResultViewFactory;
import com.wrld.widgets.searchbox.SuggestionProvider;

public class SearchboxActivity extends AppCompatActivity {

    private MapView m_mapView;

    private ViewGroup m_searchView;

    private SearchModule m_searchModule;


    private SearchResultViewFactory m_defaultSearchResultViewFactory;
    private SearchResultViewFactory m_defaultSuggestionViewFactory;

    private ErrorHandler m_errorHandler;
    private RequestQueue m_requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BuildConfig.DEBUG) {
            // These flags cause the device screen to turn on (and bypass screen guard if possible) when launching.
            // This makes it easy for developers to test the app launch without needing to turn on the device
            // each time and without needing to enable the "Stay awake" option.
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                    | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        }

        final Context context = this;

        // Initialise the EegeoApi with your api key - this needs calling either in the application
        // instance, or in the activity that contains the MapView
        EegeoApi.init(context, getString(R.string.eegeo_api_key));

        // The MapView is contained in a layout xml
        setContentView(R.layout.basic_map_activity);

        m_mapView = (MapView) findViewById(R.id.basic_mapview);
        m_mapView.onCreate(savedInstanceState);

        m_defaultSearchResultViewFactory = new DefaultSearchResultViewFactory(com.wrld.widgets.R.layout.search_result);
        m_defaultSuggestionViewFactory = new DefaultSuggestionViewFactory(com.wrld.widgets.R.layout.search_suggestion);

        m_errorHandler = new AlertErrorHandler(context);
        m_requestQueue = Volley.newRequestQueue(context);
        m_requestQueue.start();

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                ViewGroup uiLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.searchbox_activity_ui, m_mapView, true);
                m_searchView = (ViewGroup) uiLayout.findViewById(R.id.searchbox_ui);
                m_searchModule = new SearchModule(m_searchView);

                SuggestionProvider poiSearchProvider = createWrldPoiSearchProvider(map);

                // Sets up and authenticates with Yelp.  You need a Yelp API key to use Yelp.
                SuggestionProvider yelpSearchProvider = createYelpSearchProvider(context, map);

                m_searchModule.setSearchProviders(new SearchProvider[]{poiSearchProvider, yelpSearchProvider});
                m_searchModule.setSuggestionProviders(new SuggestionProvider[]{poiSearchProvider, yelpSearchProvider});
            }
        });
    }

    private SuggestionProvider createWrldPoiSearchProvider(EegeoMap map) {
        PoiService poiService = map.createPoiService();

        SuggestionProvider wrldPoiSearchProvider =  new WrldPoiSearchProvider(poiService, map);
        wrldPoiSearchProvider.setResultViewFactory(m_defaultSearchResultViewFactory);
        wrldPoiSearchProvider.setSuggestionViewFactory(m_defaultSuggestionViewFactory);
        return wrldPoiSearchProvider;
    }

    private SuggestionProvider createYelpSearchProvider (Context context, EegeoMap map) {

        SearchResultViewFactory yelpResultViewFactory = new YelpSearchResultViewFactory(context);
        YelpSearchProvider yelpSearch = new YelpSearchProvider(m_requestQueue, map, m_errorHandler);

        // Yelp search requires authenticating with a Yelp API client id and secret.
        // This needs to be called before any search results or autocomplete suggestions are requested.
        // The keys can be obtained from https://www.yelp.com/developers/v3/manage_app
        yelpSearch.authenticate(getString(R.string.yelp_client_id), getString(R.string.yelp_client_secret));

        yelpSearch.setResultViewFactory(yelpResultViewFactory);
        yelpSearch.setSuggestionViewFactory(m_defaultSuggestionViewFactory);

        return yelpSearch;
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
