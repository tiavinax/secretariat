package com.ecole.controller;

import com.ecole.dto.AjoutEleveDTO;
import com.ecole.dto.EleveProfilDTO;
import com.ecole.dto.PaiementHistoriqueDTO;
import com.ecole.service.EleveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@Controller
public class SecretaireController {

    private static final Logger log = LoggerFactory.getLogger(SecretaireController.class);

    private final EleveService eleveService;

    public SecretaireController(EleveService eleveService) {
        this.eleveService = eleveService;
    }

    // ----------------------------------------------------------------
    //  Pages existantes (inchangées)
    // ----------------------------------------------------------------

    @GetMapping("/secretariat/paiement")
    public String paiement(Model model) {
        model.addAttribute("pageTitle", "Ajouter un Paiement");
        model.addAttribute("currentRole", "secretariat");
        return "Secretaire/paiement";
    }

    @GetMapping("/secretariat/bilan")
    public String bilan(Model model) {
        model.addAttribute("pageTitle", "Bilan de Paiement");
        model.addAttribute("currentRole", "secretariat");
        return "Secretaire/bilan";
    }

    // ----------------------------------------------------------------
    //  Module Élève – Liste
    // ----------------------------------------------------------------

    @GetMapping("/secretariat/eleves")
    public String eleves(
            Model model,
            @RequestParam(value = "niveau", required = false) String niveau,
            @RequestParam(value = "classeId", required = false) Long classeId,
            @RequestParam(value = "search", required = false) String search) {

        if (search != null && !search.isBlank()) {
            model.addAttribute("eleves", eleveService.rechercherEleves(search));
        } else if (classeId != null) {
            model.addAttribute("eleves", eleveService.listerParClasse(classeId));
        } else if (niveau != null && !niveau.isBlank()) {
            model.addAttribute("eleves", eleveService.listerParNiveau(niveau));
        } else {
            model.addAttribute("eleves", eleveService.listerTousEleves());
        }

        model.addAttribute("classes", eleveService.listerClasses());
        model.addAttribute("niveauActif", niveau);
        model.addAttribute("classeIdActif", classeId);
        model.addAttribute("search", search);
        model.addAttribute("pageTitle", "Liste des Élèves");
        model.addAttribute("currentRole", "secretariat");
        return "Secretaire/eleves";
    }

    // ----------------------------------------------------------------
    //  Module Élève – Profil
    // ----------------------------------------------------------------

    @GetMapping("/secretariat/profil/{id}")
    public String profil(@PathVariable Long id, Model model) {
        model.addAttribute("eleve", eleveService.getProfil(id));
        model.addAttribute("pageTitle", "Profil de l'Élève");
        model.addAttribute("currentRole", "secretariat");
        return "Secretaire/profil_eleve";
    }

    @GetMapping("/secretariat/profil")
    public String profilRedirect() {
        return "redirect:/secretariat/eleves";
    }

    // ----------------------------------------------------------------
    //  Module Élève – Export PDF
    // ----------------------------------------------------------------

    @GetMapping("/secretariat/profil/{id}/export/pdf")
    public ResponseEntity<byte[]> exportPdf(@PathVariable Long id) {
        EleveProfilDTO eleve = eleveService.getProfil(id);

        // Génération d'un HTML minimal imprimable converti côté client
        // (iText ou WeasyPrint peuvent remplacer si disponibles)
        String html = buildHtmlProfil(eleve);
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);

        String filename = "profil_" + eleve.getMatricule() + ".html";
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
            .contentType(MediaType.TEXT_HTML)
            .body(bytes);
    }

    // ----------------------------------------------------------------
    //  Module Élève – Export CSV
    // ----------------------------------------------------------------

    @GetMapping("/secretariat/profil/{id}/export/csv")
    public ResponseEntity<byte[]> exportCsv(@PathVariable Long id) {
        EleveProfilDTO eleve = eleveService.getProfil(id);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(baos, true, StandardCharsets.UTF_8);

        // En-tête BOM pour Excel
        baos.write(0xEF); baos.write(0xBB); baos.write(0xBF);

        pw.println("Matricule;Nom;Prénom;Classe;Date naissance;Quartier;Adresse;Nom parent;Contact parent");
        pw.printf("%s;%s;%s;%s;%s;%s;%s;%s;%s%n",
            safe(eleve.getMatricule()),
            safe(eleve.getNom()),
            safe(eleve.getPrenom()),
            safe(eleve.getNomClasse()),
            eleve.getDateNaissance() != null ? eleve.getDateNaissance().toString() : "",
            safe(eleve.getCommune()),
            safe(eleve.getAdresse()),
            safe(eleve.getNomParent() != null ? eleve.getPrenomParent() + " " + eleve.getNomParent() : ""),
            safe(eleve.getTelephoneParent())
        );

        if (eleve.getHistoriquesPaiements() != null && !eleve.getHistoriquesPaiements().isEmpty()) {
            pw.println();
            pw.println("Historique paiements");
            pw.println("Mois;Date paiement;Montant;Statut");
            for (PaiementHistoriqueDTO p : eleve.getHistoriquesPaiements()) {
                pw.printf("%s;%s;%s;%s%n",
                    safe(p.getMoisLabel()),
                    p.getDatePaiement() != null ? p.getDatePaiement().toString() : "",
                    p.getMontant() != null ? p.getMontant().toPlainString() : "",
                    safe(p.getStatut())
                );
            }
        }
        pw.flush();

        String filename = "profil_" + eleve.getMatricule() + ".csv";
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
            .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
            .body(baos.toByteArray());
    }

    // ----------------------------------------------------------------
    //  Module Élève – Demande de modification dossier (POST)
    // ----------------------------------------------------------------

    @PostMapping("/secretariat/profil/{id}/demande-modification")
    public String demandeModification(
            @PathVariable Long id,
            @RequestParam String champModifie,
            @RequestParam(required = false) String ancienneValeur,
            @RequestParam String nouvelleValeur,
            @RequestParam(required = false) String motif,
            RedirectAttributes redirectAttrs) {
        try {
            eleveService.soumettreDemandeModification(id, champModifie, ancienneValeur, nouvelleValeur, motif);
            redirectAttrs.addFlashAttribute("successMessage", "Demande de modification soumise avec succès.");
        } catch (Exception e) {
            log.error("Erreur demande modification élève {} : {}", id, e.getMessage(), e);
            redirectAttrs.addFlashAttribute("errorMessage", "Erreur : " + e.getMessage());
        }
        return "redirect:/secretariat/profil/" + id;
    }

    // ----------------------------------------------------------------
    //  Module Élève – Ajout (POST)
    // ----------------------------------------------------------------

    @PostMapping("/secretariat/eleves/ajouter")
    public String ajouterEleve(@ModelAttribute AjoutEleveDTO dto,
                                RedirectAttributes redirectAttrs) {
        try {
            eleveService.ajouterEleve(dto);
            redirectAttrs.addFlashAttribute("successMessage", "Élève ajouté avec succès !");
        } catch (Exception e) {
            log.error("Erreur ajout élève : {}", e.getMessage(), e);
            redirectAttrs.addFlashAttribute("errorMessage", "Erreur : " + e.getMessage());
        }
        return "redirect:/secretariat/eleves";
    }

    // ----------------------------------------------------------------
    //  Helpers privés
    // ----------------------------------------------------------------

    private String safe(String s) {
        if (s == null) return "";
        // Échapper les points-virgules pour CSV
        return s.replace(";", ",").replace("\n", " ").replace("\r", "");
    }

    private String buildHtmlProfil(EleveProfilDTO e) {
        return """
            <!DOCTYPE html>
            <html lang="fr">
            <head>
              <meta charset="UTF-8"/>
              <title>Profil – %s %s</title>
              <style>
                body { font-family: Arial, sans-serif; padding: 30px; color: #111; }
                h1 { font-size: 22px; margin-bottom: 4px; }
                .sub { color: #666; font-size: 13px; margin-bottom: 24px; }
                table { width: 100%%; border-collapse: collapse; margin-top: 16px; }
                th, td { border: 1px solid #ddd; padding: 8px 12px; font-size: 13px; text-align: left; }
                th { background: #f4f4f4; }
                h2 { font-size: 15px; margin-top: 28px; }
                @media print { body { padding: 0; } }
              </style>
            </head>
            <body>
              <h1>%s %s</h1>
              <div class="sub">%s &nbsp;·&nbsp; %s</div>
              <h2>Informations personnelles</h2>
              <table>
                <tr><th>Date de naissance</th><td>%s</td></tr>
                <tr><th>Quartier</th><td>%s</td></tr>
                <tr><th>Adresse</th><td>%s</td></tr>
                <tr><th>Date d'inscription</th><td>%s</td></tr>
                <tr><th>Parent / Tuteur</th><td>%s %s</td></tr>
                <tr><th>Contact parent</th><td>%s</td></tr>
              </table>
              <h2>Historique de paiement</h2>
              <table>
                <tr><th>Mois</th><th>Date paiement</th><th>Montant</th><th>Statut</th></tr>
                %s
              </table>
              <script>window.onload = function(){ window.print(); }</script>
            </body>
            </html>
            """.formatted(
                e.getPrenom(), e.getNom(),
                e.getPrenom(), e.getNom(),
                e.getMatricule(), e.getNomClasse() != null ? e.getNomClasse() : "",
                e.getDateNaissance() != null ? e.getDateNaissance().toString() : "—",
                e.getCommune() != null ? e.getCommune() : "—",
                e.getAdresse() != null ? e.getAdresse() : "—",
                e.getDateInscription() != null ? e.getDateInscription().toString() : "—",
                e.getPrenomParent() != null ? e.getPrenomParent() : "",
                e.getNomParent() != null ? e.getNomParent() : "—",
                e.getTelephoneParent() != null ? e.getTelephoneParent() : "—",
                buildPaiementsRows(e)
        );
    }

    private String buildPaiementsRows(EleveProfilDTO e) {
        if (e.getHistoriquesPaiements() == null || e.getHistoriquesPaiements().isEmpty()) {
            return "<tr><td colspan='4' style='color:#999;'>Aucun paiement enregistré.</td></tr>";
        }
        StringBuilder sb = new StringBuilder();
        for (PaiementHistoriqueDTO p : e.getHistoriquesPaiements()) {
            sb.append("<tr><td>").append(p.getMoisLabel()).append("</td>")
              .append("<td>").append(p.getDatePaiement() != null ? p.getDatePaiement() : "—").append("</td>")
              .append("<td>").append(p.getMontant() != null ? p.getMontant().toPlainString() + " Ar" : "—").append("</td>")
              .append("<td>").append(p.getStatut()).append("</td></tr>");
        }
        return sb.toString();
    }
}