package com.ecole.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DirecteurController {

    @GetMapping("/directeur/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("pageTitle", "Tableau de bord");
        model.addAttribute("currentRole", "directeur");
        return "directeur/dashboard";
    }

    @GetMapping("/directeur/finances")
    public String finances(Model model) {
        model.addAttribute("pageTitle", "Finances & Bénéfices");
        model.addAttribute("currentRole", "directeur");
        return "directeur/finances";
    }

    @GetMapping("/directeur/professeurs")
    public String professeurs(Model model) {
        model.addAttribute("pageTitle", "Corps Professoral");
        model.addAttribute("currentRole", "directeur");
        return "directeur/professeurs";
    }

    @GetMapping("/directeur/profil_professeur")
    public String profilProfesseur(Model model) {
        model.addAttribute("pageTitle", "Profil du Professeur");
        model.addAttribute("currentRole", "directeur");
        return "directeur/profil_professeur";
    }

    @GetMapping("/directeur/ecolages")
    public String ecolages(Model model) {
        model.addAttribute("pageTitle", "Écolages du Mois");
        model.addAttribute("currentRole", "directeur");
        return "directeur/ecolages";
    }
}
