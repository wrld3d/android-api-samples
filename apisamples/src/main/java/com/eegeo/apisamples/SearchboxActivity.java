package com.eegeo.apisamples;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.content.Context;
import android.widget.ImageButton;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.camera.CameraPosition;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.services.poi.PoiService;
import com.wrld.searchproviders.AlertErrorHandler;
import com.wrld.searchproviders.ErrorHandler;
import com.wrld.searchproviders.YelpSearchProvider;
import com.wrld.searchproviders.YelpSearchResultViewFactory;
import com.wrld.widgets.searchbox.SpoofSuggestionProvider;
import com.wrld.widgets.searchbox.api.DefaultSearchResultViewFactory;
import com.wrld.searchproviders.WrldPoiSearchProvider;
import com.wrld.widgets.searchbox.api.DefaultSuggestionViewFactory;
import com.wrld.widgets.searchbox.api.SearchModule;
import com.wrld.widgets.searchbox.api.SearchProvider;
import com.wrld.widgets.searchbox.api.SearchResultViewFactory;
import com.wrld.widgets.searchbox.api.SuggestionProvider;
import com.wrld.widgets.searchbox.menu.SearchBoxMenuChild;
import com.wrld.widgets.searchbox.menu.SearchBoxMenuGroup;
import com.wrld.widgets.searchbox.menu.SearchBoxMenuItem;
import com.wrld.widgets.ui.TextHighlighter;

import java.util.List;

public class SearchboxActivity extends AppCompatActivity {

    private MapView m_mapView;

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

        m_errorHandler = new AlertErrorHandler(context);
        m_requestQueue = Volley.newRequestQueue(context);
        m_requestQueue.start();

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                ViewGroup uiLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.searchbox_activity_ui, m_mapView, true);
                m_searchModule = (SearchModule)uiLayout.findViewById(R.id.search_module);
                int matchedTextColor = ContextCompat.getColor(context, com.wrld.widgets.R.color.searchbox_autcomplete_list_header_font_matched);

				//TODO use another suggestion factory 
                m_defaultSuggestionViewFactory = new DefaultSuggestionViewFactory(
                        com.wrld.widgets.R.layout.search_suggestion,
                        m_searchModule,
                        new TextHighlighter(matchedTextColor));

                SuggestionProvider poiSearchProvider = createWrldPoiSearchProvider(context, map);

                // Sets up and authenticates with Yelp.  You need a Yelp API key to use Yelp.
                SuggestionProvider yelpSearchProvider = createYelpSearchProvider(context, map);

                SuggestionProvider spoofProvider = new SpoofSuggestionProvider("Spoof", m_defaultSuggestionViewFactory, 50, 100);

                m_searchModule.setSearchProviders(new SearchProvider[]{poiSearchProvider, yelpSearchProvider});
                m_searchModule.setSuggestionProviders(new SuggestionProvider[]{poiSearchProvider, yelpSearchProvider, spoofProvider});

                //m_searchModule.setSearchProviders(new SearchProvider[]{poiSearchProvider, poiSearchProvider, poiSearchProvider, poiSearchProvider, yelpSearchProvider});
                //m_searchModule.setSuggestionProviders(new SuggestionProvider[]{poiSearchProvider, poiSearchProvider, poiSearchProvider, poiSearchProvider, yelpSearchProvider});
                SearchBoxMenuGroup locations = m_searchModule.addGroup("Locations");
                locations.add(createLocations());
                locations.addOnClickListenerToAllChildren(jumpToLocation(map, m_searchModule));

                ImageButton button = (ImageButton) uiLayout.findViewById(R.id.button);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        displaySpeechRecognizer();
                    }
                });
            }
        });
    }

    private static final int SPEECH_REQUEST_CODE = 0;

    // Create an intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
// Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    // This callback is invoked when the Speech Recognizer returns.
// This is where you process the intent and extract the speech text from the intent.
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            // Do something with spokenText
            m_searchModule.doSearch(spokenText);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private SearchBoxMenuItem.OnClickListener jumpToLocation(final EegeoMap map, final SearchModule searchModule){
        return new SearchBoxMenuItem.OnClickListener() {
            @Override
            public void onClick(SearchBoxMenuItem clickedItem) {
                CameraPositionChild clickedLocation = (CameraPositionChild) clickedItem;
                map.setCameraPosition(clickedLocation.getCameraPosition());
                searchModule.showDefaultView();
            }
        };
    }

    private class CameraPositionChild extends SearchBoxMenuChild {

        private CameraPosition m_cameraPosition;

        public CameraPosition getCameraPosition() { return m_cameraPosition; }

        public CameraPositionChild(String text, CameraPosition cameraPosition) {
            super(text);
            m_cameraPosition = cameraPosition;
        }
    }

    private SearchBoxMenuChild[] createLocations(){
        SearchBoxMenuChild[] locations = new SearchBoxMenuChild[10];
        CameraPosition.Builder builder = new CameraPosition.Builder();

        locations[0] = new CameraPositionChild("Bangkok",       builder.target(13.747348, 100.533493).build());
        locations[1] = new CameraPositionChild("Chicago",       builder.target(41.882276, -87.629201).build());
        locations[2] = new CameraPositionChild("Dundee",        builder.target(56.458598, -2.969868).build());
        locations[3] = new CameraPositionChild("Edinburgh",     builder.target(55.948991, -3.199949).build());
        locations[4] = new CameraPositionChild("London",        builder.target(51.51122, -0.081494).build());
        locations[5] = new CameraPositionChild("Milan",         builder.target(45.474097, 9.177512).build());
        locations[6] = new CameraPositionChild("New York",      builder.target(40.710184, -74.012957).build());
        locations[7] = new CameraPositionChild("Oslo",          builder.target(59.907757, 10.752348).build());
        locations[8] = new CameraPositionChild("San Francisco", builder.target(-22.948350, -43.207783).build());
        locations[9] = new CameraPositionChild("Vancouver",     builder.target(37.791592, -122.39937).build());
        return locations;
    }

    private SuggestionProvider createWrldPoiSearchProvider(Context context, EegeoMap map) {
        PoiService poiService = map.createPoiService();

        SuggestionProvider wrldPoiSearchProvider =  new WrldPoiSearchProvider(context, poiService, map);
        wrldPoiSearchProvider.setResultViewFactory(m_defaultSearchResultViewFactory);
        wrldPoiSearchProvider.setSuggestionViewFactory(m_defaultSuggestionViewFactory);
        return wrldPoiSearchProvider;
    }

    private SuggestionProvider createYelpSearchProvider (Context context, EegeoMap map) {

        SearchResultViewFactory yelpResultViewFactory = new YelpSearchResultViewFactory(context);
        YelpSearchProvider yelpSearch = new YelpSearchProvider(context, m_requestQueue, map, m_errorHandler);

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
