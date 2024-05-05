package com.example.weconnect.weConnectApp.integration;

import com.webapp.weconnect.WeConnect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = WeConnect.class)
@AutoConfigureMockMvc
public class CommunityMembershipSimulationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "xlent3348@proton.me", password = "a11111111", roles = "USER")
    public void simulateCommunityMembership() throws Exception {
        String communityName = "Gaming";


        MvcResult result = this.mockMvc.perform(get("/viewCommunity?communityName=" + communityName))
                .andExpect(status().isOk())
                .andExpect(model().attribute("isMember", false))
                .andReturn();

        this.mockMvc.perform(get("/viewCommunity?communityName=" + communityName))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("members", "isMember"))
                .andExpect(view().name("viewCommunity"));


        this.mockMvc.perform(post("/joinCommunity")
                        .param("communityName", communityName)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("null" ));

        this.mockMvc.perform(post("/joinCommunity")
                        .param("communityName", communityName)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("null?error=You+are+already+a+member+of+this+community%21"));

        this.mockMvc.perform(post("/leaveCommunity")
                        .param("communityName", communityName)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("null"));

        Map<String, Object> model = result.getModelAndView().getModel();
        Boolean isMember = (Boolean) model.get("isMember");
        assertFalse(isMember);
    }
}
