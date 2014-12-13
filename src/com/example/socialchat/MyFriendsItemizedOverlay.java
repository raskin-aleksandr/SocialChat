package com.example.socialchat;

import java.util.List;

import org.json.JSONObject;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SendCallback;

public class MyFriendsItemizedOverlay extends ItemizedIconOverlay<OverlayItem> {

	protected Context mContext;

	public MyFriendsItemizedOverlay(final Context context, final List<OverlayItem> aList) {
		super(context, aList, new OnItemGestureListener<OverlayItem>() {
			@Override
			public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
				return false;
			}

			@Override
			public boolean onItemLongPress(final int index, final OverlayItem item) {
				return false;
			}
		});
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	@Override
	public boolean addItem(OverlayItem item) {
		// TODO Auto-generated method stub
		return super.addItem(item);
	}

	@Override
	protected boolean onSingleTapUpHelper(final int index, final OverlayItem item, final MapView mapView) {
		AlertDialog.Builder message = new AlertDialog.Builder(mContext);
		message.setTitle("Send to " + item.getTitle());

		final EditText input = new EditText(mContext);
		input.setHint("enter message");
		message.setView(input);

		message.setCancelable(true);
		message.setNegativeButton("Send", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// ParseObject mess = new ParseObject("Messages");
				//
				// mess.put("senderName", User.getInstance().getmUser().getUsername());
				// mess.put("reciverName", item.getTitle());
				// mess.put("message", input.getText().toString());
				// mess.put("state", false);
				// mess.saveInBackground(new SaveCallback() {
				//
				// @Override
				// public void done(ParseException e) {
				// if (e == null) {
				//
				// Toast.makeText(mContext, "Message send", Toast.LENGTH_SHORT).show();
				// } else {
				//
				// Toast.makeText(mContext, "Something went wrong: " + e.getMessage(), Toast.LENGTH_SHORT).show();
				// }
				// }
				// });

				JSONObject data;
				try {
					data = new JSONObject();
					data.put("action", "com.example.socialchat");
					data.put("name", input.getText().toString());
					ParsePush push = new ParsePush();
					push.setChannel("A" + User.getInstance().getmUser().getObjectId());
					push.setData(data);
					push.sendInBackground(new SendCallback() {

						@Override
						public void done(ParseException e) {
							if (e == null) {
								Toast.makeText(mContext, "message sended", Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
							}

						}
					});
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		AlertDialog alert = message.create();
		alert.show();
		return true;
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e, MapView mapView) {
		float x = e.getRawX();
		float y = e.getRawY();
		// GeoPoint gp = (GeoPoint) mapView.getProjection().toPixels(x,y);
		return super.onSingleTapUp(e, mapView);
	}

}