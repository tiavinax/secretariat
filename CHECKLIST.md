# ✅ TODO — Module Secrétariat + Authentification
> Projet : Gestion École Secondaire — Spring Boot / Thymeleaf / PostgreSQL
> Équipe : 3 membres (A = données/entités, B = services/controllers, C = vues/tests)

---

## 📦 Phase 0 — Préparation & base de données 

- [ ] **[A]** Créer la base PostgreSQL `ecole` et exécuter `ScriptSQL/schema_ecole_v2.sql`
- [ ] **[A]** Configurer `application.properties` : datasource PostgreSQL, `ddl-auto: validate`, Thymeleaf
- [ ] **[B]** Ajouter les dépendances Maven dans `pom.xml` : Spring Security, Spring Data JPA, Thymeleaf Security extras, BCrypt
- [ ] **[C]** Créer les données de test (seed SQL) : 1 user par rôle, 1 établissement, 1 année scolaire active

---

##  Phase 1 — Authentification (Spring Security) (Dix)

- [ok] **[A]** Créer l'entité `User` mappée sur `users` (id, email, password, is_active, last_login)
- [ok] **[A]** Créer les entités `Role` et `UserRole` avec la relation ManyToMany
- [ok] **[A]** Créer `UserRepository` avec `findByEmail()`
- [ok] **[B]** Implémenter `CustomUserDetailsService` : charger user + rôles depuis la BDD
- [ok] **[B]** Configurer `SecurityConfig` : BCryptPasswordEncoder, formLogin, logout, règles d'accès par rôle
  - ex : `/secretariat/**` → `ROLE_SECRETARIAT`, `/directeur/**` → `ROLE_DIRECTEUR`
- [ok] **[B]** Implémenter `AuthSuccessHandler` : redirection post-login selon le rôle
  - directeur → `/directeur/dashboard`
  - secrétaire → `/secretariat/eleves`
  - professeur → `/professeur/notes`
  - étudiant → `/etudiant/bulletin`
- [ok] **[B]** Mettre à jour `last_login` en BDD à chaque connexion réussie
- [ok] **[C]** Brancher la page `index.html` (login) sur Spring Security avec affichage des erreurs
- [ok] **[C]** Compléter `error.html` pour les erreurs 403 (accès refusé) et 404
- [ok] **[C]** Tester la connexion pour chaque rôle et vérifier les redirections

[Login: rakoto@ecole.mg]
        ↓
[UserDetailsService]
  → SELECT user WHERE email = ?
  → SELECT roles WHERE user_id = ?
  → retourne : [ROLE_PROFESSEUR, ROLE_PARENT]
        ↓
[Spring Security stocke les 2 rôles en session]
        ↓
[AuthSuccessHandler]
  → a ROLE_DIRECTEUR ?    NON
  → a ROLE_SECRETARIAT ?  NON
  → a ROLE_PROFESSEUR ?   OUI → redirect /professeur/notes
        ↓
[Navigation libre]
  /professeur/notes   ✅ (a ROLE_PROFESSEUR)
  /parent/suivi       ✅ (a ROLE_PARENT)
  /secretariat/eleves ❌ 403 Forbidden
  /directeur/dashboard ❌ 403 Forbidden
---

## 👨‍🎓 Phase 2 — Module Secrétariat : Élèves (Tsinjo)

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

---

## Phase 3 — Module Secrétariat : Paiements (Tiavina)

- [ ] **[A]** Créer les entités JPA : `GrilleTarifaire`, `Echeancier`, `Echeance`, `Paiement`
- [ ] **[A]** Créer `PaiementRepository` : paiements par inscription, échéances non soldées, total encaissé
- [ ] **[B]** Implémenter `PaiementService` : enregistrer un versement, marquer l'échéance soldée si couverte, affecter `saisi_par`
- [ ] **[B]** Brancher `GET /secretariat/paiement` : charger les élèves et leurs échéances ouvertes
- [ ] **[B]** Implémenter `POST /secretariat/paiement` : enregistrer le versement + mise à jour statut échéance
- [ ] **[B]** Générer un reçu PDF et enregistrer dans la table `documents` (type `recu_paiement`)
- [ ] **[C]** Vue `paiement.html` : formulaire de saisie (recherche élève → sélection échéance → montant + mode)
- [ ] **[C]** Vue `bilan.html` : synthèse encaissements vs attendus, liste des impayés par classe

---

## 🎨 Phase 4 — Interface & sécurité transversale (Tiavina)

- [ ] **[A]** Vérifier la protection CSRF sur tous les formulaires POST (activée par défaut dans Spring Security)
- [ ] **[B]** Ajouter la validation côté serveur sur les DTOs des formulaires (`@NotNull`, `@Size`, `@Email`)
- [ ] **[C]** Sécuriser `fragments/header.html` : afficher le nom de l'utilisateur connecté + bouton déconnexion
- [ ] **[C]** Masquer/afficher les menus selon le rôle avec `sec:authorize` dans les fragments Thymeleaf
- [ ] **[C]** Tests manuels complets : chaque rôle ne peut pas accéder aux pages des autres rôles