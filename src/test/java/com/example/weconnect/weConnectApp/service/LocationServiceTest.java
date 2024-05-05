package com.example.weconnect.weConnectApp.service;

import com.webapp.weconnect.model.Location;
import com.webapp.weconnect.repository.LocationRepository;
import com.webapp.weconnect.service.LocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationService locationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllLocations_shouldReturnAllLocations() {
        // Arrange
        Location location1 = new Location(); // Assuming Location has a no-arg constructor
        Location location2 = new Location();
        // Setup mock behaviour
        when(locationRepository.findAll()).thenReturn(Arrays.asList(location1, location2));

        // Act
        List<Location> locations = locationService.getAllLocations();

        // Assert
        assertEquals(2, locations.size(), "The size of the returned location list should be 2");
        verify(locationRepository).findAll(); // Verifies findAll() was called
    }
}
