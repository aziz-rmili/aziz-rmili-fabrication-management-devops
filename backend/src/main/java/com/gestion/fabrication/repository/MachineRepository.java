package com.gestion.fabrication.repository;

import com.gestion.fabrication.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository Machine — couche DAO.
 *
 * Méthodes spécifiques pour :
 *   - Suivi des maintenances (ton sujet le demande explicitement)
 *   - Disponibilité des machines
 */
@Repository
public interface MachineRepository extends JpaRepository<Machine, Long> {

    // Besoin : trouver les machines par état
    // (DISPONIBLE, EN_MAINTENANCE, HORS_SERVICE)
    // Spring génère → SELECT * FROM machines WHERE etat = ?
    List<Machine> findByEtat(String etat);

    // Besoin : suivi des maintenances
    // Trouver les machines dont la maintenance est avant une date
    // Spring génère → SELECT * FROM machines WHERE derniere_maintenance < ?
    List<Machine> findByDerniereMaintenanceBefore(LocalDate date);

    // Besoin : machines jamais maintenues (derniereMaintenance = null)
    // Spring génère → SELECT * FROM machines WHERE derniere_maintenance IS NULL
    List<Machine> findByDerniereMaintenanceIsNull();
}