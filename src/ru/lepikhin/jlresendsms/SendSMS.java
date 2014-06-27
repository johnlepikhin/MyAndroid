package ru.lepikhin.jlresendsms;

import java.util.ArrayList;

import android.telephony.SmsManager;
import android.util.Log;

public class SendSMS implements Retriable {
	String phone;
	String message;

	public void retry () {
		try {
		    SmsManager smsManager = SmsManager.getDefault();
		    ArrayList<String> parts = smsManager.divideMessage(message); 
		    Log.i("zzz SendSMS", "Send SMS to " + phone);
		    smsManager.sendMultipartTextMessage(phone, null, parts, null, null);
		} catch (Exception e) {
			Static.Add(this);
		}
	}
	
	public void sendLongSMS(String phone, String message) {
		this.phone = phone;
		this.message = message;
		Static.Add(this);
	}
}
