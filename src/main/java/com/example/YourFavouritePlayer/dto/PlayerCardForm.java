package com.example.YourFavouritePlayer.dto;

import com.example.YourFavouritePlayer.entity.PlayerCard;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PlayerCardForm {

	@NotBlank(message = "Player name is required")
	@Size(max = 80, message = "Player name is too long")
	private String name;

	@NotBlank(message = "Club is required")
	@Size(max = 80, message = "Club is too long")
	private String club;

	@NotBlank(message = "Position is required")
	@Size(max = 40, message = "Position is too long")
	private String position;

	@NotBlank(message = "Country is required")
	@Size(max = 80, message = "Country is too long")
	private String country;

	@Size(max = 1200, message = "Description is too long")
	private String description;

	private MultipartFile image;

	public static PlayerCardForm from(PlayerCard playerCard) {
		PlayerCardForm form = new PlayerCardForm();
		form.setName(playerCard.getName());
		form.setClub(playerCard.getClub());
		form.setPosition(playerCard.getPosition());
		form.setCountry(playerCard.getCountry());
		form.setDescription(playerCard.getDescription());
		return form;
	}
}
