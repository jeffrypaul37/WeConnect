package com.webapp.weconnect.controller;

import com.webapp.weconnect.model.Location;
import com.webapp.weconnect.model.User;
import com.webapp.weconnect.service.LocationService;
import com.webapp.weconnect.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/profile")
public class UserProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private LocationService locationService;

    /**
     * Method to view the user profile who is currently logged in.
     * So that if a user wants to review its personal information, then he can do so using this page.
     * @return profile page which contains the user details.
     * */
    @GetMapping("/view")
    public String viewProfile(Model model, RedirectAttributes redirectAttributes) {
        User currentUser = userService.getCurrentLoggedInUser();
        model.addAttribute("currentUser", currentUser);
        // Check if the redirectAttributes contain the success message
        if (redirectAttributes.getFlashAttributes().containsKey("successUpdateMessage")) {
            String message = (String) redirectAttributes.getFlashAttributes().get("successUpdateMessage");
            model.addAttribute("successUpdateMessage", message);
        }
        return "profile";
    }

    /**
     * Method to perform an update on profile details. So that the user can update his information. So latest details are represented to others.
     * @return profile edit page where user can edit the contents.
     */
    @GetMapping("/edit")
    public String editProfile(Model model) {
        // Retrieve the currently logged-in user and add it to the model
        User currentUser = userService.getCurrentLoggedInUser();
        // Retrieve the list of locations from the database
        List<Location> locations = locationService.getAllLocations();
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("locations",locations);
        return "profile_edit";
    }

    /**
     * Method tp update the user details based on the changes made by the user.
     * So that the latest changes can be recorded in out records.
     * @param user user model to fetch the user
     * @param firstName first name of user
     * @param lastName last name of user
     * @param location the location of user
     * @param uploadImage the profile picture uploaded by user, if any.
     * @return profile edit page with success message, error message otherwise.
     * */
    @PostMapping("/update")
    public String updateProfile(@ModelAttribute("user") User user,
                                @RequestParam("firstName") String firstName,
                                @RequestParam("lastName") String lastName,
                                @RequestParam("userLocation") String location,
                                @RequestParam(value = "uploadImage", required = false) MultipartFile uploadImage,
                                Model model, RedirectAttributes redirectAttributes) {
        try {
            //Retrieving the currently signed in user
            User currentUser = userService.getCurrentLoggedInUser();

            // Handling if any change is made or not
            if (!firstName.isEmpty()) {
                currentUser.setFirstName(firstName);
            }
            if (!lastName.isEmpty()) {
                currentUser.setLastName(lastName);
            }
            if (!location.isEmpty()) {
                currentUser.setLocation(location);
            }
            if(!uploadImage.isEmpty())
            {
                byte[] imageBytes = uploadImage.getBytes();
                currentUser.setProfile_photo(imageBytes);
            }
            //Storing the updated user information
            userService.updateUserProfile(currentUser);
            model.addAttribute("successUpdateMessage", "Profile updated successfully!");
            redirectAttributes.addFlashAttribute("successUpdateMessage", "Profile updated successfully!");
            return "redirect:/profile/view";
            //        return "profile";
        } catch (Exception e) {
            model.addAttribute("error", "An error occured while updating the profile. Please try again later.");
            return "profile_edit";
        }
    }
}
