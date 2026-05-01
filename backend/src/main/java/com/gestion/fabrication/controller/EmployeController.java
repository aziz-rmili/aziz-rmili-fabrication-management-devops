package com.gestion.fabrication.controller;

import com.gestion.fabrication.entity.Employe;
import com.gestion.fabrication.repository.EmployeRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/employes")
@CrossOrigin(origins = "http://localhost:4200")
public class EmployeController {

    private final EmployeRepository repo;

    public EmployeController(EmployeRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Employe> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employe> getById(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Employe> create(@Valid @RequestBody Employe e) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(e));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employe> update(@PathVariable Long id, @Valid @RequestBody Employe e) {
        return repo.findById(id)
                .map(existing -> {
                    existing.setNom(e.getNom());
                    existing.setPoste(e.getPoste());
                    existing.setMachineAssignee(e.getMachineAssignee());
                    return ResponseEntity.ok(repo.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
