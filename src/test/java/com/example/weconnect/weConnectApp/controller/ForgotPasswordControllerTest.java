package com.example.weconnect.weConnectApp.controller;

import com.webapp.weconnect.controller.ForgotPasswordController;
import com.webapp.weconnect.model.ForgotPasswordToken;
import com.webapp.weconnect.model.User;
import com.webapp.weconnect.repository.ForgotPasswordRepository;
import com.webapp.weconnect.service.ForgotPasswordService;
import com.webapp.weconnect.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import javax.servlet.http.HttpServletRequest;


public class ForgotPasswordControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private ForgotPasswordService forgotPasswordService;

    @Mock
    private ForgotPasswordRepository forgotPasswordRepository;

    @Mock
    private Model model;

    @Mock
    private HttpSession httpSession;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ForgotPasswordController forgotPasswordController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPasswordRequest() {

        ForgotPasswordController forgotPasswordController = new ForgotPasswordController();

        String result = forgotPasswordController.passwordRequest();

        assertEquals("password_request", result);
    }

    @Test
    public void testSavePasswordRequest_UserNotFound() {

        String username = "nonexistent@example.com";
        when(userService.findByEmail(username)).thenReturn(null);

        String result = forgotPasswordController.savePasswordRequest(username, model);

        assertEquals("password_request", result);
        verify(model).addAttribute("error", "Email Address Not Registered");
        verifyNoMoreInteractions(forgotPasswordService, forgotPasswordRepository);
    }

    @Test
    public void testSavePasswordRequest_Success() throws MessagingException, UnsupportedEncodingException {

        String username = "existing@example.com";
        User user = new User();
        user.setEmail(username);
        when(userService.findByEmail(username)).thenReturn(user);

        ForgotPasswordToken token = new ForgotPasswordToken();
        when(forgotPasswordService.generateToken()).thenReturn("dummy_token");
        LocalDateTime expireTime = LocalDateTime.now().plusMinutes(10);
        when(forgotPasswordService.expireTimeRange()).thenReturn(expireTime);
        when(forgotPasswordRepository.save(any())).thenReturn(token);

        String result = forgotPasswordController.savePasswordRequest(username, model);

        assertEquals("redirect:/password_request?success", result);
        verify(forgotPasswordService).sendEmail(eq(username), eq("Password Reset Link"), anyString());
        verify(model, never()).addAttribute(eq("error"), anyString());
    }

    @Test
    public void testSavePasswordRequest_EmailSendingFailure() throws MessagingException, UnsupportedEncodingException {

        String username = "test@example.com";
        User user = new User();
        user.setEmail(username);
        when(userService.findByEmail(username)).thenReturn(user);
        doThrow(new MessagingException("Simulated email sending failure")).when(forgotPasswordService).sendEmail(anyString(), anyString(), anyString());
        Model model = mock(Model.class);

        String result = forgotPasswordController.savePasswordRequest(username, model);

        assertEquals("password_request", result);
        verify(model).addAttribute("error", "Problem in Sending Email");
    }

    @Test
    public void testResetPassword_TokenFound() {

        String token = "dummyToken";
        ForgotPasswordToken expectedToken = new ForgotPasswordToken();
        when(forgotPasswordRepository.findByToken(token)).thenReturn(expectedToken);
        when(forgotPasswordService.checkTokenValidity(expectedToken, model)).thenReturn("expectedViewName");

        String result = forgotPasswordController.resetPassword(token, model, httpSession);

        assertEquals("expectedViewName", result);
    }

    @Test
    public void testResetPassword_TokenNotFound() {

        String token = "nonExistingToken";
        when(forgotPasswordRepository.findByToken(token)).thenReturn(null);
        when(forgotPasswordService.checkTokenValidity(null, model)).thenReturn("errorViewName");

        String result = forgotPasswordController.resetPassword(token, model, httpSession);

        assertEquals("errorViewName", result); // Replace "errorViewName" with the actual error view name
    }

    @Test
    public void testSaveResetPassword_PasswordEmpty() {

        when(httpServletRequest.getParameter("password")).thenReturn("");

        String result = forgotPasswordController.saveResetPassword(httpServletRequest, httpSession, model);

        assertEquals("reset_password", result);
    }

    @Test
    public void testSaveResetPassword_TokenNotFound() {

        String token = "dummyToken";
        when(httpSession.getAttribute("token")).thenReturn(token);
        when(forgotPasswordRepository.findByToken(token)).thenReturn(null);

        String result = forgotPasswordController.saveResetPassword(httpServletRequest, httpSession, model);

        assertEquals("reset_password", result);
    }

    @Test
    public void testSaveResetPassword_TokenNotFoundInSession() {

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpSession httpSession = mock(HttpSession.class);
        Model model = mock(Model.class);
        String password = "testPassword";

        when(httpServletRequest.getParameter("password")).thenReturn(password);
        when(httpSession.getAttribute("token")).thenReturn(null);

        String result = forgotPasswordController.saveResetPassword(httpServletRequest, httpSession, model);

        assertEquals("reset_password", result);
        verify(model).addAttribute("error", "Token not found in session");
    }

    @Test
    public void testSaveResetPassword_Success() {

        String password = "newPassword";
        String token = "dummyToken";
        String encodedPassword = "encodedPassword";

        User user = new User();
        ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken();
        forgotPasswordToken.setUser(user);

        when(httpServletRequest.getParameter("password")).thenReturn(password);
        when(httpSession.getAttribute("token")).thenReturn(token);
        when(forgotPasswordRepository.findByToken(token)).thenReturn(forgotPasswordToken);
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        String result = forgotPasswordController.saveResetPassword(httpServletRequest, httpSession, model);

        assertEquals("reset_password", result);
    }

    @Test
    public void testSaveResetPassword_InvalidToken() {

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpSession httpSession = mock(HttpSession.class);
        Model model = mock(Model.class);
        String password = "testPassword";
        String token = "invalidToken";

        when(httpServletRequest.getParameter("password")).thenReturn(password);
        when(httpSession.getAttribute("token")).thenReturn(token);
        when(forgotPasswordRepository.findByToken(token)).thenReturn(null);

        String result = forgotPasswordController.saveResetPassword(httpServletRequest, httpSession, model);

        assertEquals("reset_password", result);
        verify(model).addAttribute("error", "Invalid token");
    }
}
