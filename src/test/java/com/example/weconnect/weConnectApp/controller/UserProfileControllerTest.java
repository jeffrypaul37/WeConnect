package com.example.weconnect.weConnectApp.controller;

import com.webapp.weconnect.controller.UserProfileController;
import com.webapp.weconnect.model.Location;
import com.webapp.weconnect.model.User;
import com.webapp.weconnect.service.LocationService;
import com.webapp.weconnect.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserProfileControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private LocationService locationService;

    @InjectMocks
    private UserProfileController userProfileController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userProfileController).build();
    }

    @Test
    public void testViewProfile() throws Exception {
        User currentUser = new User();

        when(userService.getCurrentLoggedInUser()).thenReturn(currentUser);


        MvcResult result = mockMvc.perform(get("/profile/view"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("currentUser"))
                .andExpect(view().name("profile"))
                .andReturn();

        Map<String, Object> model = result.getModelAndView().getModel();
        User modelCurrentUser = (User) model.get("currentUser");
        assertSame(currentUser, modelCurrentUser);

        verify(userService, times(1)).getCurrentLoggedInUser();
    }

    @Test
    public void testEditProfile() throws Exception {
        User currentUser = new User();
        List<Location> locations = Arrays.asList(new Location(), new Location());

        when(userService.getCurrentLoggedInUser()).thenReturn(currentUser);
        when(locationService.getAllLocations()).thenReturn(locations);

        MvcResult result = mockMvc.perform(get("/profile/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("currentUser", "locations"))
                .andExpect(view().name("profile_edit"))
                .andReturn();

        Map<String, Object> model = result.getModelAndView().getModel();
        List<Location> modelLocations = (List<Location>) model.get("locations");

        assertSame(locations, modelLocations);

    }


    @Test
    public void testUpdateProfile_Success() throws Exception {
        User currentUser = new User();
        when(userService.getCurrentLoggedInUser()).thenReturn(currentUser);
        MockMultipartFile file = new MockMultipartFile("uploadImage", "filename.txt", "text/plain", "some xml".getBytes());

        mockMvc.perform(multipart("/profile/update").file(file)
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("userLocation", "Earth"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile/view"));

        verify(userService, times(1)).updateUserProfile(any(User.class));
        assertEquals("John", currentUser.getFirstName());

    }

}
