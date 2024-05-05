package com.webapp.weconnect.service;

import com.webapp.weconnect.model.Location;
import com.webapp.weconnect.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Class which implements the main logic behind the fetching of location.
 * Uses the .findAll() method to retrieve all the records from the database
 * @Author : Luv Patel
 * */
@Service
public class LocationService {

    private final LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    /**
     * Retrieves locations stored in the database. It provides with a list if locations.
     * @return list of locations present in the database.
     * */
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }
}
