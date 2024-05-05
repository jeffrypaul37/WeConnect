package com.example.weconnect.weConnectApp.controller;

import com.webapp.weconnect.controller.MainController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainControllerTest {

    private MainController mainController;
    public Model model;

    @BeforeEach
    public void setUp() {
        // Initialize the MainController and a mock Model
        mainController = new MainController();
        model = mock(Model.class); // Mocking Model though not used here, useful for future tests
    }

    @Test
    public void login_ReturnsCorrectViewName() {
        // Act
        String viewName = mainController.login();

        // Assert
        assertEquals("login", viewName, "The login method should return the view name 'login'");
    }
}

