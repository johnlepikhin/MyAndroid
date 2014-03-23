package ru.lepikhin.jlresendsms;

import org.jivesoftware.smack.SmackAndroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootUp extends BroadcastReceiver {
	private Context c;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		c = context;
	}
}