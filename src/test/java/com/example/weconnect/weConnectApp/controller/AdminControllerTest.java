package com.example.weconnect.weConnectApp.controller;

import com.webapp.weconnect.WeConnect;
import com.webapp.weconnect.controller.AdminController;
import com.webapp.weconnect.controller.CommunityController;
import com.webapp.weconnect.repository.CommunityRepository;
import com.webapp.weconnect.service.CommunityImageService;
import com.webapp.weconnect.service.CommunityService;
import com.webapp.weconnect.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.ui.ModelMap;

@SpringBootTest(classes = WeConnect.class)
@AutoConfigureMockMvc
public class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommunityService communityService;

    @Mock
    private CommunityImageService communityImageService;

    @InjectMocks
    private AdminController adminController;

    @MockBean
    private UserService userService;

    @MockBean
    private CommunityRepository communityRepository;

    @Value("${file.storage.location}")
    private String fileStorageLocation;

    public interface FileStorageService {
        Resource loadFileAsResource(String communityName, String fileName) throws MalformedURLException;
    }

    @MockBean
    private FileStorageService fileStorageService;

    @Test
    public void showPendingCommunities_ShouldAddPendingCommunitiesToModelAndRenderView() throws Exception {
        MvcResult result = mockMvc.perform(get("/admin/pendingCommunities"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminPendingCommunities"))
                .andReturn();

        ModelMap modelMap = result.getModelAndView().getModelMap();

        assertNotNull(modelMap.get("pendingCommunities"));
    }

    @Test
    public void approveCommunity_ShouldRedirectToPendingCommunities() throws Exception {
        MvcResult result = mockMvc.perform(post("/admin/approveCommunity/{id}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/pendingCommunities"))
                .andReturn();

        assertTrue(result.getModelAndView().getModelMap().isEmpty());
    }

    @Test
    public void rejectCommunity_ShouldRedirectToPendingCommunities() throws Exception {
        MvcResult result = mockMvc.perform(post("/admin/rejectCommunity/{id}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/pendingCommunities"))
                .andReturn();

        assertTrue(result.getModelAndView().getModelMap().isEmpty());
    }

    @Test
    public void adminHome_ShouldRenderViewWithCommunities() throws Exception {
        MvcResult result = mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin"))
                .andReturn();

        assertNotNull(result.getModelAndView().getModelMap().get("communities"));
    }

    @Test
    void downloadIdFile_WhenFileDoesNotExist_ShouldReturnNotFound() throws Exception {
        String communityName = "nonExistingCommunity";
        String fileName = "nonExistingFile.txt";

        MvcResult result = mockMvc.perform(get("/admin/downloadIdFile")
                        .param("communityName", communityName)
                        .param("fileName", fileName))
                .andExpect(status().isNotFound())
                .andReturn();

        assertEquals("", result.getResponse().getContentAsString());
    }

    @Test
    public void adminLogin_WithValidCredentials_ShouldRedirectToAdminHome() throws Exception {
        MvcResult result = mockMvc.perform(post("/admin/login")
                        .param("username", "weconnectadmin@gmail.com")
                        .param("password", "admin123456"))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        String redirectedUrl = result.getResponse().getRedirectedUrl();
        assertEquals("/admin", redirectedUrl);
    }

    @Test
    public void getImage_WhenCommunityHasImage_ShouldReturnImage() {

        byte[] imageBytes = new byte[]{1, 2, 3};
        when(communityImageService.getImageStringByCommunityName(anyString())).thenReturn(imageBytes);

        ResponseEntity<byte[]> response = adminController.getImage("Tech");

        assertArrayEquals(imageBytes, response.getBody());
    }


    @Test
    public void adminLogin_WithInvalidCredentials_ShouldRedirectToLogin() throws Exception {
        MvcResult result = mockMvc.perform(post("/admin/login")
                        .param("username", "wrong")
                        .param("password", "credentials"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andReturn();

        assertNotNull(result.getFlashMap().get("error"));
    }
}
