-- Comptes d'accès unifiés pour tous les acteurs du système
CREATE TABLE users (
    id            SERIAL PRIMARY KEY,
    email         VARCHAR(255) UNIQUE NOT NULL,
    password      VARCHAR(255) NOT NULL,         -- bcrypt via CI4 Password helper
    is_active     BOOLEAN   DEFAULT TRUE,        -- désactivation sans suppression physique
    last_login    TIMESTAMP,
    created_at    TIMESTAMP DEFAULT NOW(),
    updated_at    TIMESTAMP DEFAULT NOW()
);

-- Profil complet d'un étudiant
-- La colonne "region" est importante : elle sert aux critères géographiques
CREATE TABLE profils_etudiants (
    id             SERIAL PRIMARY KEY,
    user_id        INT UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    matricule      VARCHAR(100) UNIQUE NOT NULL,      -- identifiant unique interne à l'école
    nom            VARCHAR(150) NOT NULL,
    prenom         VARCHAR(150) NOT NULL,
    date_naissance DATE,
    lieu_naissance VARCHAR(200),
    sexe           CHAR(1) CHECK (sexe IN ('M', 'F')),
    photo_url      VARCHAR(500),
    adresse        TEXT,
    commune        VARCHAR(150),
    region         VARCHAR(150),
    nationalite    VARCHAR(100) DEFAULT 'Malgache',
    cin            VARCHAR(50),                       -- Carte d'Identité Nationale (si majeur 18+)
    telephone      VARCHAR(50),
    is_archived    BOOLEAN DEFAULT FALSE,             -- archivé après fin de scolarité, jamais supprimé
    created_at     TIMESTAMP DEFAULT NOW(),
    updated_at     TIMESTAMP DEFAULT NOW()
);

-- Classes (ex : "Terminale A 2024-2025")
-- Une classe est l'instance d'un niveau pour une année donnée
CREATE TABLE classes (
    id                SERIAL PRIMARY KEY,
    niveau_id         INT REFERENCES niveaux(id),
    annee_scolaire_id INT REFERENCES annees_scolaires(id),
    nom               VARCHAR(100) NOT NULL,         -- ex : 'Terminale A', '1ère S'
    capacite_max      INT DEFAULT 40,
    created_at        TIMESTAMP DEFAULT NOW()
);

-- Inscription annuelle d'un étudiant dans une classe
-- UNIQUE sur (etudiant_id, annee_scolaire_id) : un dossier par an maximum
CREATE TABLE inscriptions (
    id                SERIAL PRIMARY KEY,
    etudiant_id       INT REFERENCES profils_etudiants(id),
    classe_id         INT REFERENCES classes(id),
    annee_scolaire_id INT REFERENCES annees_scolaires(id),
    type_inscription  VARCHAR(50) DEFAULT 'reinscription',  -- 'nouvelle' | 'reinscription'
    date_inscription  DATE        DEFAULT CURRENT_DATE,
    statut            VARCHAR(50) DEFAULT 'active',
    -- 'active', 'transfere', 'exclu', 'diplome', 'abandonne'
    rang_final        INT,                             -- calculé et stocké en fin d'année
    est_admis         BOOLEAN,                         -- résultat de passage en classe supérieure
    created_at        TIMESTAMP DEFAULT NOW(),
    updated_at        TIMESTAMP DEFAULT NOW(),
    UNIQUE (etudiant_id, annee_scolaire_id)
);

-- Profil parent ou tuteur légal
-- user_id peut être NULL : le parent n'est pas obligé d'avoir un compte
CREATE TABLE profils_parents (
    id           SERIAL PRIMARY KEY,
    user_id      INT REFERENCES users(id) ON DELETE SET NULL,
    nom          VARCHAR(150) NOT NULL,
    prenom       VARCHAR(150) NOT NULL,
    telephone    VARCHAR(50),
    email        VARCHAR(255),
    profession   VARCHAR(200),
    lien_parente VARCHAR(100),                        -- 'père', 'mère', 'tuteur', 'grand-parent'
    created_at   TIMESTAMP DEFAULT NOW()
);


-- Liaison étudiant ↔ parents/tuteurs (un étudiant peut avoir plusieurs tuteurs)
CREATE TABLE etudiants_parents (
    etudiant_id           INT REFERENCES profils_etudiants(id) ON DELETE CASCADE,
    parent_id             INT REFERENCES profils_parents(id)   ON DELETE CASCADE,
    est_contact_principal BOOLEAN DEFAULT FALSE,      -- un seul doit être TRUE par étudiant
    PRIMARY KEY (etudiant_id, parent_id)
);


