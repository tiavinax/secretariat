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


## 🧾 Phase Facture & Liste Paiements

### Étape 1 — Backend
- [ ] Ajouter `findPaiements(String nom, String classe, String mode, LocalDate date)`
      dans `PaiementRepository` — recherche multi-critère nullable (JPA Specification)
- [ ] Ajouter `getPaiementsFiltres(...)` dans `PaiementService`
- [ ] Ajouter `getPaiementById(Integer id)` dans `PaiementService`
- [ ] Ajouter `exportFacturePdf(Integer paiementId)` dans `PaiementService`
- [ ] Ajouter `GET /secretariat/paiements` dans `SecretaireController`
- [ ] Ajouter `GET /secretariat/paiements/{id}/pdf` dans `SecretaireController`

### Étape 2 — Vue
- [ ] Créer `src/main/resources/templates/Secretaire/liste_paiements.html`
  - Formulaire recherche : nom élève, classe, mode paiement, date
  - Tableau résultats : nom, classe, tranche, montant, date, mode, statut
  - Bouton "Voir facture" par ligne
- [ ] Créer `src/main/resources/templates/Secretaire/facture.html`
  - Affichage détail paiement
  - Bouton "Télécharger PDF"
  - Bouton "Imprimer"