package com.ecole.entity.Secretaire;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter 
@Setter
@Entity
@Table(name = "profils_etudiants")
public class ProfilEtudiant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String matricule;
    private String nom;
    private String prenom;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(name = "lieu_naissance")
    private String lieuNaissance;

    private String sexe;

    @Column(name = "photo_url")
    private String photoUrl;

    private String adresse;
    private String commune;
    private String region;
    private String nationalite;
    private String cin;
    private String telephone;

    @Column(name = "is_archived")
    private Boolean isArchived;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}