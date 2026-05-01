package com.gestion.fabrication.controller;

import com.gestion.fabrication.entity.OrdreFabrication;
import com.gestion.fabrication.repository.OrdreFabricationRepository;
import com.gestion.fabrication.repository.ProduitRepository;
import com.gestion.fabrication.repository.MachineRepository;
import com.gestion.fabrication.repository.EmployeRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controller OrdreFabrication — endpoints REST pour les ordres.
 *
 * Endpoints disponibles :
 *   GET    /api/ordres            → liste tous les ordres
 *   GET    /api/ordres/{id}       → un ordre par ID
 *   POST   /api/ordres            → créer un ordre
 *   PUT    /api/ordres/{id}       → modifier un ordre
 *   DELETE /api/ordres/{id}       → supprimer un ordre
 *   PATCH  /api/ordres/{id}/etat  → changer l'état d'un ordre
 */
@RestController
@RequestMapping("/api/ordres")
@CrossOrigin(origins = "http://localhost:4200")
public class OrdreFabricationController {

    // Injection par constructeur (recommandée par Spring)
    private final OrdreFabricationRepository ordreRepo;
    private final ProduitRepository produitRepo;
    private final MachineRepository machineRepo;
    private final EmployeRepository employeRepo;

    public OrdreFabricationController(
            OrdreFabricationRepository ordreRepo,
            ProduitRepository produitRepo,
            MachineRepository machineRepo,
            EmployeRepository employeRepo) {
        this.ordreRepo = ordreRepo;
        this.produitRepo = produitRepo;
        this.machineRepo = machineRepo;
        this.employeRepo = employeRepo;
    }

    // ==================== GET ALL ====================
    @GetMapping
    public List<OrdreFabrication> getAll() {
        return ordreRepo.findAll();
    }

    // ==================== GET BY ID ====================
    @GetMapping("/{id}")
    public ResponseEntity<OrdreFabrication> getById(@PathVariable Long id) {
        return ordreRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ==================== POST (CREER) ====================
    /**
     * Exemple de body JSON pour créer un ordre :
     * {
     *   "projet": "Projet Alpha",
     *   "quantite": 50,
     *   "date": "2026-04-04",
     *   "etat": "EN_ATTENTE",
     *   "produit": { "id": 1 },
     *   "machine": { "id": 2 },
     *   "employe": { "id": 3 }
     * }
     */
    @PostMapping
    public ResponseEntity<OrdreFabrication> create(@Valid @RequestBody OrdreFabrication ordre) {

        // Vérification que le produit existe (obligatoire)
        if (ordre.getProduit() != null && ordre.getProduit().getId() != null) {
            produitRepo.findById(ordre.getProduit().getId())
                    .ifPresent(ordre::setProduit);
        }

        // Vérification que la machine existe (optionnel)
        if (ordre.getMachine() != null && ordre.getMachine().getId() != null) {
            machineRepo.findById(ordre.getMachine().getId())
                    .ifPresent(ordre::setMachine);
        }

        // Vérification que l'employé existe (optionnel)
        if (ordre.getEmploye() != null && ordre.getEmploye().getId() != null) {
            employeRepo.findById(ordre.getEmploye().getId())
                    .ifPresent(ordre::setEmploye);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(ordreRepo.save(ordre));
    }

    // ==================== PUT (MODIFIER) ====================
    @PutMapping("/{id}")
    public ResponseEntity<OrdreFabrication> update(
            @PathVariable Long id,
            @Valid @RequestBody OrdreFabrication o) {

        return ordreRepo.findById(id)
                .map(existing -> {
                    existing.setProjet(o.getProjet());
                    existing.setQuantite(o.getQuantite());
                    existing.setDate(o.getDate());
                    existing.setEtat(o.getEtat());

                    // Mise à jour produit
                    if (o.getProduit() != null && o.getProduit().getId() != null) {
                        produitRepo.findById(o.getProduit().getId())
                                .ifPresent(existing::setProduit);
                    }

                    // Mise à jour machine
                    if (o.getMachine() != null && o.getMachine().getId() != null) {
                        machineRepo.findById(o.getMachine().getId())
                                .ifPresent(existing::setMachine);
                    } else {
                        existing.setMachine(null);
                    }

                    // Mise à jour employé
                    if (o.getEmploye() != null && o.getEmploye().getId() != null) {
                        employeRepo.findById(o.getEmploye().getId())
                                .ifPresent(existing::setEmploye);
                    } else {
                        existing.setEmploye(null);
                    }

                    return ResponseEntity.ok(ordreRepo.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ==================== DELETE ====================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!ordreRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        ordreRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== PATCH ETAT ====================
    /**
     * Changer uniquement l'état d'un ordre.
     * Exemple : PATCH /api/ordres/1/etat
     * Body : "EN_COURS"
     *
     * Transitions logiques :
     *   EN_ATTENTE → EN_COURS → TERMINE
     *   EN_ATTENTE → ANNULE
     */
    @PatchMapping("/{id}/etat")
    public ResponseEntity<OrdreFabrication> changerEtat(
            @PathVariable Long id,
            @RequestBody String nouvelEtat) {

        return ordreRepo.findById(id)
                .map(ordre -> {
                    ordre.setEtat(nouvelEtat.replace("\"", "").trim());
                    return ResponseEntity.ok(ordreRepo.save(ordre));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ==================== GET PAR ETAT ====================
    /**
     * Filtrer les ordres par état.
     * Exemple : GET /api/ordres/etat/EN_COURS
     */
    @GetMapping("/etat/{etat}")
    public List<OrdreFabrication> getByEtat(@PathVariable String etat) {
        return ordreRepo.findByEtat(etat);
    }
}
