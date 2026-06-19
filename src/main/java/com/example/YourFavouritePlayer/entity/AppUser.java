package com.example.YourFavouritePlayer.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
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
@Table(name = "app_users")
public class AppUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 40)
	private String username;

	@Column(nullable = false, unique = true, length = 120)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@OneToMany(mappedBy = "creator", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<PlayerCard> playerCards = new ArrayList<>();

	public AppUser(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}

	@PrePersist
	public void prePersist() {
		createdAt = LocalDateTime.now();
	}
}
