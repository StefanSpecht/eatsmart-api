package dom.company.eatsmart.service;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.core.UriInfo;

import dom.company.eatsmart.model.User;

public class MailService {

	private static final String SMTP_USER = "stefan_specht@gmx.net";
	private static final String SMTP_PASSWORD = "71388A4557!";
	private static final String SMTP_HOST = "mail.gmx.net";
	private static final String SMTP_FROM = "stefan_specht@gmx.net";
	private static final String REGISTRATION_VERIFICATION_PATH = "verification/registration/";
	private static final String RESET_VERIFICATION_PATH = "verification/pwdResetRequest/";
	
	MailAuthenticator mailAuthenticator = new MailAuthenticator(SMTP_USER, SMTP_PASSWORD);
	
	public void sendRegistrationMail(User user, String token, UriInfo uriInfo) {
				
	    String to = user.getEmail();
	    String from = SMTP_FROM;
	    String host = SMTP_HOST;
	    String subject = "Willkommen bei EatSmart!";
	    String uri = uriInfo.getBaseUriBuilder()
	    		.path(REGISTRATION_VERIFICATION_PATH + token)
	    		.build()
	    		.toString();
	    String htmlBody = String.format("<h1>Hallo %s,</h1>"
	    		+ "<p>danke für deine Registrierung bei EatSmart! </p>"
	    		+ "<p>Zur Bestätigung der Registrierung, klicke bitte auf folgenden Link: "
	    		+ "<a href=\"" + uri + "\">Registrierung bestätigen</a> </p>"
	    		+ "<p>...oder kopiere diese URL in deinen Browser: <br>" + uri +" </p><br>"
	    		+ "Viel Spaß wünscht <br>"
	    		+ " - Das EatSmart-Team - ", user.getUsername());
	    /*
	    InternetHeaders headers = new InternetHeaders();
	    headers.addHeader("Content-type", "text/html; charset=UTF-8");
	    
	    MimeBodyPart mimeBody = new MimeBodyPart(headers, htmlBody.getBytes("UTF-8"));
	    */
	    Properties properties = new Properties();
	    properties.put("mail.smtp.host", host);
	    properties.put("mail.smtp.auth", "true");
	    properties.put("mail.smtp.socketFactory.port", "465");
	    properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	    
	    Session session = Session.getDefaultInstance(properties, mailAuthenticator);
	    
	    try {
	    	MimeMessage message = new MimeMessage(session);
	    	message.setFrom(new InternetAddress(from));
	    	message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
	    	message.setSubject(subject);
	    	message.setText(htmlBody,"UTF-8","html");

	    	Transport.send(message);
	        
	      	}catch (MessagingException ex) {
	         ex.printStackTrace();
	      	}
	}
	
public void sendPwdResetMail(User user, String token, UriInfo uriInfo) {
		
	    String to = user.getEmail();
	    String from = SMTP_FROM;
	    String host = SMTP_HOST;
	    String subject = "Passwort vergessen?";
	    String uri = uriInfo.getBaseUriBuilder()
	    		.path(RESET_VERIFICATION_PATH + token)
	    		.build()
	    		.toString();
	    String htmlBody = String.format("<h1>Hallo %s,</h1>"
	    		+ "<p>bitte bestätige das Zurücksetzen deines Passworts durch einen Klick auf folgenden Link: <br>"
	    		+ "<a href=\"" + uri + "\">Passwort zurücksetzen</a> </p>"
	    		+ "<p>...oder kopiere diese URL in deinen Browser: <br>" + uri +" </p><br>"
	    		+ "Liebe Grüße <br>"
	    		+ " - Das EatSmart-Team - ", user.getUsername());
	    /*
	    InternetHeaders headers = new InternetHeaders();
	    headers.addHeader("Content-type", "text/html; charset=UTF-8");
	    
	    MimeBodyPart mimeBody = new MimeBodyPart(headers, htmlBody.getBytes("UTF-8"));
	    */
	    Properties properties = new Properties();
	    properties.put("mail.smtp.host", host);
	    properties.put("mail.smtp.auth", "true");
	    properties.put("mail.smtp.socketFactory.port", "465");
	    properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	    
	    Session session = Session.getDefaultInstance(properties, mailAuthenticator);
	    
	    try {
	    	MimeMessage message = new MimeMessage(session);
	    	message.setFrom(new InternetAddress(from));
	    	message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
	    	message.setSubject(subject);
	    	message.setText(htmlBody,"UTF-8","html");

	    	Transport.send(message);
	        
	      	}catch (MessagingException ex) {
	         ex.printStackTrace();
	      	}
	}

public void sendNewPwdMail(User user) {
	
    String to = user.getEmail();
    String from = SMTP_FROM;
    String host = SMTP_HOST;
    String subject = "Dein neues Passwort";
    
    String htmlBody = String.format("<h1>Hallo %s,</h1>"
    		+ "<p>dein neues Passwort lautet: %s </p>"
    		+ "Liebe Grüße <br>"
    		+ " - Das EatSmart-Team - ", user.getUsername(), user.getPassword());
    /*
    InternetHeaders headers = new InternetHeaders();
    headers.addHeader("Content-type", "text/html; charset=UTF-8");
    
    MimeBodyPart mimeBody = new MimeBodyPart(headers, htmlBody.getBytes("UTF-8"));
    */
    Properties properties = new Properties();
    properties.put("mail.smtp.host", host);
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.socketFactory.port", "465");
    properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    
    Session session = Session.getDefaultInstance(properties, mailAuthenticator);
    
    try {
    	MimeMessage message = new MimeMessage(session);
    	message.setFrom(new InternetAddress(from));
    	message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
    	message.setSubject(subject);
    	message.setText(htmlBody,"UTF-8","html");

    	Transport.send(message);
        
      	}catch (MessagingException ex) {
         ex.printStackTrace();
      	}
}
}
