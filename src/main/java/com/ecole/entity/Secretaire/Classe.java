package com.ecole.entity.Secretaire;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Table(name = "classes")
public class Classe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "niveau_id")
    private Niveau niveau;

    @ManyToOne
    @JoinColumn(name = "annee_scolaire_id")
    private AnneeScolaire anneeScolaire;

    private String nom;

    @Column(name = "capacite_max")
    private Integer capaciteMax;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}