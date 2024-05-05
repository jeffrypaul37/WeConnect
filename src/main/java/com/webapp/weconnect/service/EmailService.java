package com.webapp.weconnect.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * To send an email to the user once the registration is successful.
     * This is done because this way the email address can be verified as well as it can be verified that it is indeed a real user.
     * @param to the receiver's email
     * @param subject the heading of mail
     * @param content the body of the email
     * */
    public void sendEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("a541579150@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }
}
