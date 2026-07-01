package com.ecole.entity.Secretaire;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Table(name = "niveaux")
public class Niveau {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "etablissement_id")
    private Etablissement etablissement;

    private String libelle;
    private Integer ordre;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}