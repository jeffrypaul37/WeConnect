package com.example.weconnect.weConnectApp.service;

import com.webapp.weconnect.model.Role;
import com.webapp.weconnect.model.User;
import com.webapp.weconnect.model.VerificationToken;
import com.webapp.weconnect.repository.UserRepository;
import com.webapp.weconnect.repository.VerificationTokenRepository;
import com.webapp.weconnect.service.EmailService;
import com.webapp.weconnect.service.UserServiceImpl;
import com.webapp.weconnect.web.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private VerificationTokenRepository verificationTokenRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void validateVerificationToken() {
        String token = "test-token";
        User user = new User();
        user.setEnabled(false);

        VerificationToken verificationToken = new VerificationToken(token, user);

        when(verificationTokenRepository.findByToken(token)).thenReturn(verificationToken);

        userService.validateVerificationToken(token);

        assertTrue(user.isEnabled());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void loadUserByUsernameFound() {
        String email = "user@example.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword("password");
        user.setRoles(Collections.singletonList(new Role("ROLE_USER")));
        user.setEnabled(true);

        when(userRepository.findByEmail(email)).thenReturn(user);

        org.springframework.security.core.userdetails.UserDetails userDetails = userService.loadUserByUsername(email);

        assertEquals(email, userDetails.getUsername());
    }

    @Test
    void loadUserByUsernameNotFound() {
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(email));
    }

    @Test
    void save_ShouldSaveUserAndSendVerificationEmail() {

        UserRepository userRepository = mock(UserRepository.class);
        VerificationTokenRepository verificationTokenRepository = mock(VerificationTokenRepository.class);
        BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);

        UserServiceImpl userService = new UserServiceImpl(userRepository, verificationTokenRepository);

        BCryptPasswordEncoder mockPasswordEncoder = mock(BCryptPasswordEncoder.class);
        when(mockPasswordEncoder.encode("password123")).thenReturn("hashedPassword");

        userService.setPasswordEncoder(mockPasswordEncoder);


        EmailService mockEmailService = mock(EmailService.class);
        userService.setEmailService(mockEmailService);

        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setFirstName("John");
        registrationDto.setLastName("Doe");
        registrationDto.setEmail("john.doe@example.com");
        registrationDto.setPassword("password123");
        registrationDto.setLocation("New York");

        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(userCaptor.capture())).thenReturn(new User());

        ArgumentCaptor.forClass(VerificationToken.class);

        userService.save(registrationDto);

        User savedUser = userCaptor.getValue();
        assertNotNull(savedUser);
    }

    @Test
    void validateVerificationToken_ShouldActivateUserIfTokenIsValid() {
        // Arrange
        UserRepository userRepository = mock(UserRepository.class);
        VerificationTokenRepository verificationTokenRepository = mock(VerificationTokenRepository.class);

        UserServiceImpl userService = new UserServiceImpl(userRepository, verificationTokenRepository);

        String validToken = "validToken";
        VerificationToken verificationToken = new VerificationToken(validToken, new User());
        when(verificationTokenRepository.findByToken(validToken)).thenReturn(verificationToken);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(userCaptor.capture())).thenReturn(new User());

        userService.validateVerificationToken(validToken);

        User activatedUser = userCaptor.getValue();
        assertTrue(activatedUser.isEnabled());
    }

    @Test
    void validateVerificationToken_ShouldDoNothingIfTokenIsInvalid() {

        UserRepository userRepository = mock(UserRepository.class);
        VerificationTokenRepository verificationTokenRepository = mock(VerificationTokenRepository.class);

        UserServiceImpl userService = new UserServiceImpl(userRepository, verificationTokenRepository);

        String invalidToken = "invalidToken";
        when(verificationTokenRepository.findByToken(invalidToken)).thenReturn(null);

        when(userRepository.save(any(User.class))).thenReturn(new User());

        userService.validateVerificationToken(invalidToken);

        verify(userRepository, never()).save(any(User.class));
        assertFalse(
                Mockito.mockingDetails(userRepository).getInvocations().stream()
                        .anyMatch(invocation -> invocation.getMethod().getName().equals("save"))
        );
    }

    @Test
    void findByEmail_ShouldReturnUserWithValidEmail() {

        UserRepository userRepository = mock(UserRepository.class);
        UserServiceImpl userService = new UserServiceImpl(userRepository, mock(VerificationTokenRepository.class));

        String validEmail = "john.doe@example.com";
        User expectedUser = new User();
        when(userRepository.findByEmail(validEmail)).thenReturn(expectedUser);

        User actualUser = userService.findByEmail(validEmail);

        verify(userRepository).findByEmail(validEmail);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    void findByEmail_ShouldReturnNullWithInvalidEmail() {

        UserRepository userRepository = mock(UserRepository.class);
        UserServiceImpl userService = new UserServiceImpl(userRepository, mock(VerificationTokenRepository.class));

        String invalidEmail = "nonexistent@example.com";
        when(userRepository.findByEmail(invalidEmail)).thenReturn(null);

        User actualUser = userService.findByEmail(invalidEmail);

        verify(userRepository).findByEmail(invalidEmail);

        assertNull(actualUser);
    }

    @Test
    void save_ShouldReturnSavedUser() {

        UserRepository userRepository = mock(UserRepository.class);
        UserServiceImpl userService = new UserServiceImpl(userRepository, mock(VerificationTokenRepository.class));

        User userToSave = new User();
        when(userRepository.save(userToSave)).thenReturn(userToSave);

        User savedUser = userService.save(userToSave);

        verify(userRepository).save(userToSave);

        assertEquals(userToSave, savedUser);
    }

    @Test
    void loadUserByUsername_ShouldReturnUserDetailsForValidUser() {

        UserRepository userRepository = mock(UserRepository.class);
        UserServiceImpl userService = new UserServiceImpl(userRepository, mock(VerificationTokenRepository.class));

        String validUsername = "john.doe@example.com";
        User validUser = new User();
        validUser.setEmail(validUsername);
        validUser.setPassword("hashedPassword");
        validUser.setEnabled(true);
        validUser.setRoles(Collections.singletonList(new Role("ROLE_USER")));
        when(userRepository.findByEmail(validUsername)).thenReturn(validUser);

        org.springframework.security.core.userdetails.UserDetails userDetails = userService.loadUserByUsername(validUsername);

        verify(userRepository).findByEmail(validUsername);

        assertTrue(userDetails.isEnabled());
    }

    @Test
    void loadUserByUsername_ShouldThrowExceptionForInvalidUser() {

        UserRepository userRepository = mock(UserRepository.class);
        UserServiceImpl userService = new UserServiceImpl(userRepository, mock(VerificationTokenRepository.class));

        String invalidUsername = "nonexistent@example.com";
        when(userRepository.findByEmail(invalidUsername)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername(invalidUsername));

        verify(userRepository).findByEmail(invalidUsername);
    }

    @Test
    void loadUserByUsername_ShouldThrowExceptionForDisabledUser() {

        UserRepository userRepository = mock(UserRepository.class);
        UserServiceImpl userService = new UserServiceImpl(userRepository, mock(VerificationTokenRepository.class));

        String disabledUsername = "disabled@example.com";
        User disabledUser = new User();
        disabledUser.setEmail(disabledUsername);
        disabledUser.setPassword("hashedPassword");
        disabledUser.setEnabled(false);
        when(userRepository.findByEmail(disabledUsername)).thenReturn(disabledUser);

        assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername(disabledUsername));

        verify(userRepository).findByEmail(disabledUsername);
    }

    @Test
    void getCurrentLoggedInUser_ShouldReturnLoggedInUser() {

        UserRepository userRepository = mock(UserRepository.class);
        UserServiceImpl userService = new UserServiceImpl(userRepository, mock(VerificationTokenRepository.class));

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("john.doe@example.com");

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);

        when(authentication.getPrincipal()).thenReturn(userDetails);

        User expectedUser = new User(
                "John",
                "Doe",
                "john.doe@example.com",
                "password",
                (Collection<Role>) Collections.singletonList(new Role("ROLE_USER")),
                "New York",
                null
        );
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(expectedUser);

        User loggedInUser = userService.getCurrentLoggedInUser();

        verify(userRepository).findByEmail("john.doe@example.com");

        assertEquals(expectedUser, loggedInUser);
    }

    @Test
    void updateUserProfile_ShouldSaveUpdatedUser() {

        UserRepository userRepository = mock(UserRepository.class);
        UserServiceImpl userService = new UserServiceImpl(userRepository, mock(VerificationTokenRepository.class));

        User updatedUser = new User(
                "John",
                "Doe",
                "john.doe@example.com",
                "password",
                (Collection<Role>) Collections
                        .singletonList(new Role("ROLE_USER")),
                "New York",
                null
        );
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        userService.updateUserProfile(updatedUser);

        verify(userRepository).save(updatedUser);

        assertEquals("john.doe@example.com", updatedUser.getEmail());

    }
}
