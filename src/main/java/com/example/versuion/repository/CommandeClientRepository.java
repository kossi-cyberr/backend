package com.example.versuion.repository;

import com.example.versuion.Dto.dashboard.CommandesParClientDto;
import com.example.versuion.models.CommandeClient;
import com.example.versuion.models.EtatCommande;
import com.example.versuion.utiles.CurrentEntreprise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommandeClientRepository extends JpaRepository<CommandeClient, Long> {

    Optional<CommandeClient> findCommandeClientByCode(String code);

    List<CommandeClient> findAllByClientId(Long id);

    // --- Méthodes multi-entreprise (filtrage par idEntreprise) ---
    Optional<CommandeClient> findByIdAndIdEntreprise(Long id, Integer idEntreprise);

    Optional<CommandeClient> findCommandeClientByCodeAndIdEntreprise(String code, Integer idEntreprise);

    List<CommandeClient> findAllByIdEntreprise(Integer idEntreprise);

    List<CommandeClient> findAllByClientIdAndIdEntreprise(Long id, Integer idEntreprise);

    @Query("select new com.example.versuion.Dto.dashboard.CommandesParClientDto(" +
            "cl.id, cl.nom, cl.prenom, count(c), coalesce(sum(l.prixUnitaire * l.quantite), 0)) " +
            "from LigneComandeClient l join l.commandeClient c join c.client cl " +
            "where l.idEntreprise = :idEntreprise " +
            "group by cl.id, cl.nom, cl.prenom " +
            "order by coalesce(sum(l.prixUnitaire * l.quantite), 0) desc")
    List<CommandesParClientDto> commandesParClient(@Param("idEntreprise") Integer idEntreprise);

    Page<CommandeClient> findAllByIdEntreprise(Integer idEntreprise, Pageable pageable);

    Page<CommandeClient> findAllByIdEntrepriseAndCodeContainingIgnoreCase(Integer idEntreprise, String search, Pageable pageable);

    // --- Filtre par état (liste des commandes) ---
    Page<CommandeClient> findAllByIdEntrepriseAndEtatCommande(Integer idEntreprise, EtatCommande etatCommande, Pageable pageable);

    Page<CommandeClient> findAllByIdEntrepriseAndCodeContainingIgnoreCaseAndEtatCommande(Integer idEntreprise, String search, EtatCommande etatCommande, Pageable pageable);

    Page<CommandeClient> findAllByEtatCommande(EtatCommande etatCommande, Pageable pageable);

    Page<CommandeClient> findAllByCodeContainingIgnoreCaseAndEtatCommande(String search, EtatCommande etatCommande, Pageable pageable);

    long countByIdEntreprise(Integer idEntreprise);

    default Optional<CommandeClient> findByIdTenant(Long id) {
        Integer idEntreprise = CurrentEntreprise.getId();
        return idEntreprise != null ? findByIdAndIdEntreprise(id, idEntreprise) : findById(id);
    }

    default Optional<CommandeClient> findByCodeTenant(String code) {
        Integer idEntreprise = CurrentEntreprise.getId();
        return idEntreprise != null ? findCommandeClientByCodeAndIdEntreprise(code, idEntreprise) : findCommandeClientByCode(code);
    }

    default List<CommandeClient> findAllTenant() {
        Integer idEntreprise = CurrentEntreprise.getId();
        return idEntreprise != null ? findAllByIdEntreprise(idEntreprise) : findAll();
    }

    default List<CommandeClient> findAllByClientIdTenant(Long id) {
        Integer idEntreprise = CurrentEntreprise.getId();
        return idEntreprise != null ? findAllByClientIdAndIdEntreprise(id, idEntreprise) : findAllByClientId(id);
    }

    default Page<CommandeClient> findAllTenant(Pageable pageable) {
        Integer idEntreprise = CurrentEntreprise.getId();
        return idEntreprise != null ? findAllByIdEntreprise(idEntreprise, pageable) : findAll(pageable);
    }

    default Page<CommandeClient> findAllTenant(EtatCommande etatCommande, Pageable pageable) {
        Integer idEntreprise = CurrentEntreprise.getId();
        if (idEntreprise != null) {
            return findAllByIdEntrepriseAndEtatCommande(idEntreprise, etatCommande, pageable);
        }
        return findAllByEtatCommande(etatCommande, pageable);
    }

    default Page<CommandeClient> findAllTenant(String search, EtatCommande etatCommande, Pageable pageable) {
        Integer idEntreprise = CurrentEntreprise.getId();
        if (idEntreprise != null) {
            return findAllByIdEntrepriseAndCodeContainingIgnoreCaseAndEtatCommande(idEntreprise, search, etatCommande, pageable);
        }
        return findAllByCodeContainingIgnoreCaseAndEtatCommande(search, etatCommande, pageable);
    }

    /**
     * Recherche multi-critères à paramètres optionnels : entreprise courante,
     * code (contient), état, vendeur — tout combinable, null = critère ignoré.
     */
    @Query("select c from CommandeClient c where " +
            "(:idEntreprise is null or c.idEntreprise = :idEntreprise) and " +
            "(:search is null or cast(:search as string) is null or lower(c.code) like lower(concat('%', cast(:search as string), '%'))) and " +
            "(:etat is null or c.etatCommande = :etat) and " +
            "(:vendeurId is null or c.vendeur.id = :vendeurId)")
    Page<CommandeClient> rechercherMultiCriteres(@Param("idEntreprise") Integer idEntreprise,
                                                 @Param("search") String search,
                                                 @Param("etat") EtatCommande etat,
                                                 @Param("vendeurId") Long vendeurId,
                                                 Pageable pageable);

    default Page<CommandeClient> findAllTenant(String search, EtatCommande etat, Long vendeurId, Pageable pageable) {
        return rechercherMultiCriteres(CurrentEntreprise.getId(), search, etat, vendeurId, pageable);
    }

    default Page<CommandeClient> findAllTenant(String search, Pageable pageable) {
        Integer idEntreprise = CurrentEntreprise.getId();
        if (idEntreprise != null) {
            return findAllByIdEntrepriseAndCodeContainingIgnoreCase(idEntreprise, search, pageable);
        }
        return findAllByCodeContainingIgnoreCase(search, pageable);
    }

    Page<CommandeClient> findAllByCodeContainingIgnoreCase(String search, Pageable pageable);
}
