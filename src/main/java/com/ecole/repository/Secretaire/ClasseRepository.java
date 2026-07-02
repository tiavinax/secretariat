package com.ecole.repository.Secretaire;

import com.ecole.entity.Secretaire.Classe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClasseRepository extends JpaRepository<Classe, Integer> {

    @Query("SELECT c FROM Classe c JOIN c.anneeScolaire a WHERE a.estActive = true ORDER BY c.nom")
    List<Classe> findAllActiveAnnee();
}