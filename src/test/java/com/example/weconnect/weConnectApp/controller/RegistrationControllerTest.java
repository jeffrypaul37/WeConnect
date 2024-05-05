package com.example.weconnect.weConnectApp.controller;

import com.webapp.weconnect.controller.RegistrationController;
import com.webapp.weconnect.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegistrationControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private RegistrationController registrationController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void confirmRegistration_ShouldRedirectToLogin() {
        // Arrange
        String token = "dummyToken";

        // Act
        String viewName = registrationController.confirmRegistration(token);

        // Assert
        verify(userService).validateVerificationToken(token);
        assertEquals("redirect:/login", viewName);
    }
}
