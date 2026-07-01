package com.ecole.entity.Secretaire;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Table(name = "etablissements")
public class Etablissement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nom;
    private String adresse;
    private String telephone;
    private String email;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}