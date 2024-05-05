package com.webapp.weconnect.repository;

import com.webapp.weconnect.model.CommunityImage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface to handle functionality related to community images.
 * */
public interface CommunityImageRepository extends JpaRepository<CommunityImage, Long> {

    /**
     * JPA to find the communities based on the community name and return the community.
     * @param communityName the name of community
     * */
    CommunityImage findByCommunityName(String communityName);

    /**
     * JPA method to check if the community exist or not with the provided name
     * @param communityName the name of community
     * */
    boolean existsByCommunityName(String communityName);
}
