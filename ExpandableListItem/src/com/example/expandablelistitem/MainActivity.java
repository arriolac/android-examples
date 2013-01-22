
package com.example.expandablelistitem;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnItemClickListener {
    
    private ListView mList;
    private ExpandableItemAdapter mAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    private void initViews() {

        int numItems = 50;

        ExpandableItem[] items = new ExpandableItem[numItems];
        for (int i = 0; i < numItems; i++) {
            items[i] = new ExpandableItem(i);
        }
        
        mAdapter = new ExpandableItemAdapter(this, R.layout.list_item, items);
        mList = (ListView) findViewById(R.id.list_view);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        
        mList.setSelection(position);
        
        ExpandableItemTag tag = (ExpandableItemTag) view.getTag();
        if (tag.isExpanded) {
            tag.collapse();
        } else {
            tag.expand();
            mAdapter.expand(position, tag);
        }
    }

    private static class ExpandableItemAdapter extends ArrayAdapter<ExpandableItem> {

        private LayoutInflater mInflater;
        private int mExpandedPosition = -1;
        private ExpandableItemTag mExpandedTag;

        public ExpandableItemAdapter(Context context, int resource, ExpandableItem[] objects) {
            super(context, resource, objects);

            mInflater = LayoutInflater.from(context);
        }
        
        public void expand(int position, ExpandableItemTag tag) {
            
            if (position == mExpandedPosition) {
                return;
            }
            
            if (mExpandedTag != null && mExpandedPosition > position) {
                mExpandedTag.collapse();
            }
            
            mExpandedPosition = position;
            mExpandedTag = tag;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ExpandableItemTag tag;

            // set tag
            if (convertView == null) {
                view = mInflater.inflate(R.layout.list_item, null);
                tag = new ExpandableItemTag(getContext(), view);
            } else {
                view = convertView;
                tag = (ExpandableItemTag) view.getTag();
            }
            tag.setItem(getItem(position), false);
            view.setTag(tag);
            
            // only expand one
            if (mExpandedPosition == position) {
                tag.expand();
            } else {
                tag.collapse();
            }
            
            return view;
        }

    }

    private static class ExpandableItem {

        public int number;

        public ExpandableItem(int number) {
            this.number = number;
        }

        @Override
        public String toString() {
            return "Number " + this.number;
        }
    }

    private static class ExpandableItemTag {

        public static final int CHILD_ID = 100;
        
        private Context mContext;
        private TextView mTitle;
        private ViewGroup mContainer;
        
        private boolean isExpanded = false;
        
        public ExpandableItemTag(Context context, View root) {
            mContext = context;
            mTitle = (TextView) root.findViewById(R.id.title);
            mContainer = (ViewGroup) root.findViewById(R.id.container);
        }

        public void setItem(ExpandableItem item, boolean expand) {
            mTitle.setText(item.toString());
        }

        public boolean isExpanded() {
            return isExpanded;
        }
        
        public void expand() {
            if (isExpanded) {
                return;
            }
           
            for (int j = 0; j < 10; j++) {
                mContainer.addView(createChildView(mContext, "child view"));
            }
            isExpanded = true;
        }
        
        public void collapse() {
            TextView title = (TextView) mContainer.getChildAt(0);
            mContainer.removeAllViews();
            mContainer.addView(title);
            
            isExpanded = false;
        }

        public static TextView createChildView(Context context, String text) {
            TextView textView = new TextView(context);
            textView.setId(CHILD_ID);
            textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
            textView.setPadding(50, 20, 0, 20);
            textView.setText(text);
            textView.setTextSize(16.0f);
            textView.setTextColor(context.getResources().getColor(android.R.color.holo_blue_dark));
            return textView;
        }
    }

}
