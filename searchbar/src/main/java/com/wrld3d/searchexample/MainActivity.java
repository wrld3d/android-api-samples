package com.wrld3d.searchexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.eegeo.indoors.IndoorMapView;
import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.map.OnMapReadyCallback;

import com.wrld.widgets.search.WrldSearchWidget;
import com.wrld.widgets.search.model.MenuChild;
import com.wrld.widgets.search.model.MenuGroup;
import com.wrld.widgets.search.model.MenuOption;
import com.wrld.widgets.search.model.OnMenuOptionSelectedCallback;
import com.wrld.widgets.search.view.MenuViewListener;

public class MainActivity extends AppCompatActivity {

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;
    private IndoorMapView m_interiorView = null;

    private WrldSearchWidget m_widget;
    private WrldPoiSearchProvider m_searchProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setContentView(R.layout.activity_main);
        m_mapView = (MapView) findViewById(R.id.mapView);
        m_mapView.onCreate(savedInstanceState);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;

                RelativeLayout uiContainer = (RelativeLayout) findViewById(R.id.eegeo_ui_container);
                m_interiorView = new IndoorMapView(m_mapView, uiContainer, m_eegeoMap);

                m_widget = (WrldSearchWidget) getFragmentManager().findFragmentById(R.id.basic_searchwidget);
                m_searchProvider = new WrldPoiSearchProvider(m_eegeoMap);
                m_widget.addSearchProvider(m_searchProvider);
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
