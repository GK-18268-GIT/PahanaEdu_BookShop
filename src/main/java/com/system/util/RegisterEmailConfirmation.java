package com.system.util;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class RegisterEmailConfirmation {
	
	public static void sendEmail(String receiverEmail, String emailGreeting, String emailBody) throws MessagingException  {
		final String adminEmail = "";
		final String adminEmailPassword = "";
		
		Properties properties = new Properties();
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		
		Session session = Session.getInstance(properties, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(adminEmail, adminEmailPassword);
			}
		});
		
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(adminEmail));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverEmail));
		message.setSubject(emailGreeting);
		message.setText(emailBody);
		
		Transport.send(message);
		
	}
}
