package com.webapp.weconnect.controller;

import com.webapp.weconnect.model.Community;
import com.webapp.weconnect.service.CommunityService;
import com.webapp.weconnect.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/search")
public class SearchController {
    private final SearchService searchService;
    private final CommunityService communityService;

    @Autowired
    public SearchController(SearchService searchService, CommunityService communityService) {
        this.searchService = searchService;
        this.communityService = communityService;
    }

    /**
     * Method to search the communities by their name.
     * So that when a user just knows the community name, then also they can search it based on the name of community.
     * This makes it easier for them to locate their favourite community.
     * @param title the title of the community that needs to be searched.
     * @return a response on the status of the result.
     * */
    @GetMapping
    public ResponseEntity<List<Community>> searchByTitle(@RequestParam String title) {

        List<Community> approvedCommunities = communityService.listApprovedCommunities();
        List<Community> communities = searchService.searchByTitle(title);

        List<Community> uniqueCommunities = communities.stream()
                .filter(community -> Boolean.TRUE.equals(community.getIsLeader()))
                .collect(Collectors.toList());

        Set<String> approvedCommunityNames = approvedCommunities.stream()
                .map(Community::getName)
                .collect(Collectors.toSet());

        List<Community> intersection = uniqueCommunities.stream()
                .filter(community -> approvedCommunityNames.contains(community.getName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(intersection);
    }
}
