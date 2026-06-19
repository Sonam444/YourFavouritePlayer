package com.example.YourFavouritePlayer.repository;

import com.example.YourFavouritePlayer.entity.AppUser;
import com.example.YourFavouritePlayer.entity.PlayerCard;
import com.example.YourFavouritePlayer.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

	Optional<Vote> findByPlayerCardAndUser(PlayerCard playerCard, AppUser user);

	List<Vote> findByUser(AppUser user);
}
