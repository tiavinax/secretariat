# Phase 2 — Module Secrétariat : Élèves (Tsinjo)

- [ ] **[A]** Créer l'entité JPA `ProfilEtudiant` mappée sur `profils_etudiants`
- [ ] **[A]** Créer l'entité `Inscription` avec ses relations (étudiant, classe, année scolaire)
- [ ] **[A]** Créer `EtudiantRepository` : recherche par matricule, nom, classe, année scolaire active
- [ ] **[B]** Implémenter `EtudiantService` : liste paginée, recherche filtrée, détail étudiant
- [ ] **[B]** Brancher `GET /secretariat/eleves` sur le service → passer la liste au modèle Thymeleaf
- [ ] **[B]** Implémenter `POST /secretariat/eleves/nouveau` : créer user + profil étudiant + inscription dans une transaction
- [ ] **[B]** Implémenter le workflow modification dossier : créer une `demande_modification_dossier` au lieu d'éditer directement
- [ ] **[C]** Vue `eleves.html` : tableau avec nom, matricule, classe, statut + bouton "Voir profil"
- [ ] **[C]** Vue `profil_eleve.html` : afficher toutes les données de l'étudiant + historique inscriptions
- [ ] **[C]** Formulaire d'inscription : saisie des données étudiant + sélection classe + année scolaire
