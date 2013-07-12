package com.tudor.ctm.ui.server;

import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailNotification {

	String emailSubject;
	String emailBody;
	InternetAddress from;
	InternetAddress to;

	public EmailNotification() throws UnsupportedEncodingException {
		this.from = new InternetAddress(
				"notification@ctm-tudor.appspotmail.com",
				"CTM Tudor Notification");

	}

	public void send() throws Exception {
		try {
			Properties props = new Properties();
			Session session = Session.getDefaultInstance(props, null);

			Message msg = new MimeMessage(session);
			msg.setFrom(from);
			msg.addRecipient(Message.RecipientType.TO, to);
			msg.setSubject(emailSubject);
			/* Set the type to HTML and charset to utf-8 */
			msg.setContent(emailBody, "text/html; charset=utf-8");
			Transport.send(msg);
		} catch (Exception e) {
			throw e;
		}
	}

	public static class Builder {
		private String emailSubject;
		private String emailBody;
		private InternetAddress to;

		public Builder emailSubject(String emailSubject) {
			this.emailSubject = emailSubject;
			return this;
		}

		public Builder emailBody(String emailBody) {
			this.emailBody = emailBody;
			return this;
		}

		public Builder to(InternetAddress to) {
			this.to = to;
			return this;
		}

		public EmailNotification build() throws UnsupportedEncodingException {
			return new EmailNotification(this);
		}
	}

	private EmailNotification(Builder builder) throws UnsupportedEncodingException {
		this();
		this.emailSubject = builder.emailSubject;
		this.emailBody = builder.emailBody;
		this.to = builder.to;
	}
}
