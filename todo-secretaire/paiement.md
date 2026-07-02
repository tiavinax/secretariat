# Phase 3 — Module Paiement (Tiavina)

### Étape 1 — Entités JPA
- [ok] Créer `Echeancier.java` mappée sur `echeanciers`
- [ok] Créer `Echeance.java` mappée sur `echeances`
- [ok] Créer `Paiement.java` mappée sur `paiements`

> Les entités `Inscription` et `User` existent déjà — on les référence directement.

---

### Étape 2 — Repositories
- [ok] Créer `EcheancierRepository` avec `findByInscription(Inscription i)`
- [ok] Créer `EcheanceRepository` avec `findByEcheancierAndEstSoldeeFalse(Echeancier e)`
- [ok] Créer `PaiementRepository` avec `findByInscription(Inscription i)`

---

### Étape 3 — Service
- [ok] Créer `PaiementService` avec 2 méthodes :
  - `enregistrerPaiement(...)` : sauvegarder le paiement + vérifier si l'échéance est soldée
  - `getEcheancesOuvertes(Inscription i)` : retourner les échéances non soldées

---

### Étape 4 — Controller
- [ok] Brancher `GET /secretariat/paiement` : charger la liste des inscriptions actives + échéances ouvertes
- [ok] Ajouter `POST /secretariat/paiement` : appeler `PaiementService.enregistrerPaiement()`

---

### Étape 5 — Vue
- [ok] Brancher `paiement.html` sur les données réelles (remplacer les `<option>` hardcodées)
- [ok] Afficher les échéances ouvertes selon l'élève sélectionné


## Phase Facture & Liste Paiements

### Étape 1 — Backend
- [ok] Ajouter `findPaiements(String nom, String classe, String mode, LocalDate date)`
      dans `PaiementRepository` — recherche multi-critère nullable (JPA Specification)
- [ok] Ajouter `getPaiementsFiltres(...)` dans `PaiementService`
- [ok] Ajouter `getPaiementById(Integer id)` dans `PaiementService`
- [ok] Ajouter `exportFacturePdf(Integer paiementId)` dans `PaiementService`
- [ok] Ajouter `GET /secretariat/paiements` dans `SecretaireController`
- [ok] Ajouter `GET /secretariat/paiements/{id}/pdf` dans `SecretaireController`

### Étape 2 — Vue
- [ok] Créer `src/main/resources/templates/Secretaire/liste_paiements.html`
  - Formulaire recherche : nom élève, classe, mode paiement, date
  - Tableau résultats : nom, classe, tranche, montant, date, mode, statut
  - Bouton "Voir facture" par ligne
- [ok] Créer `src/main/resources/templates/Secretaire/facture.html`
  - Affichage détail paiement
  - Bouton "Télécharger PDF"
  - Bouton "Imprimer"

## Phase Paiement Multi-lignes + Facture

### Étape 1 — Backend
- [ok] Modifier `Paiement` : ajouter champ `referencePaiement` (groupe plusieurs lignes)
- [ok] Créer `PaiementGroupeDTO` : `inscriptionId`, `echeanceId`, `lignes[]` (montant + mode)
- [ok] Modifier `PaiementService.enregistrerPaiement()` : accepter plusieurs lignes,
      générer une référence groupe `GRP-{date}-{uuid court}`, sauvegarder N paiements
- [ok] Ajouter `getPaiementsByReference(String ref)` dans `PaiementRepository`
- [ok] Ajouter `GET /secretariat/paiements/{ref}/facture` → page HTML facture
- [ok] Modifier `POST /secretariat/paiement` → redirect vers `/secretariat/paiements/{ref}/facture`

### Étape 2 — Vue paiement.html
- [ok] Remplacer les champs montant + mode par un tableau dynamique
- [ok] Bouton `+` pour ajouter une ligne (montant + mode)
- [ok] Bouton `-` pour supprimer une ligne
- [ok] Total dynamique calculé en JS

### Étape 3 — Vue facture_detail.html
- [ok] Afficher infos élève + classe + tranche
- [ok] Tableau des lignes de paiement (montant + mode)
- [ok] Total payé
- [ok] Bouton "Exporter PDF" → `/secretariat/paiements/{ref}/pdf`
- [ok] Bouton "Imprimer" → `window.print()`