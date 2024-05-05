package com.example.weconnect.weConnectApp.model;


import com.webapp.weconnect.model.CommunityImage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CommunityImageTest {

    @Test
    void communityImageGettersAndSettersWorkAsExpected() {
        // Arrange
        CommunityImage communityImage = new CommunityImage();
        byte[] image = {1, 2, 3, 4};
        String description = "A description of the community image.";
        String communityName = "CommunityName";

        // Act
        communityImage.setCommunityImage(image);
        communityImage.setCommunityDescription(description);
        communityImage.setCommunityName(communityName);

        // Assert
        assertArrayEquals(image, communityImage.getCommunityImage(), "Image byte array should match the one that was set.");
    }
}
