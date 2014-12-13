package com.example.socialchat;

import android.app.Application;

import com.parse.Parse;

public class MyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
	
		Parse.initialize(this, "KMDfO99s3gRrNeSwPULwdwJUIWK1JYRfTK4RmqnD",
				"fc4k3vprG7TpRfHLMgOK3a13fI2gW50zoLeUecyn");
	}
}
