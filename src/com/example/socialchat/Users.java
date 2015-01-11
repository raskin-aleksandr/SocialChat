package com.example.socialchat;

import java.util.Vector;

public class Users {

	private static Users instance = null;

	private Vector<Users> mVector;

	public static Users getInstance() {

		if (instance == null) {
			instance = new Users();
		}

		return instance;
	}

	private Users() {
		mVector = new Vector<Users>();
	}

	public Vector<Users> getVector() {
		return mVector;
	}
	
	private String userName;
	
	public Users (String userName){
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}
}
