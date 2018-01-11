package com.eegeo.apisamples.expandablelistview;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/**
 * Created by michael.odonnell on 10/01/2018.
 */

public class ExtendedBaseExpandableListViewAdapter extends BaseExpandableListAdapter {
    @Override
    public int getGroupCount() {
        return 5;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 20;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return "Group" + groupPosition;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return "Child" + childPosition;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = new TextView(parent.getContext());

        }

        TextView textView = (TextView)convertView;
        textView.setTextSize(24);
        textView.setText((String)getGroup(groupPosition));
        return textView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = new TextView(parent.getContext());
        }

        TextView textView = (TextView)convertView;
        textView.setText((String)getChild(groupPosition, childPosition));
        textView.setTextSize(18);
        return textView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
