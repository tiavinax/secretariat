package com.ecole.repository.Secretaire;

import com.ecole.entity.Secretaire.Echeance;
import com.ecole.entity.Secretaire.Echeancier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EcheanceRepository extends JpaRepository<Echeance, Integer> {
    List<Echeance> findByEcheancierAndEstSoldeeFalse(Echeancier echeancier);
    List<Echeance> findByEcheancier(Echeancier echeancier);
}