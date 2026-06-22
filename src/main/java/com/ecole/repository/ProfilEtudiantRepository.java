package com.ecole.repository;

import com.ecole.entity.ProfilEtudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfilEtudiantRepository extends JpaRepository<ProfilEtudiant, Long> {

    Optional<ProfilEtudiant> findByMatricule(String matricule);

    List<ProfilEtudiant> findByIsArchivedFalse();

    // Recherche multi-critères (matricule, nom, prenom)
    @Query("SELECT e FROM ProfilEtudiant e WHERE e.isArchived = false AND (" +
           "LOWER(e.nom) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.prenom) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.matricule) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<ProfilEtudiant> searchByNomOrPrenomOrMatricule(@Param("search") String search);

    // Liste filtrée par classe (via inscription active)
    @Query("""
        SELECT e FROM ProfilEtudiant e
        JOIN Inscription i ON i.etudiantId = e.id
        JOIN Classe c ON c.id = i.classeId
        WHERE e.isArchived = false
          AND i.statut = 'active'
          AND c.id = :classeId
        """)
    List<ProfilEtudiant> findByClasseId(@Param("classeId") Long classeId);

    // Liste filtrée par niveau (Seconde / Première / Terminale)
    // On filtre sur le nom de la classe (ex: "Seconde A", "1ère B", "Terminale C")
    // sans JOIN sur Niveau qui n'est pas une entity mappée dans ce contexte.
    @Query("""
        SELECT e FROM ProfilEtudiant e
        JOIN Inscription i ON i.etudiantId = e.id
        JOIN Classe c ON c.id = i.classeId
        WHERE e.isArchived = false
          AND i.statut = 'active'
          AND LOWER(c.nom) LIKE LOWER(CONCAT('%', :niveau, '%'))
        """)
    List<ProfilEtudiant> findByNiveau(@Param("niveau") String niveau);

    boolean existsByMatricule(String matricule);
}