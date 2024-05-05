package com.example.weconnect.weConnectApp.model;

import com.webapp.weconnect.model.Message;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    @Test
    void gettersAndSettersShouldFunctionCorrectly() {
        // Arrange
        Message message = new Message();
        Long id = 1L;
        String title = "Test Title";
        String content = "This is test content.";
        byte[] image = {1, 2, 3, 4};
        String communityName = "TestCommunity";

        // Act
        message.setTitle(title);
        message.setContent(content);
        message.setPostImage(image);
        message.setCommunityname(communityName);

        // Assert
        assertNull(message.getId(), "ID should be null by default");

    }

    @Test
    void hasImageShouldReturnTrueWhenImageIsSet() {
        // Arrange
        Message message = new Message();
        byte[] image = {1, 2, 3, 4};

        // Act
        message.setPostImage(image);

        // Assert
        assertTrue(message.hasImage(), "hasImage should return true when image is not null and length > 0");
    }

    @Test
    void hasImageShouldReturnFalseWhenImageIsNull() {
        // Arrange
        Message message = new Message();

        // Act & Assert
        assertFalse(message.hasImage(), "hasImage should return false when image is null.");
    }

    @Test
    void hasImageShouldReturnFalseWhenImageIsEmpty() {
        // Arrange
        Message message = new Message();
        byte[] image = {};

        // Act
        message.setPostImage(image);

        // Assert
        assertFalse(message.hasImage(), "hasImage should return false when image array is empty.");
    }
}

