package com.example.weconnect.weConnectApp.model;


import com.webapp.weconnect.model.User;
import com.webapp.weconnect.model.VerificationToken;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class VerificationTokenTest {

    @Test
    void tokenExpiryDateIsCorrectlyCalculated() {
        // Arrange
        User user = new User(); // Ensure this matches your User class constructor
        String tokenString = "testToken";
        VerificationToken token = new VerificationToken(tokenString, user);

        // Act
        Date now = new Date();
        Date expiryDate = token.getExpiryDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.MINUTE, 60 * 24); // Add 24 hours
        Date expectedExpiryDate = cal.getTime();

        long diffInMillies = Math.abs(expiryDate.getTime() - expectedExpiryDate.getTime());
        assertTrue(diffInMillies < 1000, "Expiry date should be approximately 24 hours from now");
    }

    @Test
    void gettersAndSettersWorkAsExpected() {
        // Arrange
        User user = new User(); // Assume a user instance is correctly created
        VerificationToken token = new VerificationToken();

        // Act
        token.setId(1L);
        token.setToken("testToken");
        token.setUser(user);
        Date now = new Date();
        token.setExpiryDate(now);

        // Assert
        assertEquals("testToken", token.getToken());
    }
}
