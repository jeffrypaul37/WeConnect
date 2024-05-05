package com.example.weconnect.weConnectApp.model;

import com.webapp.weconnect.model.Location;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocationTest {

    @Test
    void noArgsConstructorShouldInitializeCorrectly() {
        // Arrange & Act
        Location location = new Location();

        // Assert
        assertEquals(null, location.getId(), "Id should be null for a newly instantiated location without parameters.");
    }

    @Test
    void argsConstructorShouldSetFieldNameCorrectly() {
        // Arrange
        String locationName = "TestLocation";

        // Act
        Location location = new Location(locationName);

        // Assert
        assertEquals(locationName, location.getName(), "Location name should match the value passed to the constructor.");
    }

    @Test
    void setIdShouldCorrectlyAssignValue() {
        // Arrange
        Location location = new Location();
        Long id = 1L;

        // Act
        location.setId(id);

        // Assert
        assertEquals(id, location.getId(), "Location ID should match the value set by setId.");
    }

    @Test
    void setNameShouldCorrectlyAssignValue() {
        // Arrange
        Location location = new Location();
        String name = "NewLocation";

        // Act
        location.setName(name);

        // Assert
        assertEquals(name, location.getName(), "Location name should match the value set by setName.");
    }
}
