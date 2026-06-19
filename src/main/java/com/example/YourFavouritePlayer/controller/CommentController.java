package com.example.YourFavouritePlayer.controller;

import com.example.YourFavouritePlayer.dto.CommentForm;
import com.example.YourFavouritePlayer.entity.AppUser;
import com.example.YourFavouritePlayer.entity.PlayerCard;
import com.example.YourFavouritePlayer.service.CommentService;
import com.example.YourFavouritePlayer.service.PlayerCardService;
import com.example.YourFavouritePlayer.service.UserService;
import com.example.YourFavouritePlayer.service.VoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;
	private final PlayerCardService playerCardService;
	private final VoteService voteService;
	private final UserService userService;

	@PostMapping("/players/{playerId}/comments")
	public String addComment(
			@PathVariable Long playerId,
			@Valid @ModelAttribute("commentForm") CommentForm commentForm,
			BindingResult bindingResult,
			Authentication authentication,
			Model model
	) {
		if (bindingResult.hasErrors()) {
			PlayerCard playerCard = playerCardService.getPlayer(playerId);
			AppUser user = currentUser(authentication);
			model.addAttribute("player", playerCard);
			model.addAttribute("comments", commentService.findByPlayer(playerCard));
			model.addAttribute("votedPlayerIds", voteService.findVotedPlayerIds(user));
			return "players/detail";
		}

		commentService.addComment(playerId, commentForm, currentUser(authentication));
		return "redirect:/players/" + playerId;
	}

	@PostMapping("/comments/{commentId}/delete")
	public String deleteComment(
			@PathVariable Long commentId,
			@RequestParam Long playerId,
			Authentication authentication
	) {
		commentService.deleteComment(commentId, currentUser(authentication));
		return "redirect:/players/" + playerId;
	}

	private AppUser currentUser(Authentication authentication) {
		return userService.getCurrentUser(authentication.getName());
	}
}
