package com.gestion.fabrication.repository;

import com.gestion.fabrication.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {

    // Chercher les produits par type
    // Spring génère → SELECT * FROM produits WHERE type = ?
    List<Produit> findByType(String type);

    // Chercher les produits par fournisseur
    // Spring génère → SELECT * FROM produits WHERE fournisseur = ?
    List<Produit> findByFournisseur(String fournisseur);

    // Produits avec stock inférieur à un seuil (alertes stock bas)
    // Spring génère → SELECT * FROM produits WHERE stock < ?
    List<Produit> findByStockLessThan(int seuil);

    // Recherche par nom (insensible à la casse)
    // Spring génère → SELECT * FROM produits WHERE LOWER(nom) LIKE %?%
    List<Produit> findByNomContainingIgnoreCase(String nom);
}
