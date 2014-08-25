package ru.lepikhin.jlresendsms;

import android.content.Context;

public interface Retriable {
	
	int type ();
	
	void send (Context context, int id, String rcpt, String data);
}
