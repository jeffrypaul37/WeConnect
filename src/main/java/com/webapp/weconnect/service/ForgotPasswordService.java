package com.webapp.weconnect.service;


import com.webapp.weconnect.model.ForgotPasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.UUID;


/**
 * Class which implements the main logic behind the forgot password feature.
 * Uses the java Mail package for mail delivery to the users for resetting passwords.
 * @Author : Luv Patel
 * */
@Service
public class ForgotPasswordService {

    @Autowired
    JavaMailSender javaMailSender;

    // defining the maximum time in which the link will expire
    private final int MINUTES = 10;

    //Generating the token
    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    // Setting the link expiration time
    public LocalDateTime expireTimeRange() {
        return LocalDateTime.now().plusMinutes(MINUTES);
    }

    /**
     * Method to send email to the user when they request for a password reset link.
     * Because if a user wants to reset password, then we need to ensure that the actual user is making this request rather than a bot or hacker.
     * @param to the receiver's email address
     * @param subject the heading for the email
     * @param emailLink the password reset link using which the user will set new password
     * */
    public void sendEmail(String to, String subject, String emailLink) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimehelper = new MimeMessageHelper(message);

            // The body of the actual message sent to the user
            String emailContent = "<p>Hello</p>"
                    + "Click the link below to reset password"
                    + "<br>"
                    + "Important : The Link will EXPIRE in '10 MINUTES'"
                    + "<p><a href=\"" + emailLink + "\">Change My Password</a></p>"
                    + "<br>"
                    + "Please Ignore This Email, if Request not made by You. Or Report Immediately."
                    + "<br>"
                    + "<br>"
                    + "<br>"
                    + "Technical Support Team"
                    + "<br>"
                    + "WeConnect Inc.";

            // Settign the content body
            mimehelper.setText(emailContent, true);
            // Setting the mail address from which the mail will be sent
            mimehelper.setFrom("weconnect056@gmail.com", "WeConnect Support Team");
            // Setting the subject Line
            mimehelper.setSubject(subject);
            mimehelper.setTo(to);
            // Sending the mail
            javaMailSender.send(message);

        } catch(MessagingException | UnsupportedEncodingException | MailException e) {
            throw new RuntimeException("Failed to send email ", e);
        }
    }

    /**
     * Function to check if token is expired or not. Because a user cannot use the reset link
     * ofter the burnout period. This helps protect any fraud. If need to reset after expiration time,
     * then the user can request new link
     * @param forgotPasswordToken the unique token associated with link
     * @return true if token is not expired, false otherwise
     * */
    public boolean isTokenExpired(ForgotPasswordToken forgotPasswordToken) {
        return LocalDateTime.now().isAfter(forgotPasswordToken.getExpireTime());
    }

    /**
     * Check if token is valid or not because a token can only be used once, or it should be used within the active time frame.
     * Once that conditions are passed, then the link won't work for the user.
     * @param forgotPasswordToken the unique token
     * @param model object used to pass message to the view layer.
     * @return password request page if condition not satisfied, reset page otherwise.
     * */
    public String checkTokenValidity(ForgotPasswordToken forgotPasswordToken, Model model) {

        // validating for a null token
        if(forgotPasswordToken == null) {
            model.addAttribute("InvalidError", "Error or Invalid Token Received");
            return "password_request";
        }
        // Validating if token is used before once
        else if(forgotPasswordToken.isUsed()) {
            model.addAttribute("UsedLinkError", "Reset password Link Already Used Once!!");
            return "password_request";
        }
        // Validating if token is past the expire time
        else if(isTokenExpired(forgotPasswordToken)) {
            model.addAttribute("ExpiredLinkError", "Reset Password Link Expired!!! Please Request Again");
            return "password_request";
        }
        // If all good, then redirect to the webpage
        else {
            return "reset_password";
        }

    }
}
