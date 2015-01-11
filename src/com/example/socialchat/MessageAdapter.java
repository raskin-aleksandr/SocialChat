package com.example.socialchat;

import java.util.Vector;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.provider.CalendarContract.Colors;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MessageAdapter extends BaseAdapter {

	private Context mContext;
	private Vector<MyMessage> m;

	MessageAdapter(Context mContext) {
		super();
		this.mContext = mContext;
	}

	public MessageAdapter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() {
		return MyMessage.getInstance().getVector().size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout rl;

		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		rl = (LinearLayout) inflater.inflate(R.layout.message, parent, false);
		TextView senderName = (TextView) rl.findViewById(R.id.textView1);
		TextView message = (TextView) rl.findViewById(R.id.textView2);
		
		senderName.setTextColor(Color.RED);
		senderName.setText(MyMessage.getInstance().getVector().get(position).getSenderName());
		
		message.setTextColor(Color.BLACK);
		message.setText(MyMessage.getInstance().getVector().get(position).getmMessage());

		return rl;
	}

}
