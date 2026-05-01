package com.gestion.fabrication.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "machines")
public class Machine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom de la machine est obligatoire")
    @Column(nullable = false, length = 100)
    private String nom;

    @NotBlank(message = "L'état est obligatoire")
    @Column(nullable = false, length = 30)
    private String etat;

    @Column(name = "derniere_maintenance")
    private LocalDate derniereMaintenance;

    /**
     * Relation BIDIRECTIONNELLE avec Employe.
     * mappedBy = "machineAssignee" → attribut dans Employe.java
     * Question : "Quels employés travaillent sur cette machine ?"
     */
    @OneToMany(mappedBy = "machineAssignee")
    @JsonIgnore
    private List<Employe> employes = new ArrayList<>();

    /**
     * Relation BIDIRECTIONNELLE avec OrdreFabrication.
     * mappedBy = "machine" → attribut dans OrdreFabrication.java
     * Question : "Quels ordres utilisent cette machine ?"
     */
    @OneToMany(mappedBy = "machine")
    @JsonIgnore
    private List<OrdreFabrication> ordres = new ArrayList<>();
}
