## 📊 Phase Bilan — Paiements (Tiavina)

### Étape 1 — DTO
- [ ] Créer `src/main/java/com/ecole/dto/Secretaire/BilanClasseDTO.java`
  - champs : `nomClasse`, `totalAttendu`, `totalEncaisse`, `nombreEleves`, `nombreSoldes`
- [ ] Créer `src/main/java/com/ecole/dto/Secretaire/BilanGlobalDTO.java`
  - champs : `totalAttenduGlobal`, `totalEncaisseGlobal`, `lignes` (liste de BilanClasseDTO)

### Étape 2 — Service
- [ ] Ajouter `getBilanGlobal()` dans `PaiementService` : calculer les totaux par classe
- [ ] Ajouter `exportPdf(BilanGlobalDTO)` : générer le PDF iText
- [ ] Ajouter `exportExcel(BilanGlobalDTO)` : générer le fichier Excel POI

### Étape 3 — Controller
- [ ] Brancher `GET /secretariat/bilan` sur `getBilanGlobal()`
- [ ] Ajouter `GET /secretariat/bilan/export/pdf` : télécharger le PDF
- [ ] Ajouter `GET /secretariat/bilan/export/excel` : télécharger le Excel

### Étape 4 — Vue
- [ ] Brancher `bilan.html` sur les données réelles
- [ ] Ajouter camembert Chart.js (payés vs impayés)
- [ ] Ajouter boutons export PDF et Excel