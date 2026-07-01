package com.ecole.controller;

import com.ecole.entity.Secretaire.*;
import com.ecole.service.Secretaire.PaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.ecole.dto.Secretaire.BilanGlobalDTO;
import com.ecole.dto.Secretaire.PaiementGroupeDTO;

import jakarta.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

@Controller
public class SecretaireController {

    @Autowired
    private PaiementService paiementService;

    // ─── PAIEMENT ────────────────────────────────────────────────

    @GetMapping("/secretariat/paiement")
    public String paiement(Model model) {
        List<Inscription> inscriptions = paiementService.getInscriptionsActives();
        model.addAttribute("inscriptions", inscriptions);
        model.addAttribute("pageTitle", "Ajouter un Paiement");
        return "Secretaire/paiement";
    }

    // Chargement AJAX des échéances ouvertes quand on sélectionne un élève
    @GetMapping("/secretariat/paiement/echeances")
    @ResponseBody
    public List<Echeance> getEcheances(@RequestParam Integer inscriptionId) {
        return paiementService.getEcheancesOuvertes(inscriptionId);
    }

    // Enregistrement du paiement
    // Modifier le POST paiement
    @PostMapping("/secretariat/paiement")
    public String enregistrerPaiement(
            @RequestParam Integer inscriptionId,
            @RequestParam Integer echeanceId,
            @RequestParam List<BigDecimal> montants,
            @RequestParam List<String> modesPaiement,
            RedirectAttributes redirectAttributes) {

        try {
            // Construire le DTO
            PaiementGroupeDTO dto = new PaiementGroupeDTO();
            dto.setInscriptionId(inscriptionId);
            dto.setEcheanceId(echeanceId);

            List<PaiementGroupeDTO.LignePaiement> lignes = new ArrayList<>();
            for (int i = 0; i < montants.size(); i++) {
                PaiementGroupeDTO.LignePaiement ligne = new PaiementGroupeDTO.LignePaiement();
                ligne.setMontant(montants.get(i));
                ligne.setModePaiement(modesPaiement.get(i));
                lignes.add(ligne);
            }
            dto.setLignes(lignes);

            String refGroupe = paiementService.enregistrerPaiementGroupe(dto);
            return "redirect:/secretariat/paiements/groupe/" + refGroupe + "/facture";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur : " + e.getMessage());
            return "redirect:/secretariat/paiement";
        }
    }

    // Page facture détail
    @GetMapping("/secretariat/paiements/groupe/{reference}/facture")
    public String factureDetail(@PathVariable String reference, Model model) {
        List<Paiement> lignes = paiementService.getPaiementsByReference(reference);
        if (lignes.isEmpty())
            return "redirect:/secretariat/paiements";

        BigDecimal total = lignes.stream()
                .map(Paiement::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("lignes", lignes);
        model.addAttribute("reference", reference);
        model.addAttribute("total", total);
        model.addAttribute("paiement", lignes.get(0));
        model.addAttribute("pageTitle", "Facture — " + reference);
        return "Secretaire/facture_detail";
    }

    // Export PDF facture groupe
    @GetMapping("/secretariat/paiements/groupe/{reference}/pdf")
    public void factureGroupePdf(@PathVariable String reference,
            HttpServletResponse response) throws Exception {
        List<Paiement> lignes = paiementService.getPaiementsByReference(reference);
        BigDecimal total = lignes.stream()
                .map(Paiement::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        byte[] pdf = paiementService.exportFactureGroupePdf(reference, lignes, total);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=facture-" + reference + ".pdf");
        response.setContentLength(pdf.length);
        response.getOutputStream().write(pdf);
    }

    @GetMapping("/secretariat/paiements")
    public String listePaiements(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String classe,
            @RequestParam(required = false) String modePaiement,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            Model model) {

        List<Paiement> paiements = paiementService.getPaiementsFiltres(
                nom, classe, modePaiement, dateDebut, dateFin);
        model.addAttribute("paiements", paiements);
        model.addAttribute("nom", nom);
        model.addAttribute("classe", classe);
        model.addAttribute("modePaiement", modePaiement);
        model.addAttribute("dateDebut", dateDebut);
        model.addAttribute("dateFin", dateFin);
        model.addAttribute("pageTitle", "Liste des Paiements");
        return "Secretaire/liste_paiements";
    }

    // Facture PDF par id paiement simple
    @GetMapping("/secretariat/paiements/simple/{id}/pdf")
    public void factturePdf(@PathVariable Integer id,
            HttpServletResponse response) throws Exception {
        byte[] pdf = paiementService.exportFacturePdf(id);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=facture-REC-" + id + ".pdf");
        response.setContentLength(pdf.length);
        response.getOutputStream().write(pdf);
    }

    // ─── BILAN ───────────────────────────────────────────────────

    @GetMapping("/secretariat/bilan")
    public String bilan(Model model) {
        BilanGlobalDTO bilan = paiementService.getBilanGlobal();
        model.addAttribute("bilan", bilan);
        model.addAttribute("pageTitle", "Bilan de Paiement");
        return "Secretaire/bilan";
    }

    @GetMapping("/secretariat/bilan/export/pdf")
    public void exportPdf(HttpServletResponse response) throws Exception {
        BilanGlobalDTO bilan = paiementService.getBilanGlobal();
        byte[] pdf = paiementService.exportPdf(bilan);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=bilan_paiements.pdf");
        response.setContentLength(pdf.length);
        response.getOutputStream().write(pdf);
    }

    @GetMapping("/secretariat/bilan/export/excel")
    public void exportExcel(HttpServletResponse response) throws Exception {
        BilanGlobalDTO bilan = paiementService.getBilanGlobal();
        byte[] excel = paiementService.exportExcel(bilan);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=bilan_paiements.xlsx");
        response.setContentLength(excel.length);
        response.getOutputStream().write(excel);
    }

    // ─── ÉLÈVES ──────────────────────────────────────────────────

    @GetMapping("/secretariat/eleves")
    public String eleves(Model model) {
        List<Inscription> inscriptions = paiementService.getInscriptionsActives();
        model.addAttribute("inscriptions", inscriptions);
        model.addAttribute("pageTitle", "Liste des Élèves");
        return "Secretaire/eleves";
    }

    @GetMapping("/secretariat/profil")
    public String profil(Model model) {
        model.addAttribute("pageTitle", "Profil de l'Élève");
        return "Secretaire/profil_eleve";
    }
}