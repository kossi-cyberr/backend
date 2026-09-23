package com.example.versuion.Dto;

import com.example.versuion.models.LigneVente;
import com.example.versuion.models.Ventes;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Builder
@Data
public class VentesDto {

    private Long id;

    private String code;

    private Instant dateVente;

    private String commentaire;

    /** Nom du client (optionnel), information enregistrée sur le ticket/la vente. */
    private String nomClient;

    /** Nom du vendeur ayant enregistré la vente (lecture seule, renseigné côté serveur). */
    private String nomVendeur;

    private List<LigneVentDto> ligneVentes;

    private Integer idEntreprise;

    public static VentesDto fromEntity(Ventes vente) {
        if (vente == null) {
            return null;
        }
        return VentesDto.builder()
                .id(vente.getId())
                .code(vente.getCode())
                .dateVente(vente.getDateVente())
                .commentaire(vente.getCommentaire())
                .nomClient(vente.getNomClient())
                .nomVendeur(vente.getVendeur() != null
                        ? String.format("%s %s",
                                vente.getVendeur().getNom() != null ? vente.getVendeur().getNom() : "",
                                vente.getVendeur().getPrenom() != null ? vente.getVendeur().getPrenom() : "").trim()
                        : null)
                // Les lignes sont exposées sans leur vente (pas de récursion JSON)
                .ligneVentes(vente.getLigneVentes() == null ? null
                        : vente.getLigneVentes().stream().map(LigneVentDto::fromEntitySansVente).toList())
                .idEntreprise(vente.getIdEntreprise())
                .build();
    }

    public static Ventes toEntity(VentesDto dto) {
        if (dto == null) {
            return null;
        }
        Ventes ventes = new Ventes();
        ventes.setId(dto.getId());
        ventes.setCode(dto.getCode());
        ventes.setDateVente(dto.getDateVente());
        ventes.setCommentaire(dto.getCommentaire());
        ventes.setNomClient(dto.getNomClient());
        ventes.setIdEntreprise(dto.getIdEntreprise());
        // Le vendeur est renseigné côté serveur (VentesServiceImp) et jamais par le client
        ventes.setVendeur(null);
        return ventes;
    }
}
