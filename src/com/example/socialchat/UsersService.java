package com.example.socialchat;

import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

public class UsersService extends Service implements Runnable {

	private UsersActivity mActivity;

	public UsersActivity getmActivity() {
		return mActivity;
	}

	public void setmActivity(UsersActivity mActivity) {
		this.mActivity = mActivity;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return new UsersBinder(this);
	}

	@Override
	public void run() {
		while (true) {
			ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
			query.whereNotEqualTo("username", User.getInstance().getInstance().getUserName());

			query.findInBackground(new FindCallback<ParseObject>() {

				@Override
				public void done(List<ParseObject> usersList, ParseException e) {
					if (e == null) {
						Users.getInstance().getVector().clear();
						for (ParseObject parseobject : usersList) {
							Users usrs = new Users(parseobject.getString("username"));
							Users.getInstance().getVector().add(usrs);
							mActivity.update();
						}
					} else {
						System.out.println("from service: " + e.getMessage());
					}

				}

			});

			SystemClock.sleep(5000);
		}
	}

	@Override
	public void onCreate() {
		Thread t1 = new Thread(this);
		t1.start();
		super.onCreate();
	}

}
