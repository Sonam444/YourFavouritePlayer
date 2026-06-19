package com.example.YourFavouritePlayer.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "player_cards")
public class PlayerCard {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 80)
	private String name;

	@Column(nullable = false, length = 80)
	private String club;

	@Column(nullable = false, length = 40)
	private String position;

	@Column(nullable = false, length = 80)
	private String country;

	@Column(length = 1200)
	private String description;

	private String imagePath;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creator_id", nullable = false)
	private AppUser creator;

	@OneToMany(mappedBy = "playerCard", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<Vote> votes = new ArrayList<>();

	@OneToMany(mappedBy = "playerCard", cascade = CascadeType.REMOVE, orphanRemoval = true)
	@OrderBy("createdAt ASC")
	private List<Comment> comments = new ArrayList<>();

	public PlayerCard(String name, String club, String position, String country, String description, String imagePath, AppUser creator) {
		this.name = name;
		this.club = club;
		this.position = position;
		this.country = country;
		this.description = description;
		this.imagePath = imagePath;
		this.creator = creator;
	}

	@PrePersist
	public void prePersist() {
		createdAt = LocalDateTime.now();
	}

	@Transient
	public int getVoteCount() {
		return votes == null ? 0 : votes.size();
	}

	public boolean isOwnedBy(AppUser user) {
		return user != null && creator != null && creator.getId() != null && creator.getId().equals(user.getId());
	}
}
