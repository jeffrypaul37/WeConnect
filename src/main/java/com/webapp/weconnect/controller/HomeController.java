package com.webapp.weconnect.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /**
     * Method to render the community home page to the user
     * so that when a user logs in, then he is displayed the home page.
     * */
    @GetMapping("/communityhome")
    public String community() {
        return "community";
    }

    /**
     * Method to render the about us page to the user
     * so that when a user clicks on about us in nav bar, then he is displayed the page.
     * */
    @GetMapping("/about")
    public String aboutUs(){ return "about"; }

}
