package com.ecole.repository.Secretaire;

import com.ecole.entity.Secretaire.Echeancier;
import com.ecole.entity.Secretaire.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EcheancierRepository extends JpaRepository<Echeancier, Integer> {
    List<Echeancier> findByInscription(Inscription inscription);
}