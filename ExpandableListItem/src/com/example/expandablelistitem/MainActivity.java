
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
        
        int numItems = 20;
        
        ExpandableItem[] items = new ExpandableItem[numItems];
        for (int i = 0; i < numItems; i++) {
            items[i] = new ExpandableItem(i);
        }
        ExpandableItemAdapter adapter = new ExpandableItemAdapter(this, R.layout.list_item, items);
        ListView list = (ListView) findViewById(R.id.list_view);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ExpandableItemTag tag = (ExpandableItemTag) view.getTag();
        tag.expand();
    }
    
    private static class ExpandableItemAdapter extends ArrayAdapter<ExpandableItem> {
        
        private LayoutInflater mInflater;
        
        public ExpandableItemAdapter(Context context, int resource, ExpandableItem[] objects) {
            super(context, resource, objects);
            
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ExpandableItemTag tag;
            
            if (convertView == null) {
                view = mInflater.inflate(R.layout.list_item, null);
                tag = new ExpandableItemTag(getContext(), view);
            } else {
                view = convertView;
                tag = (ExpandableItemTag) view.getTag();
            }
            
            tag.setItem(getItem(position), false);
            view.setTag(tag);
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
        
        private Context mContext;
        private TextView mTitle;
        private ViewGroup mContainer;
        
        public ExpandableItemTag(Context context, View root) {
            mContext = context;
            mTitle = (TextView) root.findViewById(R.id.title);
            mContainer = (ViewGroup) root.findViewById(R.id.container);
        }
        
        public void setItem(ExpandableItem item, boolean expand) {
            mTitle.setText(item.toString());
        }
        
        public void expand() {
            mContainer.addView(createChildView(mContext, "child view"));
        }
        
        public static TextView createChildView(Context context, String text) {
            TextView textView = new TextView(context);
            textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            textView.setPadding(50, 20, 0, 20);
            textView.setText(text);
            //textView.setTextSize(size)
            return textView;
        }
    }

}
