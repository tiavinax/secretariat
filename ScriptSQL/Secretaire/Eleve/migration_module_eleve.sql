-- ============================================================
-- ScriptSQL/Secretaire/Eleve/migration_module_eleve.sql
-- Migration pour activer le module Élève du Secrétariat
-- À exécuter UNE FOIS sur la base existante (schema_ecole_v2.sql)
-- ============================================================

-- Table de liaison étudiant ↔ parent (si absente)
CREATE TABLE IF NOT EXISTS etudiants_parents (
    etudiant_id           INT REFERENCES profils_etudiants(id) ON DELETE CASCADE,
    parent_id             INT REFERENCES profils_parents(id)   ON DELETE CASCADE,
    est_contact_principal BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (etudiant_id, parent_id)
);

-- Table de demandes de modification de dossier (si absente)
CREATE TABLE IF NOT EXISTS demandes_modification_dossier (
    id              SERIAL PRIMARY KEY,
    etudiant_id     INT REFERENCES profils_etudiants(id),
    champ_modifie   VARCHAR(150) NOT NULL,
    ancienne_valeur TEXT,
    nouvelle_valeur TEXT,
    motif           TEXT,
    statut          VARCHAR(50) DEFAULT 'en_attente',  -- 'en_attente', 'approuvee', 'refusee'
    soumis_par      INT REFERENCES users(id),
    traite_par      INT REFERENCES users(id),
    date_traitement TIMESTAMP,
    created_at      TIMESTAMP DEFAULT NOW()
);

-- Index utiles pour les requêtes de liste et recherche
CREATE INDEX IF NOT EXISTS idx_inscriptions_etudiant_statut
    ON inscriptions(etudiant_id, statut);

CREATE INDEX IF NOT EXISTS idx_inscriptions_annee_active
    ON inscriptions(annee_scolaire_id);

CREATE INDEX IF NOT EXISTS idx_paiements_inscription
    ON paiements(inscription_id);

CREATE INDEX IF NOT EXISTS idx_profils_etudiants_archived
    ON profils_etudiants(is_archived);

CREATE INDEX IF NOT EXISTS idx_profils_etudiants_search
    ON profils_etudiants(nom, prenom, matricule);
