package com.example.YourFavouritePlayer.service;

import com.example.YourFavouritePlayer.dto.CommentForm;
import com.example.YourFavouritePlayer.entity.AppUser;
import com.example.YourFavouritePlayer.entity.Comment;
import com.example.YourFavouritePlayer.entity.PlayerCard;
import com.example.YourFavouritePlayer.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

	private final CommentRepository commentRepository;
	private final PlayerCardService playerCardService;

	public List<Comment> findByPlayer(PlayerCard playerCard) {
		return commentRepository.findByPlayerCardOrderByCreatedAtAsc(playerCard);
	}

	@Transactional
	public Comment addComment(Long playerId, CommentForm form, AppUser user) {
		PlayerCard playerCard = playerCardService.getPlayer(playerId);
		Comment comment = new Comment(form.getContent().trim(), playerCard, user);
		return commentRepository.save(comment);
	}

	@Transactional
	public void deleteComment(Long commentId, AppUser user) {
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new IllegalArgumentException("Comment not found"));
		boolean commentOwner = comment.getUser().getId().equals(user.getId());
		boolean playerOwner = comment.getPlayerCard().isOwnedBy(user);
		if (!commentOwner && !playerOwner) {
			throw new AccessDeniedException("Only the comment owner or player creator can delete this comment");
		}
		commentRepository.delete(comment);
	}
}
