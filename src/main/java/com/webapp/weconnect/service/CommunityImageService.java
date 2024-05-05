package com.webapp.weconnect.service;

import com.webapp.weconnect.model.CommunityImage;
import com.webapp.weconnect.repository.CommunityImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommunityImageService {

    private final CommunityImageRepository communityImageRepository;

    /**
     * Constructs a CommunityImageService with the necessary repository.
     * @param communityImageRepository The repository responsible for community image data operations.
     * */
    @Autowired
    public CommunityImageService(CommunityImageRepository communityImageRepository) {
        this.communityImageRepository = communityImageRepository;
    }

    /**
     * Retrieves the image data for a given community by its name.
     * If the community or its image data does not exist, this method returns null.
     * Otherwise, it returns the byte array representing the image associated with the community.
     * @param communityName The name of the community whose image data is to be retrieved.
     * @return A byte array containing the image data if available; otherwise, null.
     * */
    public byte[] getImageStringByCommunityName(String communityName) {
        CommunityImage communityImage = communityImageRepository.findByCommunityName(communityName);

        if (communityImage == null) {
            return null;
        }

        byte[] imageData = communityImage.getCommunityImage();
        if (imageData == null || imageData.length == 0) {
            return null;
        }

        return imageData;
    }

    /**
     * To fetch the description of community by its name. This fetches the Image entity associated with the community name
     * @param communityName The name of the community whose description is to be retrieved.
     * @return The description of the community.
     * */
    public String getDescriptionByCommunityName(String communityName) {
        CommunityImage communityDescription= communityImageRepository.findByCommunityName(communityName);
            return communityDescription.getCommunityDescription();
    }

}
