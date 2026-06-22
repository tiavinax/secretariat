package com.ecole.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "paiements")
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "echeance_id")
    private Long echeanceId;

    @Column(name = "inscription_id")
    private Long inscriptionId;

    @Column(name = "montant", nullable = false)
    private BigDecimal montant;

    @Column(name = "date_paiement", nullable = false)
    private LocalDate datePaiement;

    @Column(name = "mode_paiement")
    private String modePaiement;

    @Column(name = "reference_transaction")
    private String referenceTransaction;

    @Column(name = "saisi_par")
    private Long saisiPar;

    @Column(name = "notes")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEcheanceId() { return echeanceId; }
    public void setEcheanceId(Long echeanceId) { this.echeanceId = echeanceId; }
    public Long getInscriptionId() { return inscriptionId; }
    public void setInscriptionId(Long inscriptionId) { this.inscriptionId = inscriptionId; }
    public BigDecimal getMontant() { return montant; }
    public void setMontant(BigDecimal montant) { this.montant = montant; }
    public LocalDate getDatePaiement() { return datePaiement; }
    public void setDatePaiement(LocalDate datePaiement) { this.datePaiement = datePaiement; }
    public String getModePaiement() { return modePaiement; }
    public void setModePaiement(String modePaiement) { this.modePaiement = modePaiement; }
    public String getReferenceTransaction() { return referenceTransaction; }
    public void setReferenceTransaction(String referenceTransaction) { this.referenceTransaction = referenceTransaction; }
    public Long getSaisiPar() { return saisiPar; }
    public void setSaisiPar(Long saisiPar) { this.saisiPar = saisiPar; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
