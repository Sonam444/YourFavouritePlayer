package com.example.YourFavouritePlayer.repository;

import com.example.YourFavouritePlayer.entity.Comment;
import com.example.YourFavouritePlayer.entity.PlayerCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

	List<Comment> findByPlayerCardOrderByCreatedAtAsc(PlayerCard playerCard);
}
