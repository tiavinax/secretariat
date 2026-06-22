# I. Initialisation du projet (Une seule fois)

1. Créer le dossier du projet
```bash
mkdir projet
```

2. Initialiser Git et faire le premier commit
```bash
git add .
git commit -m "Initialisation projet"
```

3. Connecter le dépôt local à GitHub
```bash
git remote add origin URL_DU_DEPOT
git branch -M main
git push -u origin main
```

---

# II. Les autres membres

## Récupérer le projet

```bash
git clone URL_DU_DEPOT
```

---

# III. Création de la branche develop (Une seule fois)

```bash
git checkout -b develop
git push -u origin develop
```

---

# IV. Travail quotidien : Feature Branch

## 1. Mettre à jour les branches

```bash
git checkout main
git pull origin main

git checkout develop
git pull origin develop
```

---

## 2. Créer une branche de travail

```bash
git checkout -b feature/nom-fonctionnalite
```

Exemple :

```bash
git checkout -b feature/login
```

---

## 3. Développer et sauvegarder localement

```bash
git add .
git commit -m "Ajout de la page de connexion"
```

(On peut effectuer plusieurs commits pendant le développement.)

---

## 4. Envoyer la branche sur GitHub

```bash
git push origin feature/login
```

---

# V. Pull Request et Code Review

Lorsque la fonctionnalité est terminée :

1. Aller sur GitHub.
2. Créer une Pull Request :

```
feature/login
      ↓
develop
```

3. Attendre la revue du code (Code Review) par le chef de projet.

---

## Si des corrections sont demandées

Modifier le code puis :

```bash
git add .
git commit -m "Correction après review"
git push origin feature/login
```

La Pull Request sera automatiquement mise à jour.

---

## Si la Pull Request est approuvée

Le chef de projet effectue le merge de :

```
feature/login
      ↓
develop
```

---

# VI. Récupérer les nouvelles modifications

Tous les membres mettent à jour leur branche `develop` :

```bash
git checkout develop
git pull origin develop
```

---

# VII. Livraison vers Main

Lorsque plusieurs fonctionnalités sont validées dans `develop`, une Pull Request peut être créée :

```
develop
    ↓
main
```

Après validation, le chef de projet effectue le merge dans `main`.

---

# VIII. Suppression d'une branche feature (optionnel)

Après fusion de la Pull Request :

```bash
git branch -d feature/login
```

Pour supprimer également la branche distante :

```bash
git push origin --delete feature/login
```

---

# Workflow résumé

```
main
 ↑
develop
 ↑
feature/login
feature/payment
feature/dashboard
```

Développeur :

```
feature/login
      ↓
Pull Request
      ↓
Code Review
      ↓
Validation du chef de projet
      ↓
develop
```

Puis :

develo
```p
      ↓
Pull Request
      ↓
Validation
      ↓
main
```