package com.example.lihui20.testhttp.customview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by lihui20 on 2016/12/20.
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space/2;
        outRect.right = space/2;
        outRect.bottom = space;
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = space;
        }
    }
}
