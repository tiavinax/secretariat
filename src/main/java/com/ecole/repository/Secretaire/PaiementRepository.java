package com.ecole.repository.Secretaire;

import com.ecole.entity.Secretaire.Paiement;
import com.ecole.entity.Secretaire.Inscription;
import com.ecole.entity.Secretaire.Echeance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Integer>,
        JpaSpecificationExecutor<Paiement> {

    List<Paiement> findByInscription(Inscription inscription);
    List<Paiement> findByEcheance(Echeance echeance);

    // Toutes les lignes d'un même groupe de paiement
    List<Paiement> findByReferenceTransaction(String referenceTransaction);

    @Query("SELECT COALESCE(SUM(p.montant), 0) FROM Paiement p WHERE p.inscription = :inscription")
    BigDecimal totalEncaisseParInscription(Inscription inscription);
}