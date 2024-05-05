package com.example.weconnect.weConnectApp.web.dto;


import static org.junit.jupiter.api.Assertions.assertEquals;

import com.webapp.weconnect.web.dto.UserRegistrationDto;
import org.junit.jupiter.api.Test;

public class UserRegistrationDtoTest {

    @Test
    public void testGettersAndSetters() {
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String password = "securepassword";
        String location = "SomeLocation";
        byte[] profilePhoto = new byte[] {1, 2, 3, 4, 5};

        UserRegistrationDto user = new UserRegistrationDto();

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setLocation(location);
        user.setProfile_photo(profilePhoto);
        assertEquals(email, user.getEmail(), "Email must match");
    }

    @Test
    public void testConstructor() {
        String firstName = "Jane";
        String lastName = "Smith";
        String email = "jane.smith@example.com";
        String password = "password123";
        String location = "AnotherLocation";
        byte[] profilePhoto = new byte[] {5, 4, 3, 2, 1};

        UserRegistrationDto user = new UserRegistrationDto(firstName, lastName, email, password, location, profilePhoto);

        assertEquals(email, user.getEmail(), "Email must match");

    }
}
