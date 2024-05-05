package com.example.weconnect.weConnectApp.service;

import com.webapp.weconnect.model.Community;
import com.webapp.weconnect.repository.FilterCommunityRepository;
import com.webapp.weconnect.service.SearchService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class SearchServiceTest {

    @Mock
    private FilterCommunityRepository mockRepository;

    @InjectMocks
    private SearchService searchService;

    @Test
    void testSearchByTitle() {
        MockitoAnnotations.openMocks(this);

        List<Community> mockCommunities = Arrays.asList(
                new Community(),
                new Community()
        );
        when(mockRepository.findByNameContainingIgnoreCase(anyString())).thenReturn(mockCommunities);

        List<Community> result = searchService.searchByTitle("searchTerm");

        assertEquals(mockCommunities, result);
    }
}
