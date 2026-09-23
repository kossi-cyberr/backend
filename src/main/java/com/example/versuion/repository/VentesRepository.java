package com.example.versuion.repository;

import com.example.versuion.models.Ventes;
import com.example.versuion.utiles.CurrentEntreprise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VentesRepository extends JpaRepository<Ventes, Long> {

    Optional<Ventes> findByCode(String code);

    // --- Méthodes multi-entreprise (filtrage par idEntreprise) ---
    Optional<Ventes> findByIdAndIdEntreprise(Long id, Integer idEntreprise);

    Optional<Ventes> findByCodeAndIdEntreprise(String code, Integer idEntreprise);

    List<Ventes> findAllByIdEntreprise(Integer idEntreprise);

    Page<Ventes> findAllByIdEntreprise(Integer idEntreprise, Pageable pageable);

    Page<Ventes> findAllByIdEntrepriseAndCodeContainingIgnoreCase(Integer idEntreprise, String search, Pageable pageable);

    Page<Ventes> findAllByIdEntrepriseAndVendeurId(Integer idEntreprise, Long vendeurId, Pageable pageable);

    long countByIdEntreprise(Integer idEntreprise);

    default Optional<Ventes> findByIdTenant(Long id) {
        Integer idEntreprise = CurrentEntreprise.getId();
        return idEntreprise != null ? findByIdAndIdEntreprise(id, idEntreprise) : findById(id);
    }

    default Optional<Ventes> findByCodeTenant(String code) {
        Integer idEntreprise = CurrentEntreprise.getId();
        return idEntreprise != null ? findByCodeAndIdEntreprise(code, idEntreprise) : findByCode(code);
    }

    default List<Ventes> findAllTenant() {
        Integer idEntreprise = CurrentEntreprise.getId();
        return idEntreprise != null ? findAllByIdEntreprise(idEntreprise) : findAll();
    }

    default Page<Ventes> findAllTenant(Pageable pageable) {
        Integer idEntreprise = CurrentEntreprise.getId();
        return idEntreprise != null ? findAllByIdEntreprise(idEntreprise, pageable) : findAll(pageable);
    }

    default Page<Ventes> findAllTenant(String search, Pageable pageable) {
        Integer idEntreprise = CurrentEntreprise.getId();
        if (idEntreprise != null) {
            return findAllByIdEntrepriseAndCodeContainingIgnoreCase(idEntreprise, search, pageable);
        }
        return findAllByCodeContainingIgnoreCase(search, pageable);
    }

    /**
     * Ventes de l'entreprise courante enregistrées par le vendeur donné.
     * Sans entreprise courante (hors contexte JWT), retombe sur un filtrage par vendeur seul.
     */
    default Page<Ventes> findAllByVendeurTenant(Long vendeurId, Pageable pageable) {
        Integer idEntreprise = CurrentEntreprise.getId();
        return idEntreprise != null
                ? findAllByIdEntrepriseAndVendeurId(idEntreprise, vendeurId, pageable)
                : findAllByVendeurId(vendeurId, pageable);
    }

    Page<Ventes> findAllByVendeurId(Long vendeurId, Pageable pageable);

    Page<Ventes> findAllByCodeContainingIgnoreCase(String search, Pageable pageable);
}
