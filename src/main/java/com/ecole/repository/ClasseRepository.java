package com.ecole.repository;

import com.ecole.entity.Classe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClasseRepository extends JpaRepository<Classe, Long> {

    // Toutes les classes de l'année scolaire active
    @Query(value = """
        SELECT c.* FROM classes c
        JOIN annees_scolaires a ON a.id = c.annee_scolaire_id
        WHERE a.est_active = true
        ORDER BY c.nom
        """, nativeQuery = true)
    List<Classe> findAllActiveAnnee();
}