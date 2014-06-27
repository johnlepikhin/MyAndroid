package ru.lepikhin.jlresendsms;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class PhoneListener extends PhoneStateListener {
	public Context context;
	
	@Override
	public void onCallStateChanged (int state, String number) {
		if (TelephonyManager.CALL_STATE_RINGING == state) {
			String contactName = new FindContactByNumber().findContactByNumber(context, number);
			new XMPPSend("Incoming call from " + number + " (" + contactName + ")", number);
		}
	}
}
