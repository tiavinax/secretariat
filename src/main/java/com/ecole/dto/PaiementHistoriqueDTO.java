package com.ecole.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Ligne d'historique de paiement pour le profil élève.
 */
public class PaiementHistoriqueDTO {

    private String moisLabel;        // ex : "Janvier 2026"
    private LocalDate datePaiement;
    private BigDecimal montant;
    private String statut;           // "Payé" ou "Non payé"

    public PaiementHistoriqueDTO() {}

    public PaiementHistoriqueDTO(String moisLabel, LocalDate datePaiement,
                                  BigDecimal montant, String statut) {
        this.moisLabel = moisLabel;
        this.datePaiement = datePaiement;
        this.montant = montant;
        this.statut = statut;
    }

    public boolean isPaye() {
        return "Payé".equals(statut);
    }

    public String getMoisLabel() { return moisLabel; }
    public void setMoisLabel(String moisLabel) { this.moisLabel = moisLabel; }
    public LocalDate getDatePaiement() { return datePaiement; }
    public void setDatePaiement(LocalDate datePaiement) { this.datePaiement = datePaiement; }
    public BigDecimal getMontant() { return montant; }
    public void setMontant(BigDecimal montant) { this.montant = montant; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
}
