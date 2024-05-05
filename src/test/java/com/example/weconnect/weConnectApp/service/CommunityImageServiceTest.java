package com.example.weconnect.weConnectApp.service;

import com.webapp.weconnect.model.CommunityImage;
import com.webapp.weconnect.repository.CommunityImageRepository;
import com.webapp.weconnect.service.CommunityImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CommunityImageServiceTest {

    @Mock
    private CommunityImageRepository communityImageRepository;

    @InjectMocks
    private CommunityImageService communityImageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getImageStringByCommunityName_whenCommunityImageExists_shouldReturnImage() {
        // Arrange
        String communityName = "TestCommunity";
        byte[] expectedImage = {1, 2, 3, 4};
        CommunityImage mockCommunityImage = new CommunityImage();
        mockCommunityImage.setCommunityImage(expectedImage);
        when(communityImageRepository.findByCommunityName(communityName)).thenReturn(mockCommunityImage);

        // Act
        byte[] actualImage = communityImageService.getImageStringByCommunityName(communityName);

        // Assert
        assertArrayEquals(expectedImage, actualImage, "The returned image should match the expected image.");
    }

    @Test
    public void getImageStringByCommunityName_whenCommunityImageDoesNotExist_shouldReturnNull() {
        // Arrange
        String communityName = "NonExistentCommunity";
        when(communityImageRepository.findByCommunityName(communityName)).thenReturn(null);

        // Act
        byte[] actualImage = communityImageService.getImageStringByCommunityName(communityName);

        // Assert
        assertNull(actualImage, "The returned image should be null for a non-existent community.");
    }

    @Test
    public void getDescriptionByCommunityName_shouldReturnDescription() {
        // Arrange
        String communityName = "TestCommunity";
        String expectedDescription = "This is a test description.";
        CommunityImage mockCommunityImage = new CommunityImage();
        mockCommunityImage.setCommunityDescription(expectedDescription);
        when(communityImageRepository.findByCommunityName(communityName)).thenReturn(mockCommunityImage);

        // Act
        String actualDescription = communityImageService.getDescriptionByCommunityName(communityName);

        // Assert
        assertEquals(expectedDescription, actualDescription, "The returned description should match the expected description.");
    }
}
