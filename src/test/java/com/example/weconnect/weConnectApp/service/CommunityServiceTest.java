package com.example.weconnect.weConnectApp.service;

import com.webapp.weconnect.model.Community;
import com.webapp.weconnect.repository.CommunityRepository;
import com.webapp.weconnect.service.CommunityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class CommunityServiceTest {

    @Mock
    private CommunityRepository communityRepository;

    @InjectMocks
    private CommunityService communityService;

    private Community approvedCommunity;
    private Community pendingCommunity;

    @BeforeEach
    void setUp() {
        approvedCommunity = new Community();
        approvedCommunity.setId(1L);
        approvedCommunity.setName("Approved Community");
        approvedCommunity.setIsApproved(true);

        pendingCommunity = new Community();
        pendingCommunity.setId(2L);
        pendingCommunity.setName("Pending Community");
        pendingCommunity.setIsApproved(false);
    }

    @Test
    void createCommunity_ShouldSaveCommunityWithApprovalStatusFalse() {
        communityService.createCommunity(pendingCommunity);
        assertFalse(pendingCommunity.getIsApproved());
        verify(communityRepository, times(1)).save(pendingCommunity);
    }

    @Test
    void listApprovedCommunities_ShouldReturnApprovedCommunities() {
        when(communityRepository.findByIsApproved(true)).thenReturn(Arrays.asList(approvedCommunity));
        List<Community> approvedCommunities = communityService.listApprovedCommunities();
        assertThat(approvedCommunities).containsExactly(approvedCommunity);
        verify(communityRepository, times(1)).findByIsApproved(true);
    }

    @Test
    void listPendingCommunities_ShouldReturnPendingCommunities() {
        when(communityRepository.findByIsApproved(false)).thenReturn(Arrays.asList(pendingCommunity));
        List<Community> pendingCommunities = communityService.listPendingCommunities();
        assertThat(pendingCommunities).containsExactly(pendingCommunity);
        verify(communityRepository, times(1)).findByIsApproved(false);
    }

    @Test
    void approveCommunity_ShouldSetCommunityToApproved() {
        when(communityRepository.findById(pendingCommunity.getId())).thenReturn(Optional.of(pendingCommunity));
        communityService.approveCommunity(pendingCommunity.getId());
        assertTrue(pendingCommunity.getIsApproved());
        verify(communityRepository, times(1)).save(pendingCommunity);
    }

    @Test
    void approveCommunity_WithNonExistingCommunity_ShouldThrowException() {
        Long nonExistingCommunityId = 999L;
        when(communityRepository.findById(nonExistingCommunityId)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> communityService.approveCommunity(nonExistingCommunityId));
    }

    @Test
    void rejectCommunity_ShouldDeleteCommunity() {
        communityService.rejectCommunity(pendingCommunity.getId());
        verify(communityRepository, times(1)).deleteById(pendingCommunity.getId());
        assertFalse(communityRepository.existsById(pendingCommunity.getId()));

    }

    @Test
    void listApprovedCommunities_WithNoApprovedCommunities_ShouldReturnEmptyList() {
        when(communityRepository.findByIsApproved(true)).thenReturn(Arrays.asList());
        List<Community> approvedCommunities = communityService.listApprovedCommunities();
        assertThat(approvedCommunities).isEmpty();
        verify(communityRepository, times(1)).findByIsApproved(true);
    }

    @Test
    void listPendingCommunities_WithNoPendingCommunities_ShouldReturnEmptyList() {
        when(communityRepository.findByIsApproved(false)).thenReturn(Arrays.asList());
        List<Community> pendingCommunities = communityService.listPendingCommunities();
        assertThat(pendingCommunities).isEmpty();
        verify(communityRepository, times(1)).findByIsApproved(false);
    }
}
