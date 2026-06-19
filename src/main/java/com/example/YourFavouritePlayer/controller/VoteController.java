package com.example.YourFavouritePlayer.controller;

import com.example.YourFavouritePlayer.entity.AppUser;
import com.example.YourFavouritePlayer.service.UserService;
import com.example.YourFavouritePlayer.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class VoteController {

	private final VoteService voteService;
	private final UserService userService;

	@PostMapping("/players/{playerId}/vote")
	public String vote(
			@PathVariable Long playerId,
			@RequestParam(defaultValue = "/players") String returnUrl,
			Authentication authentication
	) {
		AppUser user = userService.getCurrentUser(authentication.getName());
		voteService.toggleVote(playerId, user);
		return "redirect:" + safeReturnUrl(returnUrl);
	}

	private String safeReturnUrl(String returnUrl) {
		if (!StringUtils.hasText(returnUrl) || !returnUrl.startsWith("/") || returnUrl.startsWith("//")) {
			return "/players";
		}
		return returnUrl;
	}
}
