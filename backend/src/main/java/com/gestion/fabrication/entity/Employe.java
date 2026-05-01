package com.gestion.fabrication.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "employes")
public class Employe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Column(nullable = false, length = 100)
    private String nom;

    @NotBlank(message = "Le poste est obligatoire")
    @Column(nullable = false, length = 100)
    private String poste;

    /**
     * Relation BIDIRECTIONNELLE côté Employe → Machine.
     *
     * @ManyToOne : plusieurs employés → une seule machine
     * @JoinColumn : crée machine_id dans la table employes
     *
     * Question : "Sur quelle machine travaille cet employé ?"
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "employes", "ordres"})
    private Machine machineAssignee;

    /**
     * Relation BIDIRECTIONNELLE avec OrdreFabrication.
     * mappedBy = "employe" → attribut dans OrdreFabrication.java
     * Question : "Quels ordres sont gérés par cet employé ?"
     */
    @OneToMany(mappedBy = "employe")
    @JsonIgnore
    private List<OrdreFabrication> ordres = new ArrayList<>();
}
