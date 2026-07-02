package com.ecole.dto.Secretaire;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.Map;

@Getter @Setter
@AllArgsConstructor
public class EleveListeDTO {
    private Integer id;
    private String matricule;
    private String nom;
    private String prenom;
    private String nomClasse;
    private String niveau;
    private String photoUrl;
    private Map<Integer, Boolean> paiementsMois;
}