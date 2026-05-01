package com.gestion.fabrication.controller;

import com.gestion.fabrication.entity.Produit;
import com.gestion.fabrication.repository.ProduitRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/produits")
@CrossOrigin(origins = "http://localhost:4200")  // autorise Angular
public class ProduitController {

    private final ProduitRepository produitRepository;

    public ProduitController(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    // GET tous les produits → http://localhost:8080/api/produits
    @GetMapping
    public List<Produit> getAll() {
        return produitRepository.findAll();
    }

    // GET un produit par ID → http://localhost:8080/api/produits/1
    @GetMapping("/{id}")
    public ResponseEntity<Produit> getById(@PathVariable Long id) {
        return produitRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST créer un produit → @Valid déclenche la validation Spring
    @PostMapping
    public ResponseEntity<Produit> create(@Valid @RequestBody Produit p) {
        Produit saved = produitRepository.save(p);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // PUT modifier un produit
    @PutMapping("/{id}")
    public ResponseEntity<Produit> update(@PathVariable Long id, @Valid @RequestBody Produit p) {
        return produitRepository.findById(id)
                .map(existing -> {
                    existing.setNom(p.getNom());
                    existing.setType(p.getType());
                    existing.setStock(p.getStock());
                    existing.setFournisseur(p.getFournisseur());
                    return ResponseEntity.ok(produitRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE supprimer un produit
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!produitRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        produitRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
