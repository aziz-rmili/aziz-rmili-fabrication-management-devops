package com.gestion.fabrication.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "produits")
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Column(nullable = false, length = 100)
    private String nom;

    @NotBlank(message = "Le type est obligatoire")
    @Column(nullable = false, length = 50)
    private String type;

    @Min(value = 0, message = "Le stock ne peut pas être négatif")
    @Column(nullable = false)
    private int stock;

    @NotBlank(message = "Le fournisseur est obligatoire")
    @Column(nullable = false, length = 100)
    private String fournisseur;

    /**
     * Relation BIDIRECTIONNELLE avec OrdreFabrication.
     *
     * mappedBy = "produit" → nom de l'attribut dans OrdreFabrication.java
     *
     * On peut ainsi répondre à la question :
     * "Quels sont tous les ordres qui fabriquent ce produit ?"
     *
     * @JsonIgnore : évite la boucle infinie JSON
     * Produit → liste d'ordres → chaque ordre → son produit → ...
     */
    @OneToMany(mappedBy = "produit")
    @JsonIgnore
    private List<OrdreFabrication> ordres = new ArrayList<>();
}
