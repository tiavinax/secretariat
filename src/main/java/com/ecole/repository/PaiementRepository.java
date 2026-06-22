package com.ecole.repository;

import com.ecole.entity.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {

    // Tous les paiements d'un étudiant (toutes années)
    @Query(value = """
        SELECT p.* FROM paiements p
        JOIN inscriptions i ON i.id = p.inscription_id
        WHERE i.etudiant_id = :etudiantId
        ORDER BY p.date_paiement ASC
        """, nativeQuery = true)
    List<Paiement> findByEtudiantId(@Param("etudiantId") Long etudiantId);

    // Paiements de l'année scolaire active uniquement
    @Query(value = """
        SELECT p.* FROM paiements p
        JOIN inscriptions i ON i.id = p.inscription_id
        JOIN annees_scolaires a ON a.id = i.annee_scolaire_id
        WHERE i.etudiant_id = :etudiantId
          AND a.est_active = true
        ORDER BY p.date_paiement ASC
        """, nativeQuery = true)
    List<Paiement> findByEtudiantIdCurrentYear(@Param("etudiantId") Long etudiantId);
}