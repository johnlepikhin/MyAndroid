package ru.lepikhin.jlresendsms;

import java.util.Arrays;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class IncomingSms extends BroadcastReceiver {
	private String senderNum;
	private String message;
	private String contactName;
	
	
	// Get the object of SmsManager
	final SmsManager sms = SmsManager.getDefault();

	@Override
	public void onReceive(Context context, Intent intent) {
		Static q = new Static (context);
		// Retrieves a map of extended data from the intent.
		final Bundle bundle = intent.getExtras();

		if (intent != null && intent.getAction() != null) {
		    Object[] pduArray = (Object[]) intent.getExtras().get("pdus");
		    SmsMessage[] messages = new SmsMessage[pduArray.length];
		    for (int i = 0; i < pduArray.length; i++) {
		        messages[i] = SmsMessage.createFromPdu((byte[]) pduArray[i]);
		    }
		}
		
		try {

			if (bundle != null) {

				final Object[] pdusObj = (Object[]) bundle.get("pdus");

				for (int i = 0; i < pdusObj.length; i++) {

					SmsMessage currentMessage = SmsMessage
							.createFromPdu((byte[]) pdusObj[i]);
					senderNum = currentMessage
							.getDisplayOriginatingAddress();
					message = currentMessage.getDisplayMessageBody();

					Log.i("zzz SmsReceiver", "senderNum: " + senderNum
							+ "; message: " + message);
					
					contactName = new FindContactByNumber().findContactByNumber(context, senderNum);
					
					Log.i ("zzz SmsReceiver", "1");
					
					// send mail
					SendGEmail mailSender = new SendGEmail();
					Log.i ("zzz SmsReceiver", "2, mailSender=" + mailSender);
					q.Add(1, Config.send_mail_to, "SMS from " + senderNum + " (" + contactName + ")\n\n" + message);
//					mailSender.sendMail(Config.send_mail_to, "SMS from " + senderNum + " (" + contactName + ")", message);
					Log.i ("zzz SmsReceiver", "3");
					
					// send sms
					if (Arrays.asList(Config.send_sms_from).contains(senderNum)) {
						Log.i ("zzz SmsReceiver", "4");
	//					SendSMS smsSender = new SendSMS();
						Log.i ("zzz SmsReceiver", "5");
						q.Add(2, Config.send_sms_to, "SMS from " + senderNum + "\n\n" + message);
//						smsSender.sendLongSMS(Config.send_sms_to, "SMS from " + senderNum + "\n\n" + message);
					}
					
					Log.i ("zzz SmsReceiver", "6");
					// send XMPP
					q.Add(3, Config.xmpp_send_to, "SMS from " + senderNum + " (" + contactName + ")\n\n" + message);
//					new XMPPSend("SMS from " + senderNum + " (" + contactName + ")" + "\n\n" + message, senderNum);
					
					q.ProcessQueue ();
				} // end for loop
			} // bundle is null

		} catch (Exception e) {
			Log.e("SmsReceiver", "Exception smsReceiver" + e);

		}
	}
}
