
Config.java && AuthData.java are required to compile.

Config.java example:
======================================================
package ru.lepikhin.jlresendsms;

final public class Config {
	static public String send_mail_to = "some-user@domain.com";

	static public String send_sms_to = "+7223322223";
	static public String[] send_sms_from = {"Alfa-Bank"; "Megafon"};
	
	static public String xmpp_send_to = "some-user@jabber.org";
}
======================================================

AuthData.java example:
======================================================
package ru.lepikhin.jlresendsms;

final public class AuthData {
	static public String gmail_username = "some-user@gmail.com";
	static public String gmail_password = "mysecret";
	
	static public String xmpp_server = "jabber.rueckgr.at";
	static public String xmpp_username = "android-robot";
	static public String xmpp_password = "mysecret";
}
======================================================
