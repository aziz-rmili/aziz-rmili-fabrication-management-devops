package com.gestion.fabrication.repository;

import com.gestion.fabrication.entity.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmployeRepository extends JpaRepository<Employe, Long> {

    // Trouver les employés assignés à une machine spécifique
    // Spring génère → SELECT * FROM employes WHERE machine_id = ?
    List<Employe> findByMachineAssigneeId(Long machineId);

    // Trouver les employés sans machine assignée
    // Spring génère → SELECT * FROM employes WHERE machine_id IS NULL
    List<Employe> findByMachineAssigneeIsNull();

    // Trouver les employés par poste
    // Spring génère → SELECT * FROM employes WHERE poste = ?
    List<Employe> findByPoste(String poste);

    // Recherche par nom (insensible à la casse)
    // Spring génère → SELECT * FROM employes WHERE LOWER(nom) LIKE %?%
    List<Employe> findByNomContainingIgnoreCase(String nom);
}
