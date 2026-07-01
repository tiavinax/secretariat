package com.ecole.repository.Secretaire;

import com.ecole.entity.Secretaire.AnneeScolaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AnneeScolaireRepository extends JpaRepository<AnneeScolaire, Integer> {
    Optional<AnneeScolaire> findByEstActiveTrue();
}