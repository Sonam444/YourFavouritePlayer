package com.example.YourFavouritePlayer.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

	@ModelAttribute("loggedIn")
	public boolean loggedIn(Authentication authentication) {
		return authentication != null
				&& authentication.isAuthenticated()
				&& !(authentication instanceof AnonymousAuthenticationToken);
	}

	@ModelAttribute("currentUsername")
	public String currentUsername(Authentication authentication) {
		if (!loggedIn(authentication)) {
			return "";
		}
		return authentication.getName();
	}
}
