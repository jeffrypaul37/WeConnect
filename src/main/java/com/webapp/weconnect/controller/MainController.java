package com.webapp.weconnect.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	/**
	 * Method to render the login page so that the user can login to the website to use the features.
	 * */
	@GetMapping("/login")
	public String login() {
		return "login";
	}

}
