package com.example.socialchat;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UsersAdapter extends BaseAdapter {

	private Context mContext;
	
	UsersAdapter(Context mContext) {
		super();
		this.mContext = mContext;
	}

	public UsersAdapter() {
	}

	@Override
	public int getCount() {
		return Users.getInstance().getVector().size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout rl;

		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		rl = (LinearLayout) inflater.inflate(R.layout.users, parent, false);

		TextView userName = (TextView) rl.findViewById(R.id.textViewUser);
		userName.setTextColor(Color.BLACK);
		userName.setText(Users.getInstance().getVector().get(position).getUserName());

		return rl;
	}

}
