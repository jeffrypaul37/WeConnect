package com.example.weconnect.weConnectApp.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionListParams;
import com.webapp.weconnect.controller.CommunityController;
import com.webapp.weconnect.controller.DonationController;
import com.webapp.weconnect.model.Donation;
import com.webapp.weconnect.model.User;
import com.webapp.weconnect.repository.UserRepository;
import com.webapp.weconnect.service.DonationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

@PrepareForTest(Session.class)
public class DonationControllerTest {

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    @Value("${base.url}")
    private String baseUrl;
    @Mock
    private DonationService donationService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DonationController donationController;

    @Mock
    private HttpServletRequest request;


    @Mock
    Authentication authentication;

    @Mock
    SecurityContext securityContext;

    private MockMvc mockMvc;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(donationController).build();
        Stripe.apiKey = "sk_test_51OtEnrEkAeeAylyByxMn7LA7tAD2IypBhknzqdAYN5dqKxJJfui0u0gZxIsVNE8V4Zrc73sSiOgt09mejiqzpd2j00BwCEigG9";
    }

    @Test
    public void testDonateResource_SuccessfulDonation() {

        User user = new User();
        user.setEmail("demouser@gmail.com");

        when(authentication.getName()).thenReturn(user.getEmail());
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(request.getHeader("Referer")).thenReturn("weconnect.com");
        DonationController donationController = new DonationController(donationService, userRepository);
        String result = donationController.donateResource("Demo Content", "Electronics", 3, "DemoEvent", "DemoCommunity", request);
        verify(donationService).saveDonation(any());
        assertEquals("redirect:weconnect.com", result);
    }

    @Test
    public void testDonateResource() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("demo@gmail.com");
        when(userRepository.findByEmail("demo@gmail.com")).thenReturn(new User());
        when(request.getHeader("Referer")).thenReturn("weconnect.com");

        DonationController donationController = new DonationController(donationService, userRepository);
        String result = donationController.donateResource("Demo Content", "Catering", 100, "FoodEvent", "DemoCommunity", request);
        verify(donationService, times(1)).saveDonation(any(Donation.class));
        assertTrue(result.startsWith("redirect:"));
    }

}