package com.example.weconnect.weConnectApp.controller;

import com.webapp.weconnect.controller.UserRegistrationController;
import com.webapp.weconnect.service.UserService;
import com.webapp.weconnect.web.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserRegistrationControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private UserRegistrationController userRegistrationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @GetMapping
    public String showRegistrationForm(@RequestParam(required = false, name = "emailInUse") String emailInUse, Model model) {
        model.addAttribute("emailInUse", emailInUse);
        return "registration";
    }


    @Test
    void showRegistrationForm_WithoutEmailInUse() {
        Model model = new ExtendedModelMap();
        String expectedViewName = "registration";

        String viewName = userRegistrationController.showRegistrationForm(null, model);

        assertEquals(expectedViewName, viewName);
    }


    @Test
    void showRegistrationForm_WithEmailInUse() {
        Model model = new ExtendedModelMap();
        String expectedViewName = "registration";

        String viewName = userRegistrationController.showRegistrationForm("true", model);

        assertEquals(expectedViewName, viewName);
    }

    @Test
    void registerUserAccount_WithErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);
        Model model = new ExtendedModelMap();
        String expectedViewName = "registration";
        UserRegistrationDto registrationDto = new UserRegistrationDto();

        String viewName = userRegistrationController.registerUserAccount(registrationDto, bindingResult, model);

        assertEquals(expectedViewName, viewName);
    }

    @Test
    void registerUserAccount_WithoutErrors_Success() {
        when(bindingResult.hasErrors()).thenReturn(false);
        Model model = new ExtendedModelMap();
        String expectedViewName = "redirect:/registration?success";
        UserRegistrationDto registrationDto = new UserRegistrationDto();

        String viewName = userRegistrationController.registerUserAccount(registrationDto, bindingResult, model);

        assertEquals(expectedViewName, viewName);
        verify(userService, times(1)).save(registrationDto);
    }

    @Test
    void registerUserAccount_WithoutErrors_FailDueToDataIntegrity() {
        when(bindingResult.hasErrors()).thenReturn(false);
        doThrow(new DataIntegrityViolationException("")).when(userService).save(any(UserRegistrationDto.class));
        Model model = new ExtendedModelMap();
        String expectedViewName = "redirect:/registration?emailInUse=true";
        UserRegistrationDto registrationDto = new UserRegistrationDto();

        String viewName = userRegistrationController.registerUserAccount(registrationDto, bindingResult, model);

        assertEquals(expectedViewName, viewName);
    }
}
