package com.ecole.repository.Secretaire;

import com.ecole.entity.Secretaire.ProfilEtudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProfilEtudiantRepository extends JpaRepository<ProfilEtudiant, Integer> {

    Optional<ProfilEtudiant> findByMatricule(String matricule);

    List<ProfilEtudiant> findByIsArchivedFalse();

    boolean existsByMatricule(String matricule);

    @Query("SELECT e FROM ProfilEtudiant e WHERE e.isArchived = false AND (" +
           "LOWER(e.nom) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.prenom) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.matricule) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<ProfilEtudiant> searchByNomOrPrenomOrMatricule(@Param("search") String search);

    // Recherche par classe — via relation @ManyToOne
    @Query("SELECT e FROM ProfilEtudiant e " +
           "JOIN Inscription i ON i.etudiant = e " +
           "WHERE e.isArchived = false AND i.statut = 'active' AND i.classe.id = :classeId")
    List<ProfilEtudiant> findByClasseId(@Param("classeId") Integer classeId);

    // Recherche par niveau — via nom de classe
    @Query("SELECT e FROM ProfilEtudiant e " +
           "JOIN Inscription i ON i.etudiant = e " +
           "WHERE e.isArchived = false AND i.statut = 'active' " +
           "AND LOWER(i.classe.nom) LIKE LOWER(CONCAT('%', :niveau, '%'))")
    List<ProfilEtudiant> findByNiveau(@Param("niveau") String niveau);
}