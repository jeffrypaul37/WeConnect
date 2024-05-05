package com.example.weconnect.weConnectApp.model;

import com.webapp.weconnect.model.Community;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommunityTest {

    private Community community;

    @BeforeEach
    public void setUp() {
        community = new Community();
    }

    @Test
    public void testId() {
        Long idValue = 1L;
        community.setId(idValue);
        assertEquals(idValue, community.getId());
    }

    @Test
    public void testName() {
        String nameValue = "Tech Innovators";
        community.setName(nameValue);
        assertEquals(nameValue, community.getName());
    }

    @Test
    public void testIsApproved() {
        community.setIsApproved(true);
        assertTrue(community.getIsApproved());
    }

    @Test
    public void testCreatedBy() {
        String createdByValue = "admin";
        community.setCreatedBy(createdByValue);
        assertEquals(createdByValue, community.getCreatedBy());
    }

    @Test
    public void testIsLeader() {
        community.setIsLeader(true);
        assertTrue(community.getIsLeader());
    }
}