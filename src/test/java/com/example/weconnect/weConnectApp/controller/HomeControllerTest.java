package com.example.weconnect.weConnectApp.controller;

import com.webapp.weconnect.controller.HomeController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HomeControllerTest {

    @Test
    public void testCommunityMapping() {
        HomeController controller = new HomeController();
        String viewName = controller.community();
        assertEquals("community", viewName, "Community mapping should return 'community'");
    }

    @Test
    public void testAboutUsMapping() {
        HomeController controller = new HomeController();
        String viewName = controller.aboutUs();
        assertEquals("about", viewName, "About Us mapping should return 'about'");
    }
}
