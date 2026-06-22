package com.ecole.dto;

import java.util.Map;

/**
 * DTO pour afficher une ligne dans la liste des élèves.
 * Contient les infos de base + statut de paiement par mois.
 */
public class EleveListeDTO {

    private Long id;
    private String matricule;
    private String nom;
    private String prenom;
    private String nomClasse;        // ex : "1ère A"
    private String niveauLibelle;    // ex : "Premiere", "Seconde", "Terminale"
    private String photoUrl;

    // Clé = numéro de mois (1..12), valeur = true si payé
    private Map<Integer, Boolean> paiementsMois;

    public EleveListeDTO() {}

    public EleveListeDTO(Long id, String matricule, String nom, String prenom,
                         String nomClasse, String niveauLibelle, String photoUrl,
                         Map<Integer, Boolean> paiementsMois) {
        this.id = id;
        this.matricule = matricule;
        this.nom = nom;
        this.prenom = prenom;
        this.nomClasse = nomClasse;
        this.niveauLibelle = niveauLibelle;
        this.photoUrl = photoUrl;
        this.paiementsMois = paiementsMois;
    }

    // Initiales pour avatar placeholder
    public String getInitiales() {
        String n = (prenom != null && !prenom.isEmpty()) ? prenom.substring(0, 1) : "";
        String p = (nom != null && !nom.isEmpty()) ? nom.substring(0, 1) : "";
        return (n + p).toUpperCase();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getNomClasse() { return nomClasse; }
    public void setNomClasse(String nomClasse) { this.nomClasse = nomClasse; }
    public String getNiveauLibelle() { return niveauLibelle; }
    public void setNiveauLibelle(String niveauLibelle) { this.niveauLibelle = niveauLibelle; }
    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public Map<Integer, Boolean> getPaiementsMois() { return paiementsMois; }
    public void setPaiementsMois(Map<Integer, Boolean> paiementsMois) { this.paiementsMois = paiementsMois; }
}
