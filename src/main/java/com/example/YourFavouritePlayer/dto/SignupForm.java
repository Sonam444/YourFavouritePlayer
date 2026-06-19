package com.example.YourFavouritePlayer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupForm {

	@NotBlank(message = "Username is required")
	@Size(min = 3, max = 40, message = "Username must be 3 to 40 characters")
	private String username;

	@NotBlank(message = "Email is required")
	@Email(message = "Enter a valid email")
	@Size(max = 120, message = "Email is too long")
	private String email;

	@NotBlank(message = "Password is required")
	@Size(min = 4, max = 80, message = "Password must be at least 4 characters")
	private String password;

	@NotBlank(message = "Confirm your password")
	private String confirmPassword;

	public boolean passwordsMatch() {
		return password != null && password.equals(confirmPassword);
	}
}
