package com.ecole.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "classes")
public class Classe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "niveau_id")
    private Long niveauId;

    @Column(name = "annee_scolaire_id")
    private Long anneeScolaireId;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "capacite_max")
    private Integer capaciteMax = 40;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getNiveauId() { return niveauId; }
    public void setNiveauId(Long niveauId) { this.niveauId = niveauId; }
    public Long getAnneeScolaireId() { return anneeScolaireId; }
    public void setAnneeScolaireId(Long anneeScolaireId) { this.anneeScolaireId = anneeScolaireId; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public Integer getCapaciteMax() { return capaciteMax; }
    public void setCapaciteMax(Integer capaciteMax) { this.capaciteMax = capaciteMax; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
