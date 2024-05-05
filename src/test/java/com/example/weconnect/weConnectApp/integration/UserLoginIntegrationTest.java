package com.example.weconnect.weConnectApp.integration;

import com.webapp.weconnect.WeConnect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = WeConnect.class)
@AutoConfigureMockMvc
public class UserLoginIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testUserLoginWithBadCredentials() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "user@example.com")
                        .param("password", "wrongpassword"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login?error"))
                .andReturn();

        assertNotNull(result.getResponse().getRedirectedUrl(), "Redirect URL is null.");
    }

    @Test
    public void testUserLoginWithCorrectCredentials() throws Exception {

        String username = "xlent3348@proton.me";
        String password = "a11111111";

        MvcResult result = this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"))
                .andReturn();

        assertNotNull(result.getResponse().getRedirectedUrl(), "Redirect URL is null.");

    }
}