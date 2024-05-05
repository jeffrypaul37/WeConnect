package com.webapp.weconnect.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import com.webapp.weconnect.model.User;
import com.webapp.weconnect.web.dto.UserRegistrationDto;
import org.springframework.web.multipart.MultipartFile;

public interface UserService extends UserDetailsService {

    User save(UserRegistrationDto registrationDto);

    // Validate the email verification token
    void validateVerificationToken(String token);

    // Find a user by their email
    User findByEmail(String email);

    // Save the user directly
    User save(User user);

    //Fetch the current logged in user
    User getCurrentLoggedInUser();

    // Update the new user information
    void updateUserProfile(User updatedUser);

}
