package com.example.socialchat;

import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class MessageService extends Service implements Runnable {

	private Messenger mActivity;

	public Messenger getmActivity() {
		return mActivity;
	}

	public void setmActivity(Messenger mActivity) {
		this.mActivity = mActivity;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return new MessageBinder(this);
	}

	@Override
	public void run() {
		while (true) {
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Messages");
			query.whereEqualTo("state", false);
			query.findInBackground(new FindCallback<ParseObject>() {

				@Override
			 	public void done(List<ParseObject> messageList, ParseException e) {

					if (e == null) {
						for (ParseObject parseobject : messageList) {
							MyMessage msg = new MyMessage(parseobject
									.getString("senderName"), parseobject
									.getString("reciverName"), parseobject
									.getString("message"));
							MyMessage.getInstance().getVector().add(0,msg);
							parseobject.put("state", true);
							parseobject.saveInBackground();
							
							mActivity.update();
						}
					}
				}
			});

			SystemClock.sleep(1000);
		}
	}

	@Override
	public void onCreate() {
		Thread t1 = new Thread(this);
		t1.start();
		super.onCreate();
	}

}
