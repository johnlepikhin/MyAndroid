package ru.lepikhin.jlresendsms;

import java.util.ArrayList;

import android.telephony.SmsManager;

public class SendSMS {
	public void sendLongSMS(String phone, String message) {
	    SmsManager smsManager = SmsManager.getDefault();
	    ArrayList<String> parts = smsManager.divideMessage(message); 
	    smsManager.sendMultipartTextMessage(phone, null, parts, null, null);
	}
}
