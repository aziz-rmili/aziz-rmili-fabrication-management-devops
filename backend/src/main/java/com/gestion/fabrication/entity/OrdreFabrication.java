package com.gestion.fabrication.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "ordres_fabrication")
public class OrdreFabrication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le projet est obligatoire")
    @Column(nullable = false, length = 150)
    private String projet;

    /**
     * Relation BIDIRECTIONNELLE côté OrdreFabrication → Produit.
     *
     * @ManyToOne : plusieurs ordres → un seul produit
     * @JoinColumn : crée produit_id dans ordres_fabrication
     *
     * @JsonIgnoreProperties : on ignore la liste "ordres" dans Produit
     * pour éviter la boucle infinie JSON
     */
    @NotNull(message = "Le produit est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produit_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "ordres"})
    private Produit produit;

    /**
     * Relation BIDIRECTIONNELLE côté OrdreFabrication → Machine.
     *
     * @ManyToOne : plusieurs ordres → une seule machine
     * @JoinColumn : crée machine_id dans ordres_fabrication
     * nullable = true : un ordre peut ne pas avoir de machine encore
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "employes", "ordres"})
    private Machine machine;

    /**
     * Relation BIDIRECTIONNELLE côté OrdreFabrication → Employe.
     *
     * @ManyToOne : plusieurs ordres → un seul employé
     * @JoinColumn : crée employe_id dans ordres_fabrication
     * nullable = true : un ordre peut ne pas avoir d'employé encore
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employe_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "ordres", "machineAssignee"})
    private Employe employe;

    @Min(value = 1, message = "La quantité doit être au moins 1")
    @Column(nullable = false)
    private int quantite;

    @NotNull(message = "La date est obligatoire")
    @Column(nullable = false)
    private LocalDate date;

    // Valeurs : EN_ATTENTE, EN_COURS, TERMINE, ANNULE
    @NotBlank(message = "L'état est obligatoire")
    @Column(nullable = false, length = 20)
    private String etat;
}
