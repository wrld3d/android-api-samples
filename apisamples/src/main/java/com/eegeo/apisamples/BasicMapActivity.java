package com.eegeo.apisamples;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.camera.CameraPosition;
import com.eegeo.mapapi.geometry.LatLng;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.services.poi.PoiService;
import com.wrld.searchproviders.AlertErrorHandler;
import com.wrld.searchproviders.WrldPoiSearchProvider;
import com.wrld.searchproviders.YelpSearchProvider;
import com.wrld.widgets.searchbox.WrldSearchWidget;
import com.wrld.widgets.searchbox.model.MenuGroup;
import com.wrld.widgets.searchbox.model.MenuOption;
import com.wrld.widgets.searchbox.model.OnMenuGroupInteractionCallback;
import com.wrld.widgets.searchbox.model.OnMenuOptionSelectedCallback;

public class BasicMapActivity extends WrldExampleActivity {

    private MapView m_mapView;

    WrldPoiSearchProvider m_provider;
    YelpSearchProvider m_yelpProvider;
    WrldSearchWidget m_widget;

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
                m_widget = (WrldSearchWidget)getFragmentManager().findFragmentById (R.id.basic_searchwidget);

                m_provider = new WrldPoiSearchProvider(m_mapView.getContext(), poiService, map);
                m_yelpProvider = new YelpSearchProvider(m_mapView.getContext(), m_requestQueue, map, m_errorHandler);
                m_yelpProvider.authenticate(getString(R.string.yelp_api_key));

                m_widget.addSearchProvider(m_provider);
                m_widget.addSearchProvider(m_yelpProvider);
                m_widget.addSuggestionProvider(m_provider);
                m_widget.addSuggestionProvider(m_yelpProvider);

                // MENU

                OnMenuOptionSelectedCallback findCallback = new OnMenuOptionSelectedCallback() {
                    @Override
                    public boolean onMenuOptionSelected(final String text, final Object context) {
                        //widget.doSearch((String)context);
                        return true;
                    }
                };
                OnMenuOptionSelectedCallback locationsCallback = new OnMenuOptionSelectedCallback() {
                    @Override
                    public boolean onMenuOptionSelected(final String text, final Object context) {
                        map.setCameraPosition(new CameraPosition((LatLng)context, 16, 0, 0));
                        return true;
                    }
                };
                OnMenuGroupInteractionCallback locationsExpandCallback = new OnMenuGroupInteractionCallback() {
                    @Override
                    public void onMenuGroupInteraction(final String text, final Object context) {
                        final CameraPosition cameraPosition = map.getCameraPosition();
                    }
                };
                OnMenuGroupInteractionCallback locationsCollapseCallback = new OnMenuGroupInteractionCallback() {
                    @Override
                    public void onMenuGroupInteraction(final String text, final Object context) {
                        final CameraPosition cameraPosition = map.getCameraPosition();
                        map.setCameraPosition(new CameraPosition(new LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude), 16, 0, 0));
                    }
                };
                MenuGroup groupA = new MenuGroup("Show me the closest...");
                groupA.addOption("Meeting Rooms", "meeting_room", findCallback);
                groupA.addOption("Training Rooms", "training_room", findCallback);
                groupA.addOption("Facilities & Amenities", "facilities_amenities", findCallback);
                groupA.addOption("Buildings", "buildings", findCallback);
                groupA.addOption("Desk Groups", "desk_groups", findCallback);
                MenuGroup groupB = new MenuGroup();
                MenuOption locationReports = new MenuOption("My Location Reports");
                locationReports.addChild("A", null, null, null);
                locationReports.addChild("A#/Bb", null, null, null);
                locationReports.addChild("B", null, null, null);
                locationReports.addChild("C", null, null, null);
                locationReports.addChild("C#", null, null, null);
                locationReports.addChild("D", null, null, null);
                locationReports.addChild("D#/Eb", null, null, null);
                locationReports.addChild("E", null, null, null);
                locationReports.addChild("F", null, null, null);
                locationReports.addChild("F#", null, null, null);
                locationReports.addChild("G", null, null, null);
                locationReports.addChild("G#", null, null, null);
                locationReports.addChild("...", null, null, null);
                locationReports.addChild("....", null, null, null);
                groupB.addOption(locationReports);
                MenuOption cityLocationOptions = new MenuOption("City Locations", null, locationsExpandCallback, locationsCollapseCallback);
                cityLocationOptions.addChild("Bangkok", android.R.drawable.ic_media_play, new LatLng(13.747348, 100.533493), locationsCallback);
                cityLocationOptions.addChild("Chicago", android.R.drawable.ic_media_play, new LatLng(41.882276, -87.629201), locationsCallback);
                cityLocationOptions.addChild("Dundee", android.R.drawable.ic_media_play, new LatLng(56.458598, -2.969868), locationsCallback);
                cityLocationOptions.addChild("Edinburgh", android.R.drawable.ic_media_play, new LatLng(55.948991, -3.199949), locationsCallback);
                cityLocationOptions.addChild("London", android.R.drawable.ic_media_play, new LatLng(51.51122, -0.081494), locationsCallback);
                cityLocationOptions.addChild("Milan", android.R.drawable.ic_media_play, new LatLng(45.474097, 9.177512), locationsCallback);
                cityLocationOptions.addChild("New York", android.R.drawable.ic_media_play, new LatLng(40.710184, -74.012957), locationsCallback);
                cityLocationOptions.addChild("Oslo", android.R.drawable.ic_media_play, new LatLng(59.907757, 10.752348), locationsCallback);
                cityLocationOptions.addChild("San Francisco", android.R.drawable.ic_media_play, new LatLng(37.791592, -122.39937), locationsCallback);
                cityLocationOptions.addChild("Vancouver", android.R.drawable.ic_media_play, new LatLng(49.289009, -123.125933), locationsCallback);
                groupB.addOption(cityLocationOptions);
                MenuGroup groupC = new MenuGroup();
                groupC.addOption(new MenuOption("Options", null, new OnMenuOptionSelectedCallback() {
                    @Override
                    public boolean onMenuOptionSelected(String text, Object context) {
                        AlertDialog dialog = new AlertDialog.Builder(BasicMapActivity.this)
                                .setTitle("Options")
                                .setMessage("WRLD Maps!")
                                .create();
                        dialog.show();
                        return false;
                    }
                }));
                groupC.addOption(new MenuOption("About", null, new OnMenuOptionSelectedCallback() {
                    @Override
                    public boolean onMenuOptionSelected(String text, Object context) {
                        AlertDialog dialog = new AlertDialog.Builder(BasicMapActivity.this)
                                .setTitle("About")
                                .setMessage("WRLD Maps!")
                                .create();
                        dialog.show();
                        return false;
                    }
                }));
                m_widget.addMenuGroup(groupA);
                m_widget.addMenuGroup(groupB);
                m_widget.addMenuGroup(groupC);

                // DO SEARCHG

                SearchManager searchManager =
                        (SearchManager) getSystemService(Context.SEARCH_SERVICE);
                SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
                m_widget.setSearchableInfo(searchableInfo);

                m_widget.doSearch("Coffee", null);
            }
        });
    }

    @Override
    public void onNewIntent(Intent intent){
        setIntent(intent);
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if(query != null) {
                m_widget.doSearch(query, null);

            }
        }
        if(Intent.ACTION_VIEW.equals(intent.getAction())) {
            //Uri data = intent.getData();
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
