package com.example.YourFavouritePlayer;

import com.example.YourFavouritePlayer.entity.AppUser;
import com.example.YourFavouritePlayer.entity.PlayerCard;
import com.example.YourFavouritePlayer.repository.AppUserRepository;
import com.example.YourFavouritePlayer.repository.PlayerCardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class YourFavouritePlayerApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private AppUserRepository appUserRepository;

	@Autowired
	private PlayerCardRepository playerCardRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void publicPagesRender() throws Exception {
		mockMvc.perform(get("/players"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Football card ranking")));

		mockMvc.perform(get("/login"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Login")));

		mockMvc.perform(get("/signup"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Sign Up")));
	}

	@Test
	void detailPageRenders() throws Exception {
		AppUser user = appUserRepository.save(new AppUser("owner", "owner@example.com", "password"));
		PlayerCard playerCard = playerCardRepository.save(new PlayerCard(
				"Son Heung-min",
				"Tottenham Hotspur",
				"Forward",
				"South Korea",
				"Fast, direct, and clinical.",
				null,
				user
		));

		mockMvc.perform(get("/players/{id}", playerCard.getId()))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Son Heung-min")));
	}

	@Test
	void createFormRendersForLoggedInUser() throws Exception {
		mockMvc.perform(get("/players/new").with(user("owner")))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Create Player Card")));
	}
}
