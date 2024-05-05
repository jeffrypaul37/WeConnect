package com.example.weconnect.weConnectApp.integration;

import com.webapp.weconnect.WeConnect;
import com.webapp.weconnect.model.Community;
import com.webapp.weconnect.service.CommunityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = WeConnect.class)
@AutoConfigureMockMvc
public class AdminRejectCommunityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CommunityService communityService;

    @Test
    @WithMockUser(username = "weconnectadmin@gmail.com", roles = "ADMIN")
    public void testAdminRejectsCommunity() throws Exception {

        mockMvc.perform(get("/admin/pendingCommunities")
                        .with(csrf()))
                .andExpect(status().isOk());

        List<Community> allPendingCommunities = communityService.listPendingCommunities();

        if (!allPendingCommunities.isEmpty()) {

            Long pendingCommunityId = allPendingCommunities.get(0).getId();

            mockMvc.perform(post("/admin/rejectCommunity/" + pendingCommunityId)
                            .with(csrf()))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/admin/pendingCommunities"));
            List<Community> updatedPendingCommunities = communityService.listPendingCommunities();
            assertFalse(updatedPendingCommunities.stream().anyMatch(c -> c.getId().equals(pendingCommunityId)));}
    }
}
