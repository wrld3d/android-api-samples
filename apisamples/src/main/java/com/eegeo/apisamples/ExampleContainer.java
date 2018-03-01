package com.eegeo.apisamples;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

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
    public final Class<? extends Activity> activityClass;

    public ExampleContainer(
            int titleResourceId, int descResourceId, Class<? extends Activity> activityClass) {
        this.titleResourceId = titleResourceId;
        this.descResourceId = descResourceId;
        this.activityClass = activityClass;
    }
}
