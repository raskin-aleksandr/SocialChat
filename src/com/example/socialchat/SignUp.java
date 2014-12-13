package com.example.socialchat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class SignUp extends Activity {

	private ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		
		EditText name = (EditText)findViewById(R.id.newName);
		EditText email = (EditText)findViewById(R.id.newEmail);
		EditText pass1 = (EditText)findViewById(R.id.newPassword1);
		EditText pass2 = (EditText)findViewById(R.id.newPassword2);
		
		name.setText("raskin");
		email.setText("raskin.aleksandr@gmail.com");
		pass1.setText("123");
		pass2.setText("123");
	}
	
	public void create (View v){
		EditText name = (EditText)findViewById(R.id.newName);
		EditText email = (EditText)findViewById(R.id.newEmail);
		EditText pass1 = (EditText)findViewById(R.id.newPassword1);
		EditText pass2 = (EditText)findViewById(R.id.newPassword2);

		
		if (pass1.getText().toString().equals(pass2.getText().toString())) {
		
			final ParseUser user = new ParseUser();
			
			user.setUsername(name.getText().toString().toLowerCase());
			user.setEmail(email.getText().toString());
			user.setPassword(pass1.getText().toString());
			
			pd = new ProgressDialog(this);
			pd.setTitle("New account");
			pd.setMessage("Saving..");
			pd.show();
					
			user.signUpInBackground(new SignUpCallback() {
				
				@Override
				public void done(ParseException e) {
					pd.cancel();
					if (e == null) {
						User.getInstance().setmUser(ParseUser.getCurrentUser());
						startActivity(new Intent(getApplicationContext(), Welcome.class));
						Toast.makeText(getApplicationContext(), "account created", Toast.LENGTH_SHORT).show();
					
						final ParseObject po = new ParseObject("location");
						po.put("userID", User.getInstance().getmUser().getObjectId());
						po.put("name", User.getInstance().getmUser().getUsername());
						po.saveInBackground(new SaveCallback() {
							
							@Override
							public void done(ParseException arg0) {
								User.getInstance().setUserID(user.getObjectId());
								User.getInstance().setLocationID(po.getObjectId());
								System.out.println("from sign in: " + po.getObjectId());
								user.put("locationID", po.getObjectId());
								user.saveInBackground(new SaveCallback() {
									
									@Override
									public void done(ParseException e) {
										if (e != null) {
											System.out.println("from location id save: " + e.getMessage());
										}
									}
								});
							}
						});
					}
					else {
						Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
					}
				}
			});
		}
	}
	
	public void cancel (View v){
		finish();
	}
}
