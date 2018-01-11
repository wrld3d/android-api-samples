package com.eegeo.apisamples;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.eegeo.apisamples.expandablelistview.ExtendedBaseExpandableListViewAdapter;
import com.eegeo.apisamples.expandablelistview.RecyclerViewAdapter;

public class ListViewMapActivity extends SoftBackButtonActivity {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // The MapView is contained in a layout xml
        setContentView(R.layout.list_view_map_activity);

        //setupExpandableListView();
        setupRecyclerView();
    }

    private void setupRecyclerView(){
        RecyclerView listView = (RecyclerView) findViewById(R.id.edbert);
        listView.setVisibility(View.VISIBLE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(linearLayoutManager);

        listView.setAdapter(new RecyclerViewAdapter());

        //addGroupDecoration(listView);
    }

    private void addGroupDecoration(RecyclerView recyclerView)
    {
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            private int textSize = 50;
            private int groupSpacing = 100;
            private int itemsInGroup = 3;

            private Paint paint = new Paint();
            {
                paint.setTextSize(textSize);
            }

            @Override
            public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
                for (int i = 0; i < parent.getChildCount(); i++) {
                    View view = parent.getChildAt(i);
                    int position = parent.getChildAdapterPosition(view);
                    if (position % itemsInGroup == 0) {
                        c.drawText("Group " + (position / itemsInGroup + 1), view.getLeft(),
                                view.getTop() - groupSpacing / 2 + textSize / 3, paint);
                    }
                }
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) % itemsInGroup == 0) {
                    outRect.set(0, groupSpacing, 0, 0);
                }
            }
        });
    }

    private void setupExpandableListView(){
        ExpandableListView listView = (ExpandableListView) findViewById(R.id.wilbur);
        listView.setVisibility(View.VISIBLE);
        //expandableListView.setLayoutTransition(createLayoutTransition());
        listView.setAdapter(new ExtendedBaseExpandableListViewAdapter());
    }

    private LayoutTransition createLayoutTransition(){
        LayoutTransition layoutTransition = new LayoutTransition();

        ObjectAnimator animIn = ObjectAnimator.ofFloat(null, "scaleY", 0f, 1f);
        ObjectAnimator animOut = ObjectAnimator.ofFloat(null, "scaleY", 1f, 0f);

        animIn.setDuration(1000);
        animOut.setDuration(1000);

        animIn.addListener(new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator anim) {
                TextView view = (TextView) ((ObjectAnimator) anim).getTarget();
            }
        });

        layoutTransition.enableTransitionType(LayoutTransition.APPEARING);
        layoutTransition.enableTransitionType(LayoutTransition.CHANGE_APPEARING);
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
        layoutTransition.enableTransitionType(LayoutTransition.CHANGE_DISAPPEARING);
        layoutTransition.enableTransitionType(LayoutTransition.DISAPPEARING);

        layoutTransition.setAnimator(LayoutTransition.APPEARING, animIn);
        layoutTransition.setAnimator(LayoutTransition.CHANGE_APPEARING, animIn);
        layoutTransition.setAnimator(LayoutTransition.CHANGING, animIn);
        layoutTransition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, animOut);
        layoutTransition.setAnimator(LayoutTransition.DISAPPEARING, animOut);

        return layoutTransition;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
