package com.example.YourFavouritePlayer.service;

import com.example.YourFavouritePlayer.entity.AppUser;
import com.example.YourFavouritePlayer.entity.PlayerCard;
import com.example.YourFavouritePlayer.entity.Vote;
import com.example.YourFavouritePlayer.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VoteService {

	private final VoteRepository voteRepository;
	private final PlayerCardService playerCardService;

	@Transactional
	public void toggleVote(Long playerId, AppUser user) {
		PlayerCard playerCard = playerCardService.getPlayer(playerId);
		voteRepository.findByPlayerCardAndUser(playerCard, user)
				.ifPresentOrElse(voteRepository::delete, () -> voteRepository.save(new Vote(playerCard, user)));
	}

	public Set<Long> findVotedPlayerIds(AppUser user) {
		return voteRepository.findByUser(user).stream()
				.map(vote -> vote.getPlayerCard().getId())
				.collect(Collectors.toSet());
	}
}
