package com.example.socialchat;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class Messenger extends Activity implements ServiceConnection{

	private Context mContext;
	private MessageService mService;
	private MessageAdapter adapter;
	ListView lv;

	String reciverName;
	String senderName = User.getInstance().getUserName();
	public void setReciverName(String reciverName) {
		this.reciverName = reciverName;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messenger);

		Intent intent = getIntent();
		String reciverName = intent.getStringExtra("reciverName");
		setReciverName(reciverName);
		
		Intent serviceIntent = new Intent(getApplicationContext(), MessageService.class);
		startService(serviceIntent);

		bindService(serviceIntent, this, 0);

		adapter = new MessageAdapter(getApplicationContext());
		lv = (ListView) findViewById(R.id.listView1);
		lv.setAdapter(adapter);
		
		MyMessage.getInstance().getVector().clear();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Messages");
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
						
						update();
					}
				}
			}
		});
	}

	public void sendMessage(View v) {
		
		EditText et2 = (EditText) findViewById(R.id.editText2);
		ParseObject mess = new ParseObject("Messages");

		mess.put("senderName", senderName);
		mess.put("reciverName", reciverName );
		mess.put("message", et2.getText().toString());
		mess.put("state", false);
		mess.saveInBackground(new SaveCallback() {

			@Override
			public void done(com.parse.ParseException arg0) {
				if (arg0 == null) {
					EditText et2 = (EditText) findViewById(R.id.editText2);
					et2.setText("");
					Toast.makeText(getApplicationContext(), "Message send", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		MessageBinder binder = (MessageBinder) service;
		mService = binder.getmService();
		mService.setmActivity(this);

	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		unbindService(this);
	}

	
	public void update() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				lv.invalidateViews();
			}
		});

	}
}