package com.example.socialchat;

import java.util.Vector;

public class MyMessage {

	private static MyMessage instance = null;

	private Vector<MyMessage> mVector;

	public static MyMessage getInstance() {
		if (instance == null) {
			instance = new MyMessage();
		}
		return instance;
	}

	private MyMessage() {
		mVector = new Vector<MyMessage>();
	}

	public Vector<MyMessage> getVector() {
		return mVector;
	}

	String senderName = "raskin";
	String reciverName;
	String mMessage;

	public String getSenderName() {
		return senderName;
	}

	public String getReciverName() {
		return reciverName;
	}

	public String getmMessage() {
		return mMessage;
	}

	 public MyMessage(String senderName, String reciverName, String mMessage)
	 {
	 super();
	 this.senderName = senderName;
	 this.reciverName = reciverName;
	 this.mMessage = mMessage;
	 }

}
