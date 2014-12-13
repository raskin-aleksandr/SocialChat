package com.example.socialchat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends Activity {

	ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ImageView iv = (ImageView) findViewById(R.id.imageView1);
		iv.setImageResource(R.drawable.index);

		ParseUser currentUser = ParseUser.getCurrentUser();

		if (currentUser != null) {

			SharedPreferences sp = getSharedPreferences("socialchat",
					MODE_PRIVATE);
			User.getInstance().setUserID(sp.getString("userID", null));
			User.getInstance().setLocationID(sp.getString("locatoinID", null));
			System.out.println("from shared on create: loaded");

			User.getInstance().setmUser(currentUser);
			startActivity(new Intent(getApplicationContext(), Welcome.class));
		}

	}

	public void login(View v) {
		final Dialog loginDialog = new Dialog(this);
		loginDialog.setContentView(R.layout.login);
		loginDialog.setTitle("Login");

//		EditText login = (EditText)findViewById(R.id.loginName);
//		EditText password = (EditText)findViewById(R.id.loginPassword);
//		
//		Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf"); 
//		login.setTypeface(type);
		
		Button loginButton = (Button) loginDialog.findViewById(R.id.login);
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText name = (EditText) loginDialog
						.findViewById(R.id.loginName);
				EditText password = (EditText) loginDialog
						.findViewById(R.id.loginPassword);

				pd = new ProgressDialog(MainActivity.this);
				pd.setTitle("Login");
				pd.setMessage("Loggin in..");
				pd.show();

				ParseUser.logInInBackground(name.getText().toString(), password
						.getText().toString(), new LogInCallback() {

					@Override
					public void done(ParseUser arg0, ParseException e) {
						if (e == null) {
							pd.cancel();
							System.out.println("from login: user id:	 "
									+ arg0.getObjectId());
							System.out.println("from login: location id: "
									+ arg0.get("locationID"));
							User.getInstance().setLocationID(
									(arg0.get("locationID")).toString());
							startActivity(new Intent(getApplicationContext(),
									Welcome.class));
						} else {
							pd.cancel();
							Toast.makeText(getApplicationContext(),
									e.getMessage(), Toast.LENGTH_LONG).show();
						}
					}
				});

			}
		});
		loginDialog.show();
	}

	public void createNew(View v) {
		startActivity(new Intent(getApplicationContext(), SignUp.class));
	}

	@Override
	public void onBackPressed() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		alertDialogBuilder.setTitle("Exit Application?");

		alertDialogBuilder.setMessage("Click yes to exit!");
		alertDialogBuilder.setCancelable(false);
		alertDialogBuilder.setIcon(R.drawable.ic_launcher);
		alertDialogBuilder.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						
						MainActivity.this.finish();
					}
				});
		alertDialogBuilder.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();
	}

	@Override
	protected void onStop() {
		SharedPreferences sp = getSharedPreferences("socialchat", MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();

		editor.putString("userID", User.getInstance().getUserID());
		editor.putString("locatoinID", User.getInstance().getLocationID());
		System.out.println("from shared on stop: saved");
		editor.commit();

		super.onStop();
	}
}
