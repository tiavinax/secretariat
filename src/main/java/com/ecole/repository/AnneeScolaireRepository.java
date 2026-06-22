package com.ecole.repository;

import com.ecole.entity.AnneeScolaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnneeScolaireRepository extends JpaRepository<AnneeScolaire, Long> {

    // Pas de JOIN externe — AnneeScolaire est maintenant une @Entity mappée
    @Query("SELECT a FROM AnneeScolaire a WHERE a.estActive = true")
    Optional<AnneeScolaire> findActive();
}