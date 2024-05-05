package com.webapp.weconnect.controller;

import com.webapp.weconnect.model.ForgotPasswordToken;
import com.webapp.weconnect.model.User;
import com.webapp.weconnect.repository.ForgotPasswordRepository;
import com.webapp.weconnect.repository.UserRepository;
import com.webapp.weconnect.service.ForgotPasswordService;
import com.webapp.weconnect.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;

/**
 * ForgotPasswordController implements all the methods required from requesting for new
 * password link to resetting the new password.
 *
 *
 * @author Luv Patel
 * @version 1.0
 * */
@Controller
public class ForgotPasswordController {

    // Creating objects of services and repository
    @Autowired
    private UserService userService;

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @Autowired
    private ForgotPasswordRepository forgotPasswordRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    /** Function to display the reset password request link
     */
    @GetMapping("/password_request")
    public String passwordRequest() {
        return "password_request";
    }

    /**
     * Method to obtain the email address, where the reset link needs to be sent
     * so that the correct user gets the link upon request to reset password.
     * @param username the user detail on which the link needs to be shared
     * @param model The Model object, used to pass attributes back to the view layer. This can include error messages or success notifications.
     * @return password reset page with successful link sent message to acknowledge user
     * */
    @PostMapping("/password_request")
    public String savePasswordRequest(@RequestParam("username") String username, Model model) {

        // Obtaining the user based on the email entered
        User user = userService.findByEmail(username);

        // verifying if user is present or not
        if(user == null)
        {
            // If user not exist, display an error on request page
            model.addAttribute("error", "Email Address Not Registered");
            return "password_request";
        }
        //If exist, generate a token and set the expiring time of 10 minutes.
        ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken();
        forgotPasswordToken.setExpireTime(forgotPasswordService.expireTimeRange());
        forgotPasswordToken.setToken(forgotPasswordService.generateToken());
        forgotPasswordToken.setUser(user);
        forgotPasswordToken.setUsed(false);

        // Save the token to repository
        forgotPasswordRepository.save(forgotPasswordToken);

        String emailLink = "http://172.17.1.76:8080/reset_password?token=" + forgotPasswordToken.getToken();

        try {
            // Send password reset link to the user's email
            forgotPasswordService.sendEmail(user.getEmail(),"Password Reset Link", emailLink);
        } catch (MessagingException | UnsupportedEncodingException e) {
            model.addAttribute("error", "Problem in Sending Email");
            return "password_request";
        }

        // Successfulky redirect to the page with success state
        return "redirect:/password_request?success";
    }

    /**
     * Method to get the new password based on the token generated validity.
     * This way only the link which is active can be used to change password, this will prevent any malfunction practices.
     * @param token the password link token to verify the link
     * @param model the model
     * @param httpSession The HttpSession object, used here to retrieve the password reset token stored in the user's session.
     * @return a validity of the token and if active then process further
     * */
    @GetMapping("/reset_password")
    public String resetPassword(@Param(value="token") String token, Model model, HttpSession httpSession) {

        httpSession.setAttribute("token", token);
        // Find based on the token generated to reset password
        ForgotPasswordToken forgotPasswordToken = forgotPasswordRepository.findByToken(token);

        return forgotPasswordService.checkTokenValidity(forgotPasswordToken, model);
    }


    /**
     * Handles the password reset process for a user. This method is mapped to a POST request for resetting the user's password.
     * It validates the user-entered password and the password reset token stored in the session. If the validation is successful,
     * it encodes the new password and updates it for the user in the database. It also marks the used token as used.
     * @param httpServletRequest The HttpServletRequest object, allowing access to request information including parameters sent by the client.
     * @param httpSession The HttpSession object, used here to retrieve the password reset token stored in the user's session.
     * @param model The Model object, used to pass attributes back to the view layer. This can include error messages or success notifications.
     * @return A string indicating the view name to render as a response.
     * */
    @PostMapping("/reset_password")
    public String saveResetPassword(HttpServletRequest httpServletRequest, HttpSession httpSession,Model model) {

        // get the user entered password
        String password = httpServletRequest.getParameter("password");
        if(password == null || password.isEmpty())
        {
            model.addAttribute("error", "Password Cannot be empty");
            return "reset_password";
        }
        // Get the token
        String token = (String)httpSession.getAttribute("token");
        if (token == null) {
            model.addAttribute("error", "Token not found in session");
            return "reset_password";
        }

        ForgotPasswordToken forgotPasswordToken = forgotPasswordRepository.findByToken(token);
        if (forgotPasswordToken == null) {
            model.addAttribute("error", "Invalid token");
            return "reset_password";
        }
        User user = forgotPasswordToken.getUser();

        // Encode the password before storing to database, to increase the privacy
        user.setPassword(passwordEncoder.encode(password));
        forgotPasswordToken.setUsed(true);


        // Save the changes to database
        userService.save(user);
        forgotPasswordRepository.save(forgotPasswordToken);

        // Pass message to the webpage about successful password reset.
        model.addAttribute("successMessage", "Password Changed Successfully!! Please Login with new Password...");

        return "reset_password";
    }
}
