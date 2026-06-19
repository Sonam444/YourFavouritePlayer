package com.example.YourFavouritePlayer.service;

import com.example.YourFavouritePlayer.dto.PlayerCardForm;
import com.example.YourFavouritePlayer.entity.AppUser;
import com.example.YourFavouritePlayer.entity.PlayerCard;
import com.example.YourFavouritePlayer.repository.PlayerCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlayerCardService {

	private final PlayerCardRepository playerCardRepository;
	private final FileStorageService fileStorageService;

	public List<PlayerCard> findRanked(String keyword) {
		if (StringUtils.hasText(keyword)) {
			return playerCardRepository.searchRanked(keyword.trim());
		}
		return playerCardRepository.findAllRanked();
	}

	public PlayerCard getPlayer(Long id) {
		return playerCardRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Player card not found"));
	}

	public void checkCanManage(Long id, AppUser currentUser) {
		checkOwner(getPlayer(id), currentUser);
	}

	@Transactional
	public PlayerCard create(PlayerCardForm form, AppUser creator) {
		String imagePath = fileStorageService.store(form.getImage());
		PlayerCard playerCard = new PlayerCard(
				form.getName().trim(),
				form.getClub().trim(),
				form.getPosition().trim(),
				form.getCountry().trim(),
				trimToNull(form.getDescription()),
				imagePath,
				creator
		);
		return playerCardRepository.save(playerCard);
	}

	@Transactional
	public PlayerCard update(Long id, PlayerCardForm form, AppUser currentUser) {
		PlayerCard playerCard = getPlayer(id);
		checkOwner(playerCard, currentUser);

		playerCard.setName(form.getName().trim());
		playerCard.setClub(form.getClub().trim());
		playerCard.setPosition(form.getPosition().trim());
		playerCard.setCountry(form.getCountry().trim());
		playerCard.setDescription(trimToNull(form.getDescription()));

		String imagePath = fileStorageService.store(form.getImage());
		if (imagePath != null) {
			playerCard.setImagePath(imagePath);
		}

		return playerCard;
	}

	@Transactional
	public void delete(Long id, AppUser currentUser) {
		PlayerCard playerCard = getPlayer(id);
		checkOwner(playerCard, currentUser);
		playerCardRepository.delete(playerCard);
	}

	private void checkOwner(PlayerCard playerCard, AppUser currentUser) {
		if (!playerCard.isOwnedBy(currentUser)) {
			throw new AccessDeniedException("Only the creator can manage this player card");
		}
	}

	private String trimToNull(String value) {
		if (!StringUtils.hasText(value)) {
			return null;
		}
		return value.trim();
	}
}
