package com.webapp.weconnect.service;

import com.webapp.weconnect.model.Community;
import com.webapp.weconnect.repository.CommunityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunityService {

    @Autowired
    private CommunityRepository communityRepository;

    /**
     * Method to create a new community and set its approval status to false.
     * This is because when a community is created, it is yet to be approved by the admin.
     * Till then, it should not be shown to the users on their home page
     * @param community the community that needs to be created.
     * */
    public void createCommunity(Community community) {
        community.setIsApproved(false);
        communityRepository.save(community);
        System.out.println("Save：" + community.getName());
    }

    /**
     * Retrieves a list of approved communities because only the approved communities needs to be shown to the user.
     * The communities will be marked as approved once the admin approves it.
     * @return List of communities which are approved.
     * */
    public List<Community> listApprovedCommunities() {
        List<Community> approvedCommunities = communityRepository.findByIsApproved(true);
        System.out.println("find " + approvedCommunities.size() + "communities。");
        return approvedCommunities;
    }

    /**
     * Retrieve the communities that are in pending state and waiting for approval.
     * Because the admin needs to approve the communities before they are made available to users.
     * @return list of communities whose status is pending.
     * */
    public List<Community> listPendingCommunities() {
        List<Community> pendingCommunities = communityRepository.findByIsApproved(false);
        System.out.println("find " + pendingCommunities.size() + "communities。");
        return pendingCommunities;
    }

    /**
     * Method for admin side for the admin to approve it, because he is the one who will approve it.
     * For approval, he will click approve and the id corresponding to that will be passed here.
     * @param id the ID of the community waiting for approval
     * */
    public void approveCommunity(Long id) {
        Community community = communityRepository.findById(id).orElseThrow(() -> new RuntimeException("Community not found"));
        community.setIsApproved(true);
        communityRepository.save(community);
    }

    /**
     * Admin can reject the community approval if he finds any mishaps or feels like the content is not right.
     * @param id the ID of the commmunity which is rejected
     * */
    public void rejectCommunity(Long id) {
        communityRepository.deleteById(id);
    }
}
