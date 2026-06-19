package com.example.YourFavouritePlayer.service;

import com.example.YourFavouritePlayer.dto.SignupForm;
import com.example.YourFavouritePlayer.entity.AppUser;
import com.example.YourFavouritePlayer.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

	private final AppUserRepository appUserRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public AppUser register(SignupForm signupForm) {
		if (!signupForm.passwordsMatch()) {
			throw new IllegalArgumentException("Passwords do not match");
		}
		if (appUserRepository.existsByUsername(signupForm.getUsername())) {
			throw new IllegalArgumentException("Username is already used");
		}
		if (appUserRepository.existsByEmail(signupForm.getEmail())) {
			throw new IllegalArgumentException("Email is already used");
		}

		AppUser user = new AppUser(
				signupForm.getUsername().trim(),
				signupForm.getEmail().trim(),
				passwordEncoder.encode(signupForm.getPassword())
		);
		return appUserRepository.save(user);
	}

	@Transactional(readOnly = true)
	public AppUser getCurrentUser(String username) {
		return appUserRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser user = appUserRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		return org.springframework.security.core.userdetails.User
				.withUsername(user.getUsername())
				.password(user.getPassword())
				.roles("USER")
				.build();
	}
}
