package com.example.weconnect.weConnectApp.service;
import com.webapp.weconnect.model.Community;
import com.webapp.weconnect.repository.FilterCommunityRepository;
import com.webapp.weconnect.service.FilterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
public class FliterServiceTest {
    private FilterCommunityRepository repository = mock(FilterCommunityRepository.class);
    private FilterService filterService;

    @BeforeEach
    void setUp() {
        filterService = new FilterService(repository);
    }

    @Test
    void testFilterByPredefined_MatchFound() {

        Community community1 = new Community();
        community1.setName("Tech Innovators");

        Community community2 = new Community();
        community2.setName("Future Science");

        Community community3 = new Community();
        community3.setName("Art Lovers");

        List<Community> allCommunities = Arrays.asList(community1, community2, community3);

        when(repository.findAll()).thenReturn(allCommunities);

        List<Community> filteredCommunities = filterService.filterByPredefined("name", "Tech");

        assertEquals("Tech Innovators", filteredCommunities.get(0).getName());

        verify(repository, times(1)).findAll();
    }

    @Test
    void testFilterByPredefined_NoMatch() {
        Community community1 = new Community();
        community1.setName("Tech Innovators");

        Community community2 = new Community();
        community2.setName("Future Science");

        Community community3 = new Community();
        community3.setName("Art Lovers");

        List<Community> allCommunities = Arrays.asList(community1, community2, community3);

        when(repository.findAll()).thenReturn(allCommunities);

        List<Community> filteredCommunities = filterService.filterByPredefined("name", "NonExistent");

        assertEquals(0, filteredCommunities.size());

        verify(repository, times(1)).findAll();
    }

}
