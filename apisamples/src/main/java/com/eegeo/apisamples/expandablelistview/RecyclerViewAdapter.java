/*
* Copyright (C) 2014 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.eegeo.apisamples.expandablelistview;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eegeo.apisamples.R;

import java.util.ArrayList;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter";

    private ArrayList<String[]> mDataSet;

    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView textView;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            textView = (TextView) v;
        }

        public TextView getTextView() {
            return textView;
        }

        // Handles the row being being clicked
        @Override
        public void onClick(View view) {
            android.util.Log.v("MOD", "clicked view");
        }
    }
    // END_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Initialize the dataset of the Adapter.
     */
    public RecyclerViewAdapter() {
        int groupCount = 10;
        int itemCount = 10;
        mDataSet = new ArrayList<String[]>();
        for(int group = 0; group < groupCount; ++group) {
            String[] children = new String[itemCount];
            for (int child = 0; child < itemCount; ++child) {
                children[child] = "Item " + (child + 1);
            }
            mDataSet.add(children);
        }
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int viewType) {
        // Create a new view.
        View v = new TextView(viewGroup.getContext());
        v.setBackgroundColor(Color.WHITE);

        return new ViewHolder(v);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.getTextView().setText(getChild(position));
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    @Override
    public int getItemViewType(int position){
        return isGroup(position) ? 0 : 1;
    }

    private String getChild(final int position){
        int count = 0;
        for(int groupIndex = 0; groupIndex < mDataSet.size(); ++groupIndex){
            if(position == count){
                return "Group " + groupIndex;
            }
            ++count;
            String[] group = mDataSet.get(groupIndex);
            int positionInGroup = position - count;
            if(positionInGroup < group.length){
                return group[positionInGroup];
            }
            count += group.length;
        }

        return "";
    }

    private boolean isGroup(final int position){
        int count = 0;
        for(int groupIndex = 0; groupIndex < mDataSet.size(); ++groupIndex){
            if(count == position){
                return true;
            }
            if(position < count){
                return false;
            }
            count +=  mDataSet.get(groupIndex).length + 1;
        }
        return false;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        int count = 0;
        for(String[] group : mDataSet){
            count += group.length;
        }
        return count + mDataSet.size();
    }
}