package com.example.weconnect.weConnectApp.controller;

import com.webapp.weconnect.controller.FilterController;
import com.webapp.weconnect.model.Community;
import com.webapp.weconnect.service.FilterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class FilterControllerTest {

    @Mock
    private FilterService filterService;

    @InjectMocks
    private FilterController filterController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void filterByPredefined_WhenCalled_ReturnsFilteredCommunities() {
        // Arrange
        String filterType = "location";
        String filterValue = "New York";
        Community community1 = new Community(); // Assume necessary fields are set
        Community community2 = new Community(); // Assume necessary fields are set
        List<Community> expectedCommunities = Arrays.asList(community1, community2);

        when(filterService.filterByPredefined(filterType, filterValue)).thenReturn(expectedCommunities);

        // Act
        ResponseEntity<List<Community>> response = filterController.filterByPredefined(filterType, filterValue);

        // Assert
        assertEquals(expectedCommunities, response.getBody());
    }
}
