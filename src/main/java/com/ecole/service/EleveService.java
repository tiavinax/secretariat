package com.ecole.service;

import com.ecole.dto.AjoutEleveDTO;
import com.ecole.dto.EleveListeDTO;
import com.ecole.dto.EleveProfilDTO;
import com.ecole.dto.PaiementHistoriqueDTO;
import com.ecole.entity.*;
import com.ecole.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class EleveService {

    private static final Logger log = LoggerFactory.getLogger(EleveService.class);

    private final ProfilEtudiantRepository etudiantRepo;
    private final InscriptionRepository inscriptionRepo;
    private final ClasseRepository classeRepo;
    private final PaiementRepository paiementRepo;
    private final ProfilParentRepository parentRepo;
    private final AnneeScolaireRepository anneeScolaireRepo;
    private final JdbcTemplate jdbc;

    public EleveService(ProfilEtudiantRepository etudiantRepo,
                        InscriptionRepository inscriptionRepo,
                        ClasseRepository classeRepo,
                        PaiementRepository paiementRepo,
                        ProfilParentRepository parentRepo,
                        AnneeScolaireRepository anneeScolaireRepo,
                        JdbcTemplate jdbc) {
        this.etudiantRepo = etudiantRepo;
        this.inscriptionRepo = inscriptionRepo;
        this.classeRepo = classeRepo;
        this.paiementRepo = paiementRepo;
        this.parentRepo = parentRepo;
        this.anneeScolaireRepo = anneeScolaireRepo;
        this.jdbc = jdbc;
    }

    // ----------------------------------------------------------------
    //  Liste des élèves
    // ----------------------------------------------------------------

    public List<EleveListeDTO> listerTousEleves() {
        return buildListeDTO(etudiantRepo.findByIsArchivedFalse());
    }

    public List<EleveListeDTO> listerParNiveau(String niveau) {
        return buildListeDTO(etudiantRepo.findByNiveau(niveau));
    }

    public List<EleveListeDTO> listerParClasse(Long classeId) {
        return buildListeDTO(etudiantRepo.findByClasseId(classeId));
    }

    public List<EleveListeDTO> rechercherEleves(String search) {
        return buildListeDTO(etudiantRepo.searchByNomOrPrenomOrMatricule(search));
    }

    public List<Classe> listerClasses() {
        return classeRepo.findAllActiveAnnee();
    }

    // ----------------------------------------------------------------
    //  Profil d'un élève
    // ----------------------------------------------------------------

    public EleveProfilDTO getProfil(Long etudiantId) {
        ProfilEtudiant etudiant = etudiantRepo.findById(etudiantId)
            .orElseThrow(() -> new RuntimeException("Élève introuvable : " + etudiantId));

        EleveProfilDTO dto = new EleveProfilDTO();
        dto.setId(etudiant.getId());
        dto.setMatricule(etudiant.getMatricule());
        dto.setNom(etudiant.getNom());
        dto.setPrenom(etudiant.getPrenom());
        dto.setPhotoUrl(etudiant.getPhotoUrl());
        dto.setDateNaissance(etudiant.getDateNaissance());
        dto.setCommune(etudiant.getCommune());
        dto.setAdresse(etudiant.getAdresse());

        // Inscription active → classe + date inscription
        inscriptionRepo.findActiveByEtudiantId(etudiantId).ifPresent(insc -> {
            dto.setDateInscription(insc.getDateInscription());
            classeRepo.findById(insc.getClasseId()).ifPresent(c -> dto.setNomClasse(c.getNom()));
        });

        // Parent principal
        parentRepo.findContactPrincipalByEtudiantId(etudiantId).ifPresent(parent -> {
            dto.setNomParent(parent.getNom());
            dto.setPrenomParent(parent.getPrenom());
            dto.setTelephoneParent(parent.getTelephone());
            dto.setLienParente(parent.getLienParente());
        });

        // Historique paiements
        List<Paiement> paiements = paiementRepo.findByEtudiantIdCurrentYear(etudiantId);
        List<PaiementHistoriqueDTO> historique = new ArrayList<>();
        for (Paiement p : paiements) {
            String moisLabel = p.getDatePaiement().getMonth()
                .getDisplayName(TextStyle.FULL, Locale.FRENCH) + " " + p.getDatePaiement().getYear();
            historique.add(new PaiementHistoriqueDTO(
                capitalize(moisLabel),
                p.getDatePaiement(),
                p.getMontant(),
                "Payé"
            ));
        }
        dto.setHistoriquesPaiements(historique);

        return dto;
    }

    // ----------------------------------------------------------------
    //  Ajout d'un élève
    // ----------------------------------------------------------------

    @Transactional
    public ProfilEtudiant ajouterEleve(AjoutEleveDTO dto) {
        // 1. Sauvegarder l'étudiant
        ProfilEtudiant etudiant = new ProfilEtudiant();
        etudiant.setNom(dto.getNom());
        etudiant.setPrenom(dto.getPrenom());
        etudiant.setDateNaissance(dto.getDateNaissance());
        etudiant.setCommune(dto.getCommune());
        etudiant.setAdresse(dto.getAdresse());
        etudiant.setMatricule(genererMatricule());
        etudiant.setCreatedAt(LocalDateTime.now());
        etudiant.setUpdatedAt(LocalDateTime.now());
        etudiant.setIsArchived(false);
        ProfilEtudiant saved = etudiantRepo.save(etudiant);

        // 2. Sauvegarder le parent si renseigné
        if (dto.getNomParent() != null && !dto.getNomParent().isBlank()) {
            ProfilParent parent = new ProfilParent();
            parent.setNom(dto.getNomParent());
            parent.setPrenom(dto.getPrenomParent() != null ? dto.getPrenomParent() : "");
            parent.setTelephone(dto.getTelephoneParent());
            parent.setLienParente(dto.getLienParente());
            parent.setCreatedAt(LocalDateTime.now());
            parentRepo.save(parent);
        }

        // 3. Créer l'inscription avec classe ET année scolaire active
        Long classeIdLong = dto.getClasseIdAsLong();
        if (classeIdLong != null) {
            Inscription inscription = new Inscription();
            inscription.setEtudiantId(saved.getId());
            inscription.setClasseId(classeIdLong);
            inscription.setTypeInscription("nouvelle");
            inscription.setDateInscription(LocalDate.now());
            inscription.setStatut("active");
            inscription.setCreatedAt(LocalDateTime.now());
            inscription.setUpdatedAt(LocalDateTime.now());

            // FIX : setter l'année scolaire active pour que les queries
            // par annee_scolaire fonctionnent correctement
            anneeScolaireRepo.findActive().ifPresentOrElse(
                annee -> {
                    inscription.setAnneeScolaireId(annee.getId());
                    log.info("Inscription créée avec année scolaire : {}", annee.getLibelle());
                },
                () -> log.warn("Aucune année scolaire active trouvée — annee_scolaire_id sera NULL")
            );

            inscriptionRepo.save(inscription);
        }

        return saved;
    }

    // ----------------------------------------------------------------
    //  Demande de modification de dossier
    // ----------------------------------------------------------------

    @Transactional
    public void soumettreDemandeModification(Long etudiantId, String champModifie,
                                              String ancienneValeur, String nouvelleValeur,
                                              String motif) {
        // JdbcTemplate évite de devoir créer une Entity + Repository pour cette table
        jdbc.update("""
            INSERT INTO demandes_modification_dossier
                (etudiant_id, champ_modifie, ancienne_valeur, nouvelle_valeur, motif, statut, created_at)
            VALUES (?, ?, ?, ?, ?, 'en_attente', NOW())
            """,
            etudiantId, champModifie, ancienneValeur, nouvelleValeur, motif
        );
        log.info("Demande modification soumise — élève {} champ '{}'", etudiantId, champModifie);
    }

    // ----------------------------------------------------------------
    //  Helpers privés
    // ----------------------------------------------------------------

    private List<EleveListeDTO> buildListeDTO(List<ProfilEtudiant> etudiants) {
        List<EleveListeDTO> result = new ArrayList<>();

        for (ProfilEtudiant e : etudiants) {
            String nomClasse = "";
            String niveau = "";

            // findActiveByEtudiantId a maintenant un fallback sur statut
            // donc la classe s'affiche même si annee_scolaire_id est NULL
            Optional<Inscription> inscOpt = inscriptionRepo.findActiveByEtudiantId(e.getId());
            if (inscOpt.isPresent()) {
                Optional<Classe> classeOpt = classeRepo.findById(inscOpt.get().getClasseId());
                if (classeOpt.isPresent()) {
                    nomClasse = classeOpt.get().getNom();
                    niveau = detecterNiveau(nomClasse);
                }
            }

            // Paiements de l'année courante
            List<Paiement> paiements = paiementRepo.findByEtudiantIdCurrentYear(e.getId());
            Map<Integer, Boolean> paiementsMois = new HashMap<>();
            for (int m = 1; m <= 12; m++) paiementsMois.put(m, false);
            for (Paiement p : paiements) {
                paiementsMois.put(p.getDatePaiement().getMonthValue(), true);
            }

            result.add(new EleveListeDTO(
                e.getId(), e.getMatricule(), e.getNom(), e.getPrenom(),
                nomClasse, niveau, e.getPhotoUrl(), paiementsMois
            ));
        }
        return result;
    }

    private String detecterNiveau(String nomClasse) {
        if (nomClasse == null) return "";
        String n = nomClasse.toLowerCase();
        if (n.contains("terminal") || n.contains("tle")) return "Terminale";
        if (n.contains("premi") || n.contains("1ère") || n.contains("1ere")) return "Première";
        if (n.contains("second") || n.contains("2nde")) return "Seconde";
        return "";
    }

    private String genererMatricule() {
        int annee = LocalDate.now().getYear();
        long count = etudiantRepo.count() + 1;
        return String.format("MAT-%d-%04d", annee, count);
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}