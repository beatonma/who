package com.beatonma.who;

import android.util.Log;

import java.util.Date;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class Mail extends javax.mail.Authenticator {
    private final static String TAG = "Mail";
    private String user;
    private String pass;

    private String[] toAddress;
    private String fromAddress;

    private String port;
    private String sPort;

    private String host;

    private String subject;
    private String body;

    private boolean auth;

    private boolean debuggable;

    private Multipart multipart;


    public Mail() {
        host = "smtp.gmail.com"; // default smtp server
        port = "465"; // default smtp port
        sPort = "465"; // default socketfactory port

        user = ""; // username
        pass = ""; // password
        fromAddress = ""; // email sent from
        subject = ""; // email subject
        body = ""; // email body

        debuggable = false; // debug mode on or off - default off
        auth = true; // smtp authentication - default on

        multipart = new MimeMultipart();

        // There is something wrong with MailCap, javamail can not find a handler for the multipart/mixed part, so this bit needs to be added.
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);
    }

    public Mail(String user, String pass) {
        this();

        this.user = user;
        this.pass = pass;

        if (user.contains("@gmail.com")) {
            this.fromAddress = user;
        }
        else {
            this.fromAddress = user + "@gmail.com";
        }
    }

    public boolean send() throws Exception {
        Properties props = setProperties();

//        if(!user.equals("") && !pass.equals("") && toAddress.length > 0 && !fromAddress.equals("") && !subject.equals("") && !body.equals("")) {
        if (validate()) {
            Session session = Session.getInstance(props, this);

            MimeMessage msg = new MimeMessage(session);

            msg.setFrom(new InternetAddress(fromAddress));

            InternetAddress[] addressTo = new InternetAddress[toAddress.length];
            for (int i = 0; i < toAddress.length; i++) {
                addressTo[i] = new InternetAddress(toAddress[i]);
            }
            msg.setRecipients(MimeMessage.RecipientType.TO, addressTo);

            msg.setSubject(subject);
            msg.setSentDate(new Date());

            // setup message body
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);
            multipart.addBodyPart(messageBodyPart);

            // Put parts in message
            msg.setContent(multipart);

            // send email
            Transport.send(msg);

            return true;
        } else {
            Log.d(TAG, "Failed validation!");
            return false;
        }
    }

    private boolean validate() {
        if (user.equals("")) {
            Log.e(TAG, "Validation failed: empty user");
            return false;
        }
        else if (pass.equals("")) {
            Log.e(TAG, "Validation failed: empty pass");
            return false;
        }
        else if (toAddress.equals("")) {
            Log.e(TAG, "Validation failed: empty toAddress");
            return false;
        }
        else if (fromAddress.equals("")) {
            Log.e(TAG, "Validation failed: empty fromAddress");
            return false;
        }
        else if (body.equals("")) {
            Log.e(TAG, "Validation failed: empty body");
            return false;
        }
        else {
            Log.d(TAG, "Validation passed!");
            return true;
        }
    }

    public void addAttachment(String filename) throws Exception {
        BodyPart messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);

        multipart.addBodyPart(messageBodyPart);
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, pass);
    }

    private Properties setProperties() {
        Properties props = new Properties();

        props.put("mail.smtp.host", host);

        if(debuggable) {
            props.put("mail.debug", "true");
        }

        if(auth) {
            props.put("mail.smtp.auth", "true");
        }

        props.put("mail.smtp.port", port);
        props.put("mail.smtp.socketFactory.port", sPort);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        return props;
    }

    // the getters and setters
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setToAddress(String[] address) {
        this.toAddress = address;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}

