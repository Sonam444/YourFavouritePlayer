package com.example.YourFavouritePlayer.repository;

import com.example.YourFavouritePlayer.entity.PlayerCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerCardRepository extends JpaRepository<PlayerCard, Long> {

	@Query("SELECT DISTINCT p FROM PlayerCard p LEFT JOIN FETCH p.creator LEFT JOIN FETCH p.votes WHERE p.id = :id")
	Optional<PlayerCard> findByIdWithDetails(@Param("id") Long id);

	@Query("SELECT p FROM PlayerCard p ORDER BY SIZE(p.votes) DESC, p.id DESC")
	List<PlayerCard> findAllRanked();

	@Query("""
			SELECT p FROM PlayerCard p
			WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
			   OR LOWER(p.club) LIKE LOWER(CONCAT('%', :keyword, '%'))
			   OR LOWER(p.country) LIKE LOWER(CONCAT('%', :keyword, '%'))
			   OR LOWER(p.position) LIKE LOWER(CONCAT('%', :keyword, '%'))
			   OR LOWER(COALESCE(p.description, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
			ORDER BY SIZE(p.votes) DESC, p.id DESC
			""")
	List<PlayerCard> searchRanked(@Param("keyword") String keyword);
}
