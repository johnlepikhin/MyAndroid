package ru.lepikhin.jlresendsms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class ServiceStarter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
		final String SOME_ACTION = "android.provider.Telephony.SMS_RECEIVED";
	    IntentFilter intentFilter = new IntentFilter(SOME_ACTION);
	    IncomingSms mReceiver = new IncomingSms();
	    context.registerReceiver(mReceiver, intentFilter);
    }
}