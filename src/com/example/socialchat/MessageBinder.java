package com.example.socialchat;

import android.os.Binder;

public class MessageBinder extends Binder{
	public MessageService mService;
	
	public MessageBinder(MessageService mService) {
		super();
		this.mService = mService;
	}

	public MessageService getmService() {
		return mService;
	}

	public void MessageBinder(MessageService mService) {
		this.mService = mService;
	}

}
