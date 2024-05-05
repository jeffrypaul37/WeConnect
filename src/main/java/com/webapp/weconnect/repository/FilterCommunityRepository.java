package com.webapp.weconnect.repository;

import com.webapp.weconnect.model.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FilterCommunityRepository extends JpaRepository<Community, Long> {

    /**
     * To fetch the communities based in the name of them. So that the user is displayed the correct community event if there is a typo.
     * @param name the input string in filter.
     * @return list of community that matches the community regex mentioned by user.
     * */
    List<Community> findByNameContainingIgnoreCase(String name);
}
