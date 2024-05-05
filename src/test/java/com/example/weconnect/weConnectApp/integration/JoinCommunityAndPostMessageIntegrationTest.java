package com.example.weconnect.weConnectApp.integration;

import com.webapp.weconnect.WeConnect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = WeConnect.class)
@AutoConfigureMockMvc
public class JoinCommunityAndPostMessageIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "xlent3348@proton.me", password = "a11111111", roles = "USER")
    public void testJoinCommunityAndCreateComment() throws Exception {
        String communityName = "Music";
        Long messageId = 1L;

        mockMvc.perform(post("/joinCommunity")
                        .param("communityName", communityName)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());

        MvcResult result = mockMvc.perform(post("/create-comment")
                        .param("messageId", messageId.toString())
                        .param("content", "This is a test comment")
                        .param("communityName", communityName)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        String redirectedUrl = result.getResponse().getRedirectedUrl();
        assertTrue(redirectedUrl.contains("/viewCommunity?communityName=" + communityName));
    }
}
