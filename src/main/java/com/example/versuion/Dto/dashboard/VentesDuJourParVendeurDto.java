package com.example.versuion.Dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Projection JPQL : ventes du jour groupées par vendeur.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VentesDuJourParVendeurDto {

    private Long idVendeur;

    private String nomVendeur;

    /** Nombre de ventes enregistrées aujourd'hui par ce vendeur. */
    private Long nombreVentes;

    /** Montant total (CA) des ventes du jour de ce vendeur. */
    private BigDecimal chiffreAffaires;

    /** Nombre d'articles vendus (somme des quantités) aujourd'hui par ce vendeur. */
    private BigDecimal articlesVendus;
}
