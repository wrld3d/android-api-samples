package com.eegeo.apisamples;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.graphics.Point;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.camera.CameraPosition;
import com.eegeo.mapapi.camera.CameraUpdateFactory;
import com.eegeo.mapapi.geometry.LatLng;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.positioner.Positioner;
import com.eegeo.mapapi.positioner.PositionerOptions;
import com.eegeo.mapapi.positioner.OnPositionerChangedListener;

public class AddPositionerActivity extends AppCompatActivity {

    private MapView m_mapView;
    TextView m_textView;
    CrossView m_crossView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialise the EegeoApi with your api key - this needs calling either in the application
        // instance, or in the activity that contains the MapView
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        // The MapView is contained in a layout xml
        setContentView(R.layout.add_positioner_activity);

        m_mapView = (MapView) findViewById(R.id.add_positioner_mapview);
        m_mapView.onCreate(savedInstanceState);

        //The coordinate will be printed in the top left corner of the screen
        m_textView = (TextView) findViewById(R.id.add_positioner_textview);

        //A cross will be drawn on the map using this
        m_crossView = new CrossView(this);
        m_crossView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        m_mapView.addView(m_crossView);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {

                //Add a Positioner to a point on the map
                map.addPositioner(new PositionerOptions()
                        .position(new LatLng(37.802115, -122.405592))
                        .positionerChangedListener(new OnScreenPointChangedListener(m_textView, m_crossView)
                    )
                );

                CameraPosition position = new CameraPosition.Builder()
                        .target(37.802355, -122.405848)
                        .zoom(19)
                        .build();
                map.moveCamera(CameraUpdateFactory.newCameraPosition(position));
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


    private class OnScreenPointChangedListener implements OnPositionerChangedListener {

        private TextView m_textView;
        private CrossView m_crossView;

        OnScreenPointChangedListener(@NonNull TextView textView, @NonNull CrossView crossView)
        {
            m_textView = textView;
            m_crossView = crossView;
        }

        //This will be called each time the screen coordinate of the positioner changes.
        @UiThread
        public void onPositionerChanged(Positioner positioner)
        {
            Point screenPoint = positioner.getScreenPoint();

            //Print out the screen coordinate.
            String string = String.format(getString(R.string.message_add_positioner_activity), screenPoint.x, screenPoint.y);
            m_textView.setText(string);

            //Draw a cross
            m_crossView.setX(screenPoint.x - (m_crossView.getWidth ()/2));
            m_crossView.setY(screenPoint.y - (m_crossView.getHeight()/2));
        }
    };

    class CrossView extends View
    {
        private Paint m_paint;
        private int m_size=100;

        public CrossView(Context context) {
            super(context);
            m_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            m_paint.setColor(Color.RED);
            m_paint.setStrokeWidth(16f);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

            setMeasuredDimension(m_size, m_size);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int s = m_size;
            canvas.drawLine(0, 0, s, s, m_paint);
            canvas.drawLine(0, s, s, 0, m_paint);
        }
    }
}
