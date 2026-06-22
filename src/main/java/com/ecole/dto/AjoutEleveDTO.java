package com.ecole.dto;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

/**
 * DTO pour le formulaire d'ajout d'un nouvel élève.
 *
 * FIXES :
 * - classeId est String pour accepter "" sans exception de conversion,
 *   puis converti en Long dans le service (null si vide).
 * - dateNaissance annotée @DateTimeFormat pour le binding HTML input[date].
 */
public class AjoutEleveDTO {

    private String nom;
    private String prenom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateNaissance;

    // String au lieu de Long : évite MethodArgumentTypeMismatchException
    // quand le select renvoie "" (option vide)
    private String classeId;

    // Parent
    private String nomParent;
    private String prenomParent;
    private String telephoneParent;
    private String lienParente;

    private String commune;
    private String adresse;

    public AjoutEleveDTO() {}

    /** Retourne null si classeId est vide ou non numérique. */
    public Long getClasseIdAsLong() {
        if (classeId == null || classeId.isBlank()) return null;
        try { return Long.parseLong(classeId.trim()); }
        catch (NumberFormatException e) { return null; }
    }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }
    public String getClasseId() { return classeId; }
    public void setClasseId(String classeId) { this.classeId = classeId; }
    public String getNomParent() { return nomParent; }
    public void setNomParent(String nomParent) { this.nomParent = nomParent; }
    public String getPrenomParent() { return prenomParent; }
    public void setPrenomParent(String prenomParent) { this.prenomParent = prenomParent; }
    public String getTelephoneParent() { return telephoneParent; }
    public void setTelephoneParent(String telephoneParent) { this.telephoneParent = telephoneParent; }
    public String getLienParente() { return lienParente; }
    public void setLienParente(String lienParente) { this.lienParente = lienParente; }
    public String getCommune() { return commune; }
    public void setCommune(String commune) { this.commune = commune; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
}