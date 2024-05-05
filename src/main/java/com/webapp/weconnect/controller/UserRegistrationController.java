package com.webapp.weconnect.controller;

import com.webapp.weconnect.service.UserService;
import com.webapp.weconnect.web.dto.UserRegistrationDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

    private UserService userService;

    /** Controller for the user registration
     * @param userService the service class of user
     * */

    public UserRegistrationController(UserService userService) {
        super();
        this.userService = userService;
    }

    /**
     * Method for the user registration Dto to be used
     * */
    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    /**
     * Method to display the registration form to the user.
     * So that they can register themselves in order to use the website.
     * @param emailInUse
     * @param model the model object, used to pass attributes back to the view layer.
     * @return registration page where the user will enter their details
     * */
    @GetMapping
    public String showRegistrationForm(@RequestParam(required = false, name = "emailInUse") String emailInUse, Model model) {
        model.addAttribute("emailInUse", emailInUse);
        return "registration";
    }

    /**
     * Method to save the registration form of the user with user details.
     * So that the user can be added to our list of users and can access the website.
     * @param registrationDto the DTO for the user
     * @param model the object, used to store the user details as per structure.
     * @return registration page with success message or prompt the user to use another email.
     * */
    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDto registrationDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "registration";
        }
        try {
            userService.save(registrationDto);
        } catch (DataIntegrityViolationException e) {
            return "redirect:/registration?emailInUse=true";
        }
        return "redirect:/registration?success";
    }
}
