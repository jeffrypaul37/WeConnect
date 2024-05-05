package com.example.weconnect.weConnectApp.model;


import com.webapp.weconnect.model.ForgotPasswordToken;
import com.webapp.weconnect.model.User;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ForgotPasswordTokenTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        ForgotPasswordToken token = new ForgotPasswordToken();
        Long id = 1L;
        String tokenString = "token123";
        User user = new User(); // You may need to provide arguments to the constructor based on the User class definition
        LocalDateTime expireTime = LocalDateTime.now().plusDays(1);
        boolean isUsed = false;

        // Act
        token.setId(id);
        token.setToken(tokenString);
        token.setUser(user);
        token.setExpireTime(expireTime);
        token.setUsed(isUsed);

        // Assert
        assertEquals(user, token.getUser(), "User should match the one that was set.");

    }

    @Test
    void tokenIsUsedShouldReflectCorrectState() {
        // Arrange
        ForgotPasswordToken token = new ForgotPasswordToken();

        // Act & Assert
        token.setUsed(true);
        assertTrue(token.isUsed(), "Token should be marked as used.");

    }
}

