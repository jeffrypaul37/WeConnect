package com.webapp.weconnect.repository;

import com.webapp.weconnect.model.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DonationRepository extends JpaRepository<Donation, Long> {

    /**
     * To find the donation records that has been done by the users to a particular events.
     * So that a list can be displayed in donation tab in the frontend by the members.
     * @param communityName the name of community for which the list of donations are made.
     * @return list of donation records of donors.
     * */
    List<Donation> getDonationsByCommunityName(String communityName);
}
