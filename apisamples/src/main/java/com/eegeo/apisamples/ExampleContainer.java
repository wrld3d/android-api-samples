package com.eegeo.apisamples;

import androidx.appcompat.app.AppCompatActivity;

public class ExampleContainer {

    /**
     * The resource id of the example's title.
     */
    public final int titleResourceId;

    /**
     * The resource id of the example's description
     */
    public final int descResourceId;

    /**
     * Class for the example
     */
    public final Class<? extends AppCompatActivity> activityClass;

    public ExampleContainer(
            int titleResourceId, int descResourceId, Class<? extends AppCompatActivity> activityClass) {
        this.titleResourceId = titleResourceId;
        this.descResourceId = descResourceId;
        this.activityClass = activityClass;
    }
}
