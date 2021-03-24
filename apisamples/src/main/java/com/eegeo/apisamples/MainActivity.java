package com.eegeo.apisamples;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.eegeo.mapapi.EegeoApi;

public class MainActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView exampleList = (ListView) findViewById(R.id.example_list);

        ListAdapter adapter = new ExampleArrayAdapter(this, ExamplesList.Examples);

        exampleList.setAdapter(adapter);
        exampleList.setOnItemClickListener(this);
        exampleList.setEmptyView(findViewById(R.id.empty));

        EegeoApi.init(getApplicationContext(), getString(R.string.eegeo_api_key));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ExampleContainer example = (ExampleContainer) parent.getAdapter().getItem(position);
        startActivity(new Intent(this, example.activityClass));
    }

    private static class ExampleArrayAdapter extends ArrayAdapter<ExampleContainer> {

        public ExampleArrayAdapter(Context context, ExampleContainer[] demos) {
            super(context, R.layout.feature, R.id.title, demos);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FeatureView featureView;
            if (convertView instanceof FeatureView) {
                featureView = (FeatureView) convertView;
            } else {
                featureView = new FeatureView(getContext());
            }

            ExampleContainer example = getItem(position);

            featureView.setTitleId(example.titleResourceId);
            featureView.setDescriptionId(example.descResourceId);

            Resources resources = getContext().getResources();
            String title = resources.getString(example.titleResourceId);
            String description = resources.getString(example.descResourceId);
            featureView.setContentDescription(title + ". " + description);

            return featureView;
        }
    }
}
