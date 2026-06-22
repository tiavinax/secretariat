package com.ecole.repository;

import com.ecole.entity.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Long> {

    Optional<Inscription> findByEtudiantIdAndStatut(Long etudiantId, String statut);

    // AnneeScolaire n'est pas une @Entity mappée → on passe en native SQL
    // pour joindre la table annees_scolaires directement.
    @Query(value = """
        SELECT i.* FROM inscriptions i
        JOIN annees_scolaires a ON a.id = i.annee_scolaire_id
        WHERE i.etudiant_id = :etudiantId
          AND a.est_active = true
        LIMIT 1
        """, nativeQuery = true)
    Optional<Inscription> findActiveByEtudiantId(@Param("etudiantId") Long etudiantId);
}