/*Table  pour liste d'etudiants */
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



CREATE TABLE niveaux (
    id               SERIAL PRIMARY KEY,
    etablissement_id INT REFERENCES etablissements(id),
    libelle          VARCHAR(100) NOT NULL,
    ordre            INT NOT NULL,                   -- tri croissant : 1=Seconde, 2=Première…
    created_at       TIMESTAMP DEFAULT NOW()
);

-- Années scolaires (ex : "2024-2025")
-- Une seule peut être marquée "active" à la fois, contrôlé applicativement
CREATE TABLE annees_scolaires (
    id               SERIAL PRIMARY KEY,
    etablissement_id INT REFERENCES etablissements(id),
    libelle          VARCHAR(50) NOT NULL,          -- ex : '2024-2025'
    date_debut       DATE NOT NULL,
    date_fin         DATE NOT NULL,
    est_active       BOOLEAN DEFAULT FALSE,          -- l'année scolaire en cours
    created_at       TIMESTAMP DEFAULT NOW()
);

-- Plan de paiement assigné à un étudiant pour son inscription
-- Définit si l'étudiant paye comptant ou en plusieurs tranches
CREATE TABLE echeanciers (
    id             SERIAL PRIMARY KEY,
    inscription_id INT REFERENCES inscriptions(id),
    grille_id      INT REFERENCES grilles_tarifaires(id),
    type           VARCHAR(50),
    -- 'comptant', 'echelonne_2', 'echelonne_3', 'personnalise'
    montant_total  NUMERIC(12,2),                      -- peut différer de la grille (remise accordée)
    created_at     TIMESTAMP DEFAULT NOW()
);


-- Tranches individuelles d'un plan de paiement
-- est_soldee = TRUE quand la somme des paiements couvre montant_attendu
CREATE TABLE echeances (
    id               SERIAL PRIMARY KEY,
    echeancier_id    INT REFERENCES echeanciers(id) ON DELETE CASCADE,
    numero_tranche   INT NOT NULL,                     -- 1, 2, 3…
    montant_attendu  NUMERIC(12,2) NOT NULL,
    date_limite      DATE NOT NULL,
    est_soldee       BOOLEAN DEFAULT FALSE,
    created_at       TIMESTAMP DEFAULT NOW()
);


-- Versements réels effectués par les familles
-- Plusieurs paiements peuvent couvrir une même échéance (paiement partiel)
CREATE TABLE paiements (
    id                    SERIAL PRIMARY KEY,
    echeance_id           INT REFERENCES echeances(id),
    inscription_id        INT REFERENCES inscriptions(id),
    montant               NUMERIC(12,2) NOT NULL,
    date_paiement         DATE NOT NULL,
    mode_paiement         VARCHAR(100),
    -- 'especes', 'virement', 'mvola', 'orange_money', 'cheque'
    reference_transaction VARCHAR(200),                -- numéro de reçu ou de transaction
    saisi_par             INT REFERENCES users(id),    -- secrétaire ou comptable
    notes                 TEXT,
    created_at            TIMESTAMP DEFAULT NOW()
);

