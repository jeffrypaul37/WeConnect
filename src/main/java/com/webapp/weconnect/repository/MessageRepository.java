package com.webapp.weconnect.repository;

import com.webapp.weconnect.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    /**
     * To fetch the message of a particular community by its community name.
     * So that all the messages can be then sent to the frontend for storage.
     * @param communityName the name of community for which the messages are desired.
     * */
    List<Message> findByCommunityName(String communityName);
}
