package com.webapp.weconnect.service;

import com.webapp.weconnect.model.Community;
import com.webapp.weconnect.repository.FilterCommunityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {
    private final FilterCommunityRepository repository;

    @Autowired
    public SearchService(FilterCommunityRepository repository) {
        this.repository = repository;
    }

    /**
     * Search the community by the title of the commmunity. This way the user gets the freedom to search any community he can think of.
     * @param title the search string that the user will input
     * @return the list of communities that match the name fully or partially.
     * */
    public List<Community> searchByTitle(String title) {
        return repository.findByNameContainingIgnoreCase(title);
    }
}
