package com.example.weconnect.weConnectApp.model;

import com.webapp.weconnect.model.Event;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    @Test
    void gettersAndSettersShouldFunctionCorrectly() {
        // Arrange
        Event event = new Event();
        Long id = 1L;
        String title = "Event Title";
        String content = "Event Content";
        byte[] image = {0, 1, 2, 3};
        String communityName = "CommunityName";

        // Act
        event.setTitle(title);
        event.setContent(content);
        event.setEventImage(image);
        event.setCommunityname(communityName);

        // Assert
        assertEquals(communityName, event.getCommunityname(), "Community name should match the setter value.");
    }

    @Test
    void hasImageShouldReturnTrueWhenImageIsSet() {
        // Arrange
        Event event = new Event();
        byte[] image = {1, 2, 3, 4};

        // Act
        event.setEventImage(image);

        // Assert
        assertTrue(event.hasImage(), "hasImage should return true when image is not null and length > 0");
    }

    @Test
    void hasImageShouldReturnFalseWhenImageIsNull() {
        // Arrange
        Event event = new Event();

        // Act & Assert
        assertFalse(event.hasImage(), "hasImage should return false when image is null.");
    }

    @Test
    void hasImageShouldReturnFalseWhenImageIsEmpty() {
        // Arrange
        Event event = new Event();
        byte[] image = new byte[0];

        // Act
        event.setEventImage(image);

        // Assert
        assertFalse(event.hasImage(), "hasImage should return false when image array is empty.");
    }
}
