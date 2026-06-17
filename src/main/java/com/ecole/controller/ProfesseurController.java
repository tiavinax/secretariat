package com.ecole.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfesseurController {

    @GetMapping("/professeur/emploi")
    public String emploi(Model model) {
        model.addAttribute("pageTitle", "Emploi du Temps");
        model.addAttribute("currentRole", "professeur");
        return "Professeur/calendar";
    }

    @GetMapping("/professeur/notes")
    public String notes(Model model) {
        model.addAttribute("pageTitle", "Notes des Élèves");
        model.addAttribute("currentRole", "professeur");
        return "Professeur/notes";
    }

    @GetMapping("/professeur/devoirs")
    public String devoirs(Model model) {
        model.addAttribute("pageTitle", "Devoirs & Leçons");
        model.addAttribute("currentRole", "professeur");
        return "Professeur/devoirs";
    }

    @GetMapping("/professeur/bulletins")
    public String bulletins(Model model) {
        model.addAttribute("pageTitle", "Bulletins");
        model.addAttribute("currentRole", "professeur");
        return "Professeur/bulletin";
    }

    @GetMapping("/professeur/profil")
    public String profil(Model model) {
        model.addAttribute("pageTitle", "Mon Profil");
        model.addAttribute("currentRole", "professeur");
        return "Professeur/profil";
    }
}
