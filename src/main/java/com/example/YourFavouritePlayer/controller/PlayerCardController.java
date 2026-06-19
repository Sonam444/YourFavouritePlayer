package com.example.YourFavouritePlayer.controller;

import com.example.YourFavouritePlayer.dto.CommentForm;
import com.example.YourFavouritePlayer.dto.PlayerCardForm;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;

@Controller
@RequiredArgsConstructor
public class PlayerCardController {

	private final PlayerCardService playerCardService;
	private final CommentService commentService;
	private final VoteService voteService;
	private final UserService userService;

	@GetMapping({"/", "/players"})
	public String index(@RequestParam(required = false) String keyword, Model model, Authentication authentication) {
		model.addAttribute("players", playerCardService.findRanked(keyword));
		model.addAttribute("keyword", keyword == null ? "" : keyword);
		addVotedPlayerIds(model, authentication);
		return "players/index";
	}

	@GetMapping("/players/new")
	public String createForm(Model model) {
		if (!model.containsAttribute("playerForm")) {
			model.addAttribute("playerForm", new PlayerCardForm());
		}
		model.addAttribute("formTitle", "Create Player Card");
		model.addAttribute("submitLabel", "Create");
		model.addAttribute("formAction", "/players");
		return "players/form";
	}

	@PostMapping("/players")
	public String create(
			@Valid @ModelAttribute("playerForm") PlayerCardForm playerForm,
			BindingResult bindingResult,
			Authentication authentication,
			Model model
	) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("formTitle", "Create Player Card");
			model.addAttribute("submitLabel", "Create");
			model.addAttribute("formAction", "/players");
			return "players/form";
		}

		try {
			AppUser creator = currentUser(authentication);
			PlayerCard playerCard = playerCardService.create(playerForm, creator);
			return "redirect:/players/" + playerCard.getId();
		} catch (IllegalArgumentException | IllegalStateException exception) {
			bindingResult.rejectValue("image", "image.invalid", exception.getMessage());
			model.addAttribute("formTitle", "Create Player Card");
			model.addAttribute("submitLabel", "Create");
			model.addAttribute("formAction", "/players");
			return "players/form";
		}
	}

	@GetMapping("/players/{id}")
	public String detail(@PathVariable Long id, Model model, Authentication authentication) {
		PlayerCard playerCard = playerCardService.getPlayer(id);
		model.addAttribute("player", playerCard);
		model.addAttribute("comments", commentService.findByPlayer(playerCard));
		model.addAttribute("commentForm", new CommentForm());
		addVotedPlayerIds(model, authentication);
		return "players/detail";
	}

	@GetMapping("/players/{id}/edit")
	public String editForm(@PathVariable Long id, Model model, Authentication authentication) {
		PlayerCard playerCard = playerCardService.getPlayer(id);
		playerCardService.checkCanManage(id, currentUser(authentication));
		model.addAttribute("player", playerCard);
		model.addAttribute("playerForm", PlayerCardForm.from(playerCard));
		model.addAttribute("formTitle", "Edit Player Card");
		model.addAttribute("submitLabel", "Save");
		model.addAttribute("formAction", "/players/" + id + "/edit");
		return "players/form";
	}

	@PostMapping("/players/{id}/edit")
	public String update(
			@PathVariable Long id,
			@Valid @ModelAttribute("playerForm") PlayerCardForm playerForm,
			BindingResult bindingResult,
			Authentication authentication,
			Model model
	) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("formTitle", "Edit Player Card");
			model.addAttribute("submitLabel", "Save");
			model.addAttribute("formAction", "/players/" + id + "/edit");
			return "players/form";
		}

		try {
			playerCardService.update(id, playerForm, currentUser(authentication));
			return "redirect:/players/" + id;
		} catch (IllegalArgumentException | IllegalStateException exception) {
			bindingResult.rejectValue("image", "image.invalid", exception.getMessage());
			model.addAttribute("formTitle", "Edit Player Card");
			model.addAttribute("submitLabel", "Save");
			model.addAttribute("formAction", "/players/" + id + "/edit");
			return "players/form";
		}
	}

	@PostMapping("/players/{id}/delete")
	public String delete(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
		playerCardService.delete(id, currentUser(authentication));
		redirectAttributes.addFlashAttribute("successMessage", "Player card deleted.");
		return "redirect:/players";
	}

	private AppUser currentUser(Authentication authentication) {
		return userService.getCurrentUser(authentication.getName());
	}

	private void addVotedPlayerIds(Model model, Authentication authentication) {
		if (authentication == null || !authentication.isAuthenticated()) {
			model.addAttribute("votedPlayerIds", Collections.emptySet());
			return;
		}

		try {
			model.addAttribute("votedPlayerIds", voteService.findVotedPlayerIds(currentUser(authentication)));
		} catch (RuntimeException exception) {
			model.addAttribute("votedPlayerIds", Collections.emptySet());
		}
	}
}
