package com.webapp.weconnect.repository;

import com.webapp.weconnect.model.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface CommunityRepository extends JpaRepository<Community, Long> {

    /**
     * Finds and returns a list of communities based on their approval status. This is used
     * to filter communities that are approved or not approved by the admin for display on the homepage.
     * so that it can be added on the homepage of users.
     * @param isApproved boolean value to verify
     * @return A list of community that are approved.
     * */
    @Query("SELECT c FROM Community c WHERE c.isApproved = ?1")
    List<Community> findByIsApproved(Boolean isApproved);

    /**
     * Finds communities by the username of the user who created them. Used so that all communities
     * created by that user can be displayed.
     * @param createdBy the name of the user who created the community.
     * @return A list of communities created by that specific user.
     * */
    List<Community> findByCreatedBy(String createdBy);

    /**
     * To Check if a community with the specified name exists.
     * So that there are not duplicate communities being formed by users.
     * @param name the community name that needs to be verified.
     * @return true if community exists, false otherwise.
     * */
    boolean existsByName(String name);

    /**
     * To find a community by its name. SO that it can be searched by name.
     * @param name the name of community
     * @return an optional containing the found, empty otherwise.
     * */
    Optional<Community> findByName(String name);

    /**
     * Retrieve the list of members who are part of a community. So that the list of members can be displayed to other users.
     * @param communityName the community for which the list of members is required.
     * @return A list containing member names.
     * */
    @Query("SELECT c.createdBy FROM Community c WHERE c.name = :communityName")
    List<String> findMembersByCommunityName(@Param("communityName") String communityName);

    /**
     * To find if a person is a leader of community, so that community features can be displayed accordingly to the leader.
     * @param communityName name of community
     * @param createdBy the name of leader
     * */
    @Query("SELECT c.isLeader FROM Community c WHERE c.name = :communityName AND c.createdBy = :createdBy")
    Optional<Boolean> getIsLeader(@Param("communityName") String communityName, @Param("createdBy") String createdBy);

    /**
     * To delete a community post which is no longer required by the leader.
     * So that the leader can have a hold on the community page.
     * @param communityName the community name
     * @param createdBy the name of leader performing the deletion
     * */
    @Modifying
    @Transactional
    @Query("DELETE FROM Community c WHERE c.name = :communityName AND c.createdBy = :createdBy")
    void deleteByCommunityNameAndCreatedBy(@Param("communityName") String communityName, @Param("createdBy") String createdBy);
}
