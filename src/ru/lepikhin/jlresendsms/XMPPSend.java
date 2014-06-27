package ru.lepikhin.jlresendsms;

import java.io.File;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import android.os.Build;
import android.util.Log;

public class XMPPSend implements Retriable {
	private String message;
	private String smsSenderPhone;
	
	public void retry () {
		XMPPConnection.DEBUG_ENABLED = true;
		
		ConnectionConfiguration config = new ConnectionConfiguration(AuthData.xmpp_server, 5222);
		config.setCompressionEnabled(true);
		config.setSASLAuthenticationEnabled(true);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
		    config.setTruststoreType("AndroidCAStore");
		    config.setTruststorePassword(null);
		    config.setTruststorePath(null);
		} else {
		    config.setTruststoreType("BKS");
		    String path = System.getProperty("javax.net.ssl.trustStore");
		    if (path == null)
		        path = System.getProperty("java.home") + File.separator + "etc"
		            + File.separator + "security" + File.separator
		            + "cacerts.bks";
		    config.setTruststorePath(path);
		}
		
		try {
			Log.i("zzz XMPPSend", "connect");
//			if (null == XMPPStatic.connection) {
				XMPPStatic.connection = new XMPPConnection(config);
//			}
			try {
				XMPPStatic.connection.connect();
				Log.i("zzz XMPPSend", "login");
				XMPPStatic.connection.login(AuthData.xmpp_username, AuthData.xmpp_password);
				Log.i("zzz XMPPSend", "createChat");
				XMPPStatic.chat = XMPPStatic.connection.getChatManager().createChat(Config.xmpp_send_to, new MessageListener() {
					@Override
				    public void processMessage(Chat chat, Message message) {
						Log.i("zzz XMPPSend", "got incoming message, processMessage 1");
						SendSMS smsSender = new SendSMS();
						Log.i("zzz XMPPSend", "got incoming message, processMessage 2");
						smsSender.sendLongSMS(smsSenderPhone, message.getBody());
						Static.ProcessQueue();
				    }
				});
			} catch (IllegalStateException e) {
				Log.i("zzz XMPPSend", "connect/login/createChat failed: " + e.getLocalizedMessage());
			}

			if (null != XMPPStatic.chat) {
//				Log.i("zzz XMPPSend", "send message (sleep): " + message);
//				Thread.sleep(15000);
				Log.i("zzz XMPPSend", "send message: " + message);
				try {
					XMPPStatic.chat.sendMessage(message);
				} catch (XMPPException e) {
					Log.e("zzz XMPPSend", "GOT EXCEPTION in sendMessage");
					e.printStackTrace();
					Static.Add(XMPPSend.this);
				}
			}
		} catch (Exception e) {
			Log.e("zzz XMPPSend", "GOT EXCEPTION");
			e.printStackTrace();
			Static.Add(this);
		}
	}
	
	public XMPPSend (String msg, String smsSenderPhone) {
		this.message = msg;
		this.smsSenderPhone = smsSenderPhone;
		Static.Add(this);
	}
}
