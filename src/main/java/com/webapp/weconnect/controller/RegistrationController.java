package com.webapp.weconnect.controller;

import com.webapp.weconnect.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    /**
     * Method to validate the token generated when a user sign up for the first time
     * so that the user can verify themselves using the link received on their email to register themselves
     * @return the login page upon successful verification.
     * */
    @GetMapping("/registrationConfirm")
    public String confirmRegistration(@RequestParam("token") String token) {
        //  implement validateVerificationToken
        userService.validateVerificationToken(token);
        return "redirect:/login"; // goto login
    }
}
