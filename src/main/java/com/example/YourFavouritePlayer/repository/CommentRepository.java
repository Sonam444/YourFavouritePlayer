package com.example.YourFavouritePlayer.repository;

import com.example.YourFavouritePlayer.entity.Comment;
import com.example.YourFavouritePlayer.entity.PlayerCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

	@Query("SELECT c FROM Comment c JOIN FETCH c.user WHERE c.playerCard = :playerCard ORDER BY c.createdAt ASC")
	List<Comment> findByPlayerCardOrderByCreatedAtAsc(@Param("playerCard") PlayerCard playerCard);
}
