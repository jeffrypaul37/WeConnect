package com.webapp.weconnect.service;

import com.webapp.weconnect.model.Donation;
import com.webapp.weconnect.model.User;
import com.webapp.weconnect.repository.DonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DonationService {

    @Autowired
    private DonationRepository donationRepository;

    /**
     * Save a donation if it is not null. The method provides the check to make sure the donation object is not null.
     * This way we ensure that non-null donation objects are only considered for persistence.
     * @param donation the donation object to be stored.
     * */
    public void saveDonation(Donation donation) {

        if (donation != null) {
            donationRepository.save(donation);
        }
    }
}
