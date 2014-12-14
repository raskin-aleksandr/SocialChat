package com.example.socialchat;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class MyReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("on receive");
		try {
			String action = intent.getAction();
			String channel = intent.getExtras().getString("com.parse.Channel");
			JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

			String name = json.getString("name");

			Intent i = new Intent(context, Welcome.class);
			PendingIntent pIntent = PendingIntent.getActivity(context, 0, i, 0);

			Notification n = new NotificationCompat.Builder(context).setContentTitle("Received message").setContentText(name)
					.setContentIntent(pIntent).setSmallIcon(R.drawable.ic_launcher).build();
			n.flags |= Notification.FLAG_AUTO_CANCEL;

			NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.notify(0, n);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
