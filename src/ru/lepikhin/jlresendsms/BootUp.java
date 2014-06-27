package ru.lepikhin.jlresendsms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootUp extends BroadcastReceiver {
	private Context c;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		c = context;
	}
}