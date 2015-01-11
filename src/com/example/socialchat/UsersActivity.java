package com.example.socialchat;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.parse.ParseUser;

public class UsersActivity extends Activity implements ServiceConnection {

	private Context mContext;
	private UsersService uService;
	private UsersAdapter uAdapter;
	ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);

		Intent intent = new Intent(getApplicationContext(), UsersService.class);
		startService(intent);

		bindService(intent, this, 0);

		uAdapter = new UsersAdapter(getApplicationContext());

		lv = (ListView) findViewById(R.id.usersList);
		lv.setAdapter(uAdapter);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(getApplicationContext(), Messenger.class);
				intent.putExtra("reciverName", Users.getInstance().getVector().get(position).getUserName());
				startActivity(intent);
			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.chat_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.map:
			UsersActivity.this.finish();
			break;

		case R.id.logout:
			ParseUser.getCurrentUser().logOut();

			SharedPreferences sp = getSharedPreferences("socialchat", MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();

			editor.putString("userID", null);
			editor.putString("locatoinID", null);
			System.out.println("from logout: cleared");
			editor.commit();

			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intent);

			break;

		case R.id.settings:
			startActivity(new Intent(getApplicationContext(), Settings.class));
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void update() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				lv.invalidateViews();
				
			}
		});

	}

	@Override
	public void onServiceConnected(ComponentName arg0, IBinder service) {
		UsersBinder binder = (UsersBinder) service;
		uService = binder.getmService();
		uService.setmActivity(this);
	}

	@Override
	public void onServiceDisconnected(ComponentName arg0) {
		unbindService(this);
	}

}
