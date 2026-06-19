package com.example.YourFavouritePlayer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
		name = "votes",
		uniqueConstraints = @UniqueConstraint(columnNames = {"player_card_id", "user_id"})
)
public class Vote {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "player_card_id", nullable = false)
	private PlayerCard playerCard;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private AppUser user;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	public Vote(PlayerCard playerCard, AppUser user) {
		this.playerCard = playerCard;
		this.user = user;
	}

	@PrePersist
	public void prePersist() {
		createdAt = LocalDateTime.now();
	}
}
