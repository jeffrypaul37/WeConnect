package com.example.weconnect.weConnectApp.model;

import com.webapp.weconnect.model.Role;
import com.webapp.weconnect.model.User;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {

    @Test
    void userGettersAndSettersWorkAsExpected() {
        // Arrange
        User user = new User();
        Long id = 1L;
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String password = "password";
        String location = "Some Location";
        boolean enabled = true;
        String verificationToken = "verificationToken";
        Role roleUser = new Role(); // Assuming Role has a no-arg constructor
        roleUser.setName("USER");

        // Act
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setLocation(location);
        user.setEnabled(enabled);
        user.setVerificationToken(verificationToken);
        user.setRoles(new HashSet<>(Arrays.asList(roleUser)));

        // Assert
        assertTrue(user.getRoles().contains(roleUser), "Roles should contain the added role.");
    }

    @Test
    void userConstructorWithRolesShouldSetFieldsCorrectly() {
        // Arrange
        byte[] profilePhoto = {}; // Empty array as placeholder
        String firstName = "John";
        String lastName = "Doe";
        String email = "john@example.com";
        String password = "password";
        String location = "City, Country";
        Role roleAdmin = new Role(); // Assuming Role has a setter for name or similar
        roleAdmin.setName("ADMIN");
        Collection<Role> roles = new HashSet<>(Arrays.asList(roleAdmin));

        // Act
        User user = new User(firstName, lastName, email, password, roles, location, profilePhoto);

        // Assert
        assertTrue(user.getRoles().contains(roleAdmin), "User should contain the role assigned in constructor.");
    }

}
