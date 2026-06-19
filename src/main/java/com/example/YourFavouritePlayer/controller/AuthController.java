package com.example.YourFavouritePlayer.controller;

import com.example.YourFavouritePlayer.dto.SignupForm;
import com.example.YourFavouritePlayer.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

	private final UserService userService;

	@GetMapping("/login")
	public String login() {
		return "auth/login";
	}

	@GetMapping("/signup")
	public String signupForm(Model model) {
		if (!model.containsAttribute("signupForm")) {
			model.addAttribute("signupForm", new SignupForm());
		}
		return "auth/signup";
	}

	@PostMapping("/signup")
	public String signup(
			@Valid @ModelAttribute("signupForm") SignupForm signupForm,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes
	) {
		if (!signupForm.passwordsMatch()) {
			bindingResult.rejectValue("confirmPassword", "password.mismatch", "Passwords do not match");
		}

		if (bindingResult.hasErrors()) {
			return "auth/signup";
		}

		try {
			userService.register(signupForm);
		} catch (IllegalArgumentException exception) {
			bindingResult.reject("signupError", exception.getMessage());
			return "auth/signup";
		}

		redirectAttributes.addFlashAttribute("successMessage", "Account created. Please log in.");
		return "redirect:/login";
	}
}
