package com.ecole.repository.Secretaire;

import com.ecole.entity.Secretaire.Inscription;
import com.ecole.entity.Secretaire.AnneeScolaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Integer> {
    List<Inscription> findByAnneeScolaireAndStatut(AnneeScolaire anneeScolaire, String statut);
}