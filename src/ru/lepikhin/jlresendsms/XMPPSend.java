package ru.lepikhin.jlresendsms;

import java.io.File;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import android.content.Context;
import android.os.Build;
import android.util.Log;

public class XMPPSend implements Retriable {
	private static Context context;

	public int type () {
		return 3;
	}
	
    public void send(Context context, int id, final String rcpt, String data) {
		XMPPConnection.DEBUG_ENABLED = true;
		XMPPSend.context = context;
		Static q = new Static (context);
		
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
//						SendSMS smsSender = new SendSMS();
						Log.i("zzz XMPPSend", "got incoming message, processMessage 2");
//						smsSender.send(0, rcpt, message.getBody());
						
						// TODO (???)
						Static q = new Static (XMPPSend.context);
//						q.Add(2, XMPPSend.sender, message.getBody());
						try {
							q.ProcessQueue(1000);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				    }
				});
			} catch (IllegalStateException e) {
				Log.i("zzz XMPPSend", "connect/login/createChat failed: " + e.getLocalizedMessage());
			}

			if (null != XMPPStatic.chat) {
//				Log.i("zzz XMPPSend", "send message (sleep): " + message);
//				Thread.sleep(15000);
				Log.i("zzz XMPPSend", "send message: " + data);
				try {
					XMPPStatic.chat.sendMessage(data);
		        	q.Remove(id);
				} catch (XMPPException e) {
					Log.e("zzz XMPPSend", "GOT EXCEPTION in sendMessage");
		        	try {
						q.ProcessQueue(15*60*1000);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			Log.e("zzz XMPPSend", "GOT EXCEPTION");
			e.printStackTrace();
		}
	}	
}
