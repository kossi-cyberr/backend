package com.example.versuion.models;

import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class Ventes extends AbstractEntity{

    private String code;

    private Instant dateVente;

    private String commentaire;

    /** Nom du client (optionnel), simple information enregistrée sur le ticket/la vente. */
    private String nomClient;

    /** Vendeur (utilisateur connecté) ayant enregistré la vente, renseigné côté serveur. */
    @ManyToOne
    @JoinColumn(name = "vendeur_id")
    private Utilisateurs vendeur;

    @Column(name = "identreprise")
    private Integer idEntreprise;

    @OneToMany(mappedBy = "vente")
    private List<LigneVente> ligneVentes;
}
