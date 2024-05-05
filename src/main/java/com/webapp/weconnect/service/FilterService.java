package com.webapp.weconnect.service;

import com.webapp.weconnect.model.Community;
import com.webapp.weconnect.repository.FilterCommunityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FilterService {
    private final FilterCommunityRepository repository;

    @Autowired
    public FilterService(FilterCommunityRepository repository) {
        this.repository = repository;
    }

    /**
     * Filters the list of communities based on the defined filter type and value.
     * Because the user can search any community which he thinks interests them.
     * @param filterValue the to be used for filtering
     * @param filterType the type of filter to apply.
     * @return A list of communities that matches the criteria.
     * */
    public List<Community> filterByPredefined(String filterType, String filterValue) {
        List<Community> allCommunities = repository.findAll();
        Pattern pattern = Pattern.compile(filterValue, Pattern.CASE_INSENSITIVE);

        Stream<Community> communityStream = allCommunities.stream();
        Predicate<Community> nameMatcher = community -> pattern.matcher(community.getName()).find();
        Stream<Community> filteredStream = communityStream.filter(nameMatcher);
        List<Community> filteredCommunities = filteredStream.collect(Collectors.toList());

        return filteredCommunities;
    }
}
