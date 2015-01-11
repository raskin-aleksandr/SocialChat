package com.example.socialchat;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends Activity {

    public static final int HDR_POS1 = 0;
    public static final int HDR_POS2 = 5;
    public static final String[] LIST = {"list1", "list2" };
    public static final String[] SUBTEXTS = { "test", "subtext2"};

    private static final Integer LIST_HEADER = 0;
    private static final Integer LIST_ITEM = 1;

    
    
    
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ListView lv = (ListView)findViewById(R.id.settingsList);
        lv.setAdapter(new MyListAdapter(this));
        
        lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
				
				Toast.makeText(getApplicationContext(), adapter.getId(), Toast.LENGTH_SHORT).show();
				
				
			}
			
		});
        
        
    }

    private class MyListAdapter extends BaseAdapter {
        public MyListAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return LIST.length;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public boolean isEnabled(int position) {
            return true;
        }

        @Override
        public Object getItem(int position) {
        	return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            String headerText = getHeader(position);
            if(headerText != null) {

                View item = convertView;
                if(convertView == null || convertView.getTag() == LIST_ITEM) {

                    item = LayoutInflater.from(mContext).inflate(
                            R.layout.lv_header_layout, parent, false);
                    item.setTag(LIST_HEADER);

                }

                TextView headerTextView = (TextView)item.findViewById(R.id.lv_list_hdr);
                headerTextView.setText(headerText);
                return item;
            }

            View item = convertView;
            if(convertView == null || convertView.getTag() == LIST_HEADER) {
                item = LayoutInflater.from(mContext).inflate(
                        R.layout.lv_layout, parent, false);
                item.setTag(LIST_ITEM);
            }

            TextView header = (TextView)item.findViewById(R.id.lv_item_header);
            header.setText(LIST[position % LIST.length]);

            TextView subtext = (TextView)item.findViewById(R.id.lv_item_subtext);
            subtext.setText(SUBTEXTS[position % SUBTEXTS.length]);

            //Set last divider in a sublist invisible
            View divider = item.findViewById(R.id.item_separator);
            if(position == HDR_POS2 -1) {
                divider.setVisibility(View.INVISIBLE);
            }


            return item;
        }

        private String getHeader(int position) {

            if(position == HDR_POS1  || position == HDR_POS2) {
                return LIST[position];
            }

            return null;
        }

        private final Context mContext;
    }

}
