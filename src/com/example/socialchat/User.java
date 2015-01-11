package com.example.socialchat;

import com.parse.ParseUser;

public class User {

	public static User getInstance() {
		if (instance == null) {
			instance = new User();
		}
		return instance;
	}

	public static User instance = null;

	private User() {

	}

	private ParseUser mUser;

	public ParseUser getmUser() {
		return mUser;
	}

	public void setmUser(ParseUser mUser) {
		this.mUser = mUser;
	}

	private String userID;
	private String LocationID;
	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLocationID() {
		return LocationID;
	}

	public void setLocationID(String locationID) {
		LocationID = locationID;
	}

	public String getUserID() {
//		System.out.println("from user class: returned id: " + userID);
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
//		System.out.println("from user class: id: " + this.userID);
	}

	public static void setInstance(User instance) {
		User.instance = instance;
	}
}
