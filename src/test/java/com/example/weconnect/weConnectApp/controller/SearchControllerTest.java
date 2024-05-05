package com.example.weconnect.weConnectApp.controller;

import com.webapp.weconnect.controller.SearchController;
import com.webapp.weconnect.model.Community;
import com.webapp.weconnect.service.CommunityService;
import com.webapp.weconnect.service.SearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class SearchControllerTest {

    @Mock
    private CommunityService communityService;

    @Mock
    private SearchService searchService;

    @InjectMocks
    private SearchController searchController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void searchByTitle_shouldReturnFilteredAndApprovedCommunities() {
        // Arrange
        String title = "Test Community";
        Community community1 = new Community();
        community1.setId(1L);
        community1.setName("Community1");
        community1.setIsLeader(true);
        community1.setIsApproved(true);

        Community community2 = new Community();
        community2.setId(2L);
        community2.setName("Community2");
        community2.setIsLeader(true);
        community2.setIsApproved(false); // Not approved

        Community community3 = new Community();
        community3.setId(3L);
        community3.setName("Community3");
        community3.setIsLeader(false); // Not a leader
        community3.setIsApproved(true);

        List<Community> mockSearchedCommunities = Arrays.asList(community1, community2, community3);
        List<Community> mockApprovedCommunities = Arrays.asList(community1); // Only community1 is approved

        when(searchService.searchByTitle(anyString())).thenReturn(mockSearchedCommunities);
        when(communityService.listApprovedCommunities()).thenReturn(mockApprovedCommunities);

        // Act
        ResponseEntity<List<Community>> response = searchController.searchByTitle(title);

        // Assert
        List<Community> expected = Arrays.asList(community1); // Expect only community1
        assertEquals(expected.get(0).getId(), response.getBody().get(0).getId());
    }
}
