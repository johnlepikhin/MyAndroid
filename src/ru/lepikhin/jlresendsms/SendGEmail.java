package ru.lepikhin.jlresendsms;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendGEmail implements Retriable {
	String email, subject, messageBody;

    public void sendMail(String email, String subject, String messageBody) {
    	Log.i("zzz sendMail", "1");
    	this.email = email;
    	this.subject = subject;
    	this.messageBody = messageBody;
    	Log.i("zzz sendMail", "2");
		Static.Add(this);
    }
    
    public void retry () {
        Session session = createSessionObject();

        try {
            Message message = createMessage(email, subject, messageBody, session);
        	Log.i("zzz SendGEmail", "send email to " + email + ", subject=" + subject);
            Transport.send(message);
        	Log.i("zzz SendGEmail", "OK, sent email to " + email + ", subject=" + subject);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
			Log.e("zzz SendGEmail", "GOT EXCEPTION");
			Static.Add(this);
        }
    }

    private Message createMessage(String email, String subject, String messageBody, Session session) throws MessagingException, UnsupportedEncodingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("tutorials@tiemenschut.com", "Tiemen Schut"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email, email));
        message.setSubject(subject);
        message.setText(messageBody);
        return message;
    }

    private Session createSessionObject() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        return Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(AuthData.gmail_username, AuthData.gmail_password);
            }
        });
    }
}