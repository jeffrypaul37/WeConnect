package com.webapp.weconnect.controller;

import com.webapp.weconnect.model.Community;
import com.webapp.weconnect.service.FilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/filter")
public class FilterController {
    private final FilterService filterService;

    /**
     * Method to filter out the communitites
     * @param filterService the filter servive for performing filter operations
     * */
    @Autowired
    public FilterController(FilterService filterService) {
        this.filterService = filterService;
    }

    /**
     * Method to find the community based on the default predefined conditions
     * @return a response indicating the status of the filter request
     * */
    @GetMapping
    public ResponseEntity<List<Community>> filterByPredefined(
            @RequestParam String filterType,
            @RequestParam String filterValue) {
        List<Community> communities = filterService.filterByPredefined(filterType, filterValue);
        return ResponseEntity.ok(communities);
    }
}
