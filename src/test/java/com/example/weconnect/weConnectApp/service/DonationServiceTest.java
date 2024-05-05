package com.example.weconnect.weConnectApp.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import com.webapp.weconnect.model.Donation;
import com.webapp.weconnect.repository.DonationRepository;
import com.webapp.weconnect.service.DonationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
public class DonationServiceTest {

    @Mock
    private DonationRepository donationRepositoryMock;

    @InjectMocks
    private DonationService donationService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveDonation() {

        Donation donation = mock(Donation.class);
        donationService.saveDonation(donation);
        verify(donationRepositoryMock, times(1)).save(donation);
        assertTrue(true, "Method completed successfully");
    }

    @Test
    public void testSaveDonationWithNullParameter() {
        // Given
        Donation donation = null;

        // When
        donationService.saveDonation(donation);

        // Then
        verify(donationRepositoryMock, never()).save(any());
        assertTrue(true);
    }
}
