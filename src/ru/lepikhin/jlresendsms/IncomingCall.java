package ru.lepikhin.jlresendsms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class IncomingCall extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if (null == XMPPStatic.phoneListener) {
			XMPPStatic.phoneListener = new PhoneListener();
			XMPPStatic.phoneListener.context = context;
			TelephonyManager telephony = (TelephonyManager) context
					.getSystemService(android.content.Context.TELEPHONY_SERVICE);
			telephony.listen(XMPPStatic.phoneListener,
					PhoneStateListener.LISTEN_CALL_STATE);
		}
	}

}
