package com.gestion.fabrication.controller;

import com.gestion.fabrication.entity.Machine;
import com.gestion.fabrication.repository.MachineRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/machines")
@CrossOrigin(origins = "http://localhost:4200")
public class MachineController {

    private final MachineRepository repo;

    public MachineController(MachineRepository repo) {
        this.repo = repo;
    }

    // ==================== CRUD DE BASE ====================

    // GET toutes les machines
    @GetMapping
    public List<Machine> getAll() {
        return repo.findAll();
    }

    // GET une machine par ID
    @GetMapping("/{id}")
    public ResponseEntity<Machine> getById(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST créer une machine
    @PostMapping
    public ResponseEntity<Machine> create(@Valid @RequestBody Machine m) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(m));
    }

    // PUT modifier une machine
    @PutMapping("/{id}")
    public ResponseEntity<Machine> update(
            @PathVariable Long id,
            @Valid @RequestBody Machine m) {
        return repo.findById(id)
                .map(existing -> {
                    existing.setNom(m.getNom());
                    existing.setEtat(m.getEtat());
                    existing.setDerniereMaintenance(m.getDerniereMaintenance());
                    return ResponseEntity.ok(repo.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE supprimer une machine
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== ENDPOINTS METIER ====================

    /**
     * ENDPOINT 1 : Machines disponibles
     * GET /api/machines/disponibles
     *
     * Retourne uniquement les machines avec etat = "DISPONIBLE"
     * Utilisé par le frontend pour afficher quelles machines
     * peuvent être affectées à un ordre de fabrication.
     */
    @GetMapping("/disponibles")
    public List<Machine> getMachinesDisponibles() {
        return repo.findByEtat("DISPONIBLE");
    }

    /**
     * ENDPOINT 2 : Machines par état
     * GET /api/machines/etat/{etat}
     *
     * Exemple : GET /api/machines/etat/EN_MAINTENANCE
     * Valeurs possibles : DISPONIBLE, EN_MAINTENANCE, HORS_SERVICE
     */
    @GetMapping("/etat/{etat}")
    public List<Machine> getByEtat(@PathVariable String etat) {
        return repo.findByEtat(etat);
    }

    /**
     * ENDPOINT 3 : Démarrer la maintenance d'une machine
     * PATCH /api/machines/{id}/maintenance/debut
     *
     * Change l'état à EN_MAINTENANCE
     * Enregistre la date d'aujourd'hui comme dernière maintenance
     */
    @PatchMapping("/{id}/maintenance/debut")
    public ResponseEntity<Machine> demarrerMaintenance(@PathVariable Long id) {
        return repo.findById(id)
                .map(machine -> {
                    machine.setEtat("EN_MAINTENANCE");
                    machine.setDerniereMaintenance(LocalDate.now());
                    return ResponseEntity.ok(repo.save(machine));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * ENDPOINT 4 : Terminer la maintenance d'une machine
     * PATCH /api/machines/{id}/maintenance/fin
     *
     * Remet l'état à DISPONIBLE après la maintenance
     */
    @PatchMapping("/{id}/maintenance/fin")
    public ResponseEntity<Machine> terminerMaintenance(@PathVariable Long id) {
        return repo.findById(id)
                .map(machine -> {
                    machine.setEtat("DISPONIBLE");
                    return ResponseEntity.ok(repo.save(machine));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * ENDPOINT 5 : Machines à maintenir
     * GET /api/machines/a-maintenir
     *
     * Retourne les machines dont la dernière maintenance
     * est antérieure à 6 mois, ou jamais maintenues.
     * Utile pour le suivi préventif.
     */
    @GetMapping("/a-maintenir")
    public List<Machine> getMachinesAMaintenir() {
        LocalDate seuil = LocalDate.now().minusMonths(6);
        List<Machine> anciennes = repo.findByDerniereMaintenanceBefore(seuil);
        List<Machine> jamais = repo.findByDerniereMaintenanceIsNull();
        anciennes.addAll(jamais);
        return anciennes;
    }
}
