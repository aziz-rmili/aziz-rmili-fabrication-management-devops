package com.gestion.fabrication.repository;

import com.gestion.fabrication.entity.OrdreFabrication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository OrdreFabrication — couche DAO.
 *
 * Cours du prof slide 47-49 :
 * En étendant JpaRepository on hérite de :
 *   save(), findAll(), findById(), deleteById()...
 *
 * On ajoute des méthodes personnalisées — Spring génère
 * le SQL automatiquement à partir du nom de la méthode !
 * Cours slide 60 : findByQteLessThan → SELECT WHERE quantite < ?
 */
public interface OrdreFabricationRepository extends JpaRepository<OrdreFabrication, Long> {

    // SELECT * FROM ordres_fabrication WHERE etat = ?
    List<OrdreFabrication> findByEtat(String etat);

    // SELECT * FROM ordres_fabrication WHERE produit_id = ?
    List<OrdreFabrication> findByProduitId(Long produitId);

    // SELECT * FROM ordres_fabrication WHERE machine_id = ?
    List<OrdreFabrication> findByMachineId(Long machineId);

    // SELECT * FROM ordres_fabrication WHERE employe_id = ?
    List<OrdreFabrication> findByEmployeId(Long employeId);
}
