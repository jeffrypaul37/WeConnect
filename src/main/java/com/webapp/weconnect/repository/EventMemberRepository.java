package com.webapp.weconnect.repository;

import com.webapp.weconnect.model.Community;
import com.webapp.weconnect.model.EventMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventMemberRepository extends JpaRepository<EventMember, Long> {

    /**
     * To obtain the list of event members who have registered for an event.
     * So that a record can be shown on the frontend about the seats remaning.
     * @param eventMember the member
     * @param communityName the name of community
     * */
    List<EventMember> findByEventMemberAndCommunityName(String eventMember, String communityName);

}
