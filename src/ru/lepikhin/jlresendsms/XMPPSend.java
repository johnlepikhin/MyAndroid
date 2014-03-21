package ru.lepikhin.jlresendsms;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import android.os.AsyncTask;

public class XMPPSend {
	private String message;
	private String senderNum;
	
	public XMPPSend (String num, String msg) {
		message = msg;
		
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				ConnectionConfiguration config = new ConnectionConfiguration(AuthData.xmpp_server, 5222);
				config.setCompressionEnabled(true);
				config.setSASLAuthenticationEnabled(true);
				
				try {
					if (null == XMPPStatic.connection) {
						XMPPStatic.connection = new XMPPConnection(config);
						XMPPStatic.connection.connect();
						XMPPStatic.connection.login(AuthData.xmpp_username, AuthData.xmpp_password);
					}
					Chat chat = XMPPStatic.connection.getChatManager().createChat(Config.xmpp_send_to, new MessageListener() {

						@Override
					    public void processMessage(Chat chat, Message message) {
							SendSMS smsSender = new SendSMS();
							smsSender.sendLongSMS(senderNum, message.getBody());
					    }
					});
					chat.sendMessage(message);
				} catch (XMPPException e) {
					e.printStackTrace();
				}
				return null;
			}
		}.execute();
	}
}
