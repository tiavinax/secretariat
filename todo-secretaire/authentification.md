## Phase 1 — Authentification (Dix)

### Étape 1 — Entités JPA
- [x] Créer l'entité `User` mappée sur `users` (id, email, password, is_active, last_login)
- [x] Créer les entités `Role` et `UserRole` avec la relation ManyToMany

---

### Étape 2 — Repositories
- [x] Créer `UserRepository` avec `findByEmail()`

---

### Étape 3 — Services et Configuration
- [x] Implémenter `CustomUserDetailsService` : charger user + rôles depuis la BDD
- [x] Configurer `SecurityConfig` : BCryptPasswordEncoder, formLogin, logout, règles d'accès par rôle
- [x] Implémenter `AuthSuccessHandler` : redirection post-login selon le rôle
- [x] Mettre à jour `last_login` en BDD à chaque connexion réussie

---

### Étape 4 — Controllers et Vues
- [x] Brancher la page `index.html` (login) sur Spring Security avec affichage des erreurs
- [x] Compléter `error.html` pour les erreurs 403 (accès refusé) et 404
- [x] Tester la connexion pour chaque rôle et vérifier les redirections
