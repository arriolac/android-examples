package com.example.expandablelistitem;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ListView;

public class PostListView extends ListView {

    public PostListView(Context context) {
        super(context);
    }
    
    public PostListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Scrolls the list to the provided position
     * 
     * @param position The position to place on top.
     */
    public void scrollToTop(int position) {
        if (Build.VERSION.SDK_INT >= 11) {
            smoothScrollToPositionFromTop(position, 0);
        } else if (Build.VERSION.SDK_INT >= 8) {
            // TODO get this to work on gingerbread
        }
    }
}
