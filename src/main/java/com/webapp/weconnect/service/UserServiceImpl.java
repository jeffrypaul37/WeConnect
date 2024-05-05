package com.webapp.weconnect.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

import com.webapp.weconnect.controller.UserProfileController;
import org.aspectj.apache.bcel.util.ClassPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.webapp.weconnect.model.Role;
import com.webapp.weconnect.model.User;
import com.webapp.weconnect.model.VerificationToken;
import com.webapp.weconnect.repository.UserRepository;
import com.webapp.weconnect.repository.VerificationTokenRepository;
import com.webapp.weconnect.web.dto.UserRegistrationDto;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserService userService;
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    public UserServiceImpl(UserRepository userRepository, VerificationTokenRepository verificationTokenRepository) {
        super();
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    /**
     * To save the user upon successful registration. This is so because the user information needs to be stored
     * in the database to help them access the website and also to authenticate the user by sending email verification
     * @param registrationDto the DTO used for user data storage
     * */
    @Override
    public User save(UserRegistrationDto registrationDto) {
        User user = new User(registrationDto.getFirstName(),
                registrationDto.getLastName(), registrationDto.getEmail(),
                passwordEncoder.encode(registrationDto.getPassword()), (Collection<Role>) Arrays.asList(new Role("ROLE_USER")), registrationDto.getLocation(), registrationDto.getProfile_photo());

        user = userRepository.save(user); // Save user information

        // Generating Authentication Tokens
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token, user);
        verificationTokenRepository.save(verificationToken);

        // Send verification email
        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl = "http://172.17.1.76:8080/registrationConfirm?token=" + token;
        String message = "Please click the link to verify your registration: " + confirmationUrl;

        emailService.sendEmail(recipientAddress, subject, message);

        return user;
    }

    /**
     * Validate the validation token whether it is set or not means, that whether the user has verified themselves
     * using the email verification link sent upon registration.
     * @param token the unique token used for validating
     * */
    public void validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken != null) {
            User user = verificationToken.getUser();
            user.setEnabled(true); // Activate user
            userRepository.save(user);
        }
    }

    /**
     * Retrieve user based on their email. As the emails are unique for every person, it is easy to identify the user in large data records.
     * @param email the user's email address.
     * @return User the user found for the email.
     * */
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Save the user in the database.
     * @param user object that contains user information
     * @return user that has been stored.
     * */
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Retrieve the user using the username. This makes it easier to find the user in large database.
     * @param username the username
     * */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }

        if (!user.isEnabled()) {
            throw new UsernameNotFoundException("User not activated. Please check your email for activation link.");
        }

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                user.isEnabled(), true, true, true,
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    /**
     * Method to get the current logged in user by using the Security context of spring
     * This makes it easy to fetch the current user who is logged in the website
     * @return user that is currently logged in
     * */
    public User getCurrentLoggedInUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(userDetails.getUsername());
    }

    /**
     * Implementation of Method to store the updated user details
     * This makes it easier to upadate the user information anytime and multiple time as they want
     * */
    public void updateUserProfile(User updatedUser) {
        userRepository.save(updatedUser);
    }

}

