package com.ecole.repository.Secretaire;

import com.ecole.entity.Secretaire.Inscription;
import com.ecole.entity.Secretaire.ProfilEtudiant;
import com.ecole.entity.Secretaire.AnneeScolaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Integer> {
    List<Inscription> findByAnneeScolaireAndStatut(AnneeScolaire anneeScolaire, String statut);

    // Ajouter ces méthodes dans ton InscriptionRepository existant

    Optional<Inscription> findByEtudiantAndStatut(ProfilEtudiant etudiant, String statut);

    @Query(value = """
            SELECT i.* FROM inscriptions i
            JOIN annees_scolaires a ON a.id = i.annee_scolaire_id
            WHERE i.etudiant_id = :etudiantId
              AND a.est_active = true
            LIMIT 1
            """, nativeQuery = true)
    Optional<Inscription> findActiveByEtudiantId(@Param("etudiantId") Integer etudiantId);
}