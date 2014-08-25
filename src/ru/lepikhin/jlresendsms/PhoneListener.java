package ru.lepikhin.jlresendsms;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class PhoneListener extends PhoneStateListener {
	public Context context;
	
	@Override
	public void onCallStateChanged (int state, String number) {
		if (TelephonyManager.CALL_STATE_RINGING == state) {
			Static q = new Static (context);
			String contactName = new FindContactByNumber().findContactByNumber(context, number);
			q.Add(3, Config.xmpp_send_to, "Incoming call from " + number + " (" + contactName + ")");		
//			new XMPPSend("Incoming call from " + number + " (" + contactName + ")", number);

			try {
				q.ProcessQueue();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}
