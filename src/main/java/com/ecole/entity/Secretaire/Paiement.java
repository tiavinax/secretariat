package com.ecole.entity.Secretaire;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@Entity
@Table(name = "paiements")
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "echeance_id")
    private Echeance echeance;

    @ManyToOne
    @JoinColumn(name = "inscription_id")
    @JsonIgnore
    private Inscription inscription;

    private BigDecimal montant;

    @Column(name = "date_paiement")
    private LocalDate datePaiement;

    @Column(name = "mode_paiement")
    private String modePaiement;

    @Column(name = "reference_transaction")
    private String referenceTransaction;

    @ManyToOne
    @JoinColumn(name = "saisi_par")
    @JsonIgnore
    private User saisiPar;

    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}