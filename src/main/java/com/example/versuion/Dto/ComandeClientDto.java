package com.example.versuion.Dto;

import com.example.versuion.models.Client;
import com.example.versuion.models.CommandeClient;
import com.example.versuion.models.EtatCommande;
import com.example.versuion.models.LigneComandeClient;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Builder
@Data
public class ComandeClientDto {

    private Long id;

    private String code;

    private Instant dateComande;

    private EtatCommande etatCommande;

    private ClientDto client;

    /** Nom du vendeur ayant créé la commande (renseigné côté serveur, lecture seule). */
    private String nomVendeur;

    /** Date de livraison effective (renseignée au passage à l'état LIVREE). */
    private Instant dateLivraison;

    /** Montant total de la commande = somme (quantité × prix unitaire) des lignes. */
    private BigDecimal montantTotal;

    private Integer idEntreprise;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<LigneCommandeClientDto> ligneComandeClientList;

    //Mapping
    public static ComandeClientDto fromEntity(CommandeClient commandeClient){
        if(commandeClient == null){
            return null;
        }
        List<LigneComandeClient> lignes = commandeClient.getLigneComandeClientList();
        BigDecimal montantTotal = lignes == null ? null : lignes.stream()
                .map(l -> (l.getPrixUnitaire() == null || l.getQuantite() == null)
                        ? BigDecimal.ZERO
                        : l.getPrixUnitaire().multiply(l.getQuantite()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return ComandeClientDto.builder()
                .id(commandeClient.getId())
                .code(commandeClient.getCode())
                .dateComande(commandeClient.getDateComande())
                .etatCommande(commandeClient.getEtatCommande())
                .client(ClientDto.fromEntity(commandeClient.getClient()))
                .nomVendeur(commandeClient.getVendeur() != null
                        ? String.format("%s %s",
                                commandeClient.getVendeur().getNom() != null ? commandeClient.getVendeur().getNom() : "",
                                commandeClient.getVendeur().getPrenom() != null ? commandeClient.getVendeur().getPrenom() : "").trim()
                        : null)
                .dateLivraison(commandeClient.getDateLivraison())
                .montantTotal(montantTotal)
                .idEntreprise(commandeClient.getIdEntreprise())
                .build();
    }

    public static CommandeClient toEntity(ComandeClientDto comandeClientDto){
        if(comandeClientDto == null){
            return null;
        }
        CommandeClient commandeClient = new CommandeClient();
        commandeClient.setId(comandeClientDto.getId());
        commandeClient.setCode(comandeClientDto.getCode());
        commandeClient.setDateComande(comandeClientDto.getDateComande());
        commandeClient.setClient(ClientDto.toEntity(comandeClientDto.getClient()));
        commandeClient.setEtatCommande(comandeClientDto.getEtatCommande());
        commandeClient.setIdEntreprise(comandeClientDto.getIdEntreprise());
        // vendeur et dateLivraison sont gérés côté serveur, jamais par le client
        return commandeClient;
    }

    public boolean isCommandeLivree() {
        return EtatCommande.LIVREE.equals(this.etatCommande);
    }

}
