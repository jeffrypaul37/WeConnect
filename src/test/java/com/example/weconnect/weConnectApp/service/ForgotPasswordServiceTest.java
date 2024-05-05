package com.example.weconnect.weConnectApp.service;

import com.webapp.weconnect.model.ForgotPasswordToken;
import com.webapp.weconnect.service.ForgotPasswordService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@ExtendWith(MockitoExtension.class)
@PrepareForTest(ForgotPasswordService.class)
public class ForgotPasswordServiceTest {

    @Mock
    private Model model;

    @Mock
    private ForgotPasswordToken forgotPasswordToken;

    @InjectMocks
    ForgotPasswordService forgotPasswordService = new ForgotPasswordService();

    @Mock
    private JavaMailSender javaMailSender;

    @Test
    public void testIsTokenExpired_NotExpired() {
        LocalDateTime futureExpiration = LocalDateTime.now().plusMinutes(15);
        ForgotPasswordToken token = new ForgotPasswordToken();
        token.setToken("validToken");
        token.setExpireTime(futureExpiration);
        token.setUsed(false);
        boolean result = forgotPasswordService.isTokenExpired(token);

        assertFalse(result);
    }

    @Test
    void generateToken() {
        String token = forgotPasswordService.generateToken();
        assertEquals(36, token.length());
    }

    @Test
    void expireTimeRange() {
        LocalDateTime expireTime = forgotPasswordService.expireTimeRange();
        assertTrue(expireTime.isBefore(LocalDateTime.now().plusMinutes(11)));
    }

    @Test
    void testIsTokenExpired() {

        ForgotPasswordService forgotPasswordService = new ForgotPasswordService();

        ForgotPasswordToken pastToken = mock(ForgotPasswordToken.class);
        when(pastToken.getExpireTime()).thenReturn(LocalDateTime.now().minusHours(1));

        assertTrue(forgotPasswordService.isTokenExpired(pastToken), "Past token should be expired");
    }

    @Test
    void testCheckTokenValidity_NullToken() {

        String result = forgotPasswordService.checkTokenValidity(null, model);

        verify(model).addAttribute("InvalidError", "Error or Invalid Token Received");
        assertEquals("password_request", result);
    }

    @Test
    void testCheckTokenValidity_UsedToken() {
        // Arrange
        when(forgotPasswordToken.isUsed()).thenReturn(true);

        // Act
        String result = forgotPasswordService.checkTokenValidity(forgotPasswordToken, model);

        // Assert
        verify(model).addAttribute(eq("UsedLinkError"), anyString());
        assertEquals("password_request", result);
    }

    @Test
    public void testCheckTokenValidity_ValidToken() {
        LocalDateTime futureExpiration = LocalDateTime.now().plusMinutes(15);
        ForgotPasswordToken token = new ForgotPasswordToken();
        token.setToken("validToken");
        token.setExpireTime(futureExpiration);
        token.setUsed(false);

        Model model = new Model() {
            @Override
            public Model addAttribute(@NotNull String attributeName, Object attributeValue) {
                return null;
            }

            @Override
            public Model addAttribute(@NotNull Object attributeValue) {
                return null;
            }

            @Override
            public Model addAllAttributes(@NotNull Collection<?> attributeValues) {
                return null;
            }

            @Override
            public Model addAllAttributes(@NotNull Map<String, ?> attributes) {
                return null;
            }

            @Override
            public Model mergeAttributes(@NotNull Map<String, ?> attributes) {
                return null;
            }

            @Override
            public boolean containsAttribute(@NotNull String attributeName) {
                return false;
            }

            @Override
            public Object getAttribute(@NotNull String attributeName) {
                return null;
            }

            @Override
            public Map<String, Object> asMap() {
                return null;
            }
        };

        String result = forgotPasswordService.checkTokenValidity(token, model);

        assertEquals("reset_password", result);
    }
}
