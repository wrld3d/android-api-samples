// Copyright eeGeo Ltd (2012-2015), All Rights Reserved

package com.example.michaelchan.androidpoiexample;

import android.app.Activity;

public class TagResources
{
    public static int getSmallIconForTag(Activity activity, String tag)
    {
        return getIconForResourceName(activity, tag);
    }

    public static int getIconForResourceName(Activity activity, String resourceName)
    {
    	String iconPrefix = "icon1_";
        String uri = "drawable/" + iconPrefix + resourceName;
        int imageResource = activity.getResources().getIdentifier(uri, null, activity.getPackageName());
        return imageResource;
    }
}
