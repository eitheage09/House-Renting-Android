package com.example.assignment.util;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

public class SimpleEmail {

    private String to = "";
    private String subject = "";
    private String content = "";
    private boolean isHtml = false;

    private static final String username = "xinjunwong0602@gmail.com";
    private static final String password = "hpxnaqbnxzradwbj";
    private static final String personal = "重置密码";

    private static final String host = "smtp.gmail.com";
    private static final String port = "587";

    private final String from = personal + "<" + username + ">";
    private MimeMessage message;

    public SimpleEmail() {}

    public SimpleEmail to(String to) { this.to = to; return this; }

    public SimpleEmail subject(String subject) { this.subject = subject; return this; }

    public SimpleEmail content(String content) { this.content = content; return this; }

    public SimpleEmail isHtml(boolean isHtml) { this.isHtml = isHtml; return this; }

    private MimeMessage getMessage() {
        if (message == null) {
            Properties prop = new Properties();
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.starttls.enable", "true");
            prop.put("mail.smtp.host", host);
            prop.put("mail.smtp.port", port);
            prop.put("mail.smtp.ssl.trust", host);
            prop.put("mail.smtp.ssl.protocols", "TLSv1.2");

            Authenticator auth = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            };

            Session sess = Session.getDefaultInstance(prop, auth);
            message = new MimeMessage(sess);
        }
        return message;
    }

    public void send(final Runnable callback) {
        String type = isHtml ? "text/html;charset=utf-8" : "text/plain;charset=utf-8";

        try {
            MimeMessage msg = getMessage();
            msg.setFrom(from);
            msg.setRecipients(Message.RecipientType.TO, to);
            msg.setSubject(subject);
            msg.setContent(content, type);

            new Thread(() -> {
                try {
                    Transport.send(msg);
                    if (callback != null) {
                        callback.run();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
