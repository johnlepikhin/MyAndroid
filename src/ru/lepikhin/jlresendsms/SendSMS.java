package ru.lepikhin.jlresendsms;

import java.util.ArrayList;

import android.content.Context;
import android.telephony.SmsManager;
import android.util.Log;

public class SendSMS implements Retriable {

	public int type () {
		return 2;
	}

    public void send(Context context, int id, String rcpt, String data) {
		try {
		    SmsManager smsManager = SmsManager.getDefault();
		    ArrayList<String> parts = smsManager.divideMessage(data); 
		    Log.i("zzz SendSMS", "Send SMS to " + rcpt);
		    smsManager.sendMultipartTextMessage(rcpt, null, parts, null, null);
			Static q = new Static (context);
        	q.Remove(id);
		} catch (Exception e) {
		}
	}
}
