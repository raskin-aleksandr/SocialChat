package com.example.socialchat;

import android.os.Binder;

public class UsersBinder extends Binder {
	private UsersService mService;

	public UsersBinder(UsersService mService) {
		super();
		this.mService = mService;
	}

	public UsersService getmService() {
		return mService;
	}

	public void setmService(UsersService mService) {
		this.mService = mService;
	}

}