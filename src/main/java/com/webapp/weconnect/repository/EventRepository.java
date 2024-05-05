package com.webapp.weconnect.repository;

import com.webapp.weconnect.model.Community;
import com.webapp.weconnect.model.Event;
import com.webapp.weconnect.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    /**
     * To find the events which are associated with a particular community.
     * So that all the events can be shown in the community.
     * @param communityName the name of community.
     * @return list of events organized in a community.
     * */
    List<Event> findByCommunityName(String communityName);

    /**
     * To find event by its unique event ID.
     * SO that event can be found based in just the ID.
     * @param id the ID of event
     * @return option event.
     * */
    Optional<Event> findById(Long id);
}
