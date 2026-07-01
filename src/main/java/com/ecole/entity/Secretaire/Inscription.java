package com.ecole.entity.Secretaire;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@Entity
@Table(name = "inscriptions")
public class Inscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "etudiant_id")
    @JsonIgnore
    private ProfilEtudiant etudiant;

    @ManyToOne
    @JoinColumn(name = "classe_id")
    @JsonIgnore
    private Classe classe;

    @ManyToOne
    @JoinColumn(name = "annee_scolaire_id")
    @JsonIgnore
    private AnneeScolaire anneeScolaire;

    @Column(name = "type_inscription")
    private String typeInscription;

    @Column(name = "date_inscription")
    private LocalDate dateInscription;

    private String statut;

    @Column(name = "rang_final")
    private Integer rangFinal;

    @Column(name = "est_admis")
    private Boolean estAdmis;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}