package com.ecole.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO complet pour la page profil d'un élève.
 */
public class EleveProfilDTO {

    // --- Identité ---
    private Long id;
    private String matricule;
    private String nom;
    private String prenom;
    private String photoUrl;
    private String nomClasse;

    // --- Informations personnelles ---
    private LocalDate dateNaissance;
    private String commune;      // Quartier
    private String adresse;
    private LocalDate dateInscription;

    // --- Parent principal ---
    private String nomParent;
    private String prenomParent;
    private String telephoneParent;
    private String lienParente;

    // --- Historique de paiement ---
    private List<PaiementHistoriqueDTO> historiquesPaiements;

    public EleveProfilDTO() {}

    // Initiales pour avatar placeholder
    public String getInitiales() {
        String n = (prenom != null && !prenom.isEmpty()) ? prenom.substring(0, 1) : "";
        String p = (nom != null && !nom.isEmpty()) ? nom.substring(0, 1) : "";
        return (n + p).toUpperCase();
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public String getNomClasse() { return nomClasse; }
    public void setNomClasse(String nomClasse) { this.nomClasse = nomClasse; }
    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }
    public String getCommune() { return commune; }
    public void setCommune(String commune) { this.commune = commune; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public LocalDate getDateInscription() { return dateInscription; }
    public void setDateInscription(LocalDate dateInscription) { this.dateInscription = dateInscription; }
    public String getNomParent() { return nomParent; }
    public void setNomParent(String nomParent) { this.nomParent = nomParent; }
    public String getPrenomParent() { return prenomParent; }
    public void setPrenomParent(String prenomParent) { this.prenomParent = prenomParent; }
    public String getTelephoneParent() { return telephoneParent; }
    public void setTelephoneParent(String telephoneParent) { this.telephoneParent = telephoneParent; }
    public String getLienParente() { return lienParente; }
    public void setLienParente(String lienParente) { this.lienParente = lienParente; }
    public List<PaiementHistoriqueDTO> getHistoriquesPaiements() { return historiquesPaiements; }
    public void setHistoriquesPaiements(List<PaiementHistoriqueDTO> historiquesPaiements) { this.historiquesPaiements = historiquesPaiements; }
}
