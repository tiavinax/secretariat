package com.ecole.entity.Secretaire;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Table(name = "annees_scolaires")
public class AnneeScolaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "etablissement_id")
    private Etablissement etablissement;

    private String libelle;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "est_active")
    private Boolean estActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}