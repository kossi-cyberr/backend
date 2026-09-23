package com.example.versuion.repository;

import com.example.versuion.models.ComandeFournisseur;
import com.example.versuion.utiles.CurrentEntreprise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommandeFournisseurRepository extends JpaRepository<ComandeFournisseur, Long> {

    Optional<ComandeFournisseur> findByCode(String code);

    @Query("select c from ComandeFournisseur c where c.Fournisseur.id = :id")
    List<ComandeFournisseur> findAllByFournisseurId(@Param("id") Long id);

    // --- Méthodes multi-entreprise (filtrage par idEntreprise) ---
    Optional<ComandeFournisseur> findByIdAndIdEntreprise(Long id, Integer idEntreprise);

    Optional<ComandeFournisseur> findByCodeAndIdEntreprise(String code, Integer idEntreprise);

    List<ComandeFournisseur> findAllByIdEntreprise(Integer idEntreprise);

    long countByIdEntreprise(Integer idEntreprise);

    @Query("select c from ComandeFournisseur c where c.Fournisseur.id = :id and c.idEntreprise = :idEntreprise")
    List<ComandeFournisseur> findAllByFournisseurIdAndIdEntreprise(@Param("id") Long id, @Param("idEntreprise") Integer idEntreprise);

    default Optional<ComandeFournisseur> findByIdTenant(Long id) {
        Integer idEntreprise = CurrentEntreprise.getId();
        return idEntreprise != null ? findByIdAndIdEntreprise(id, idEntreprise) : findById(id);
    }

    default Optional<ComandeFournisseur> findByCodeTenant(String code) {
        Integer idEntreprise = CurrentEntreprise.getId();
        return idEntreprise != null ? findByCodeAndIdEntreprise(code, idEntreprise) : findByCode(code);
    }

    default List<ComandeFournisseur> findAllTenant() {
        Integer idEntreprise = CurrentEntreprise.getId();
        return idEntreprise != null ? findAllByIdEntreprise(idEntreprise) : findAll();
    }

    default List<ComandeFournisseur> findAllByFournisseurIdTenant(Long id) {
        Integer idEntreprise = CurrentEntreprise.getId();
        return idEntreprise != null ? findAllByFournisseurIdAndIdEntreprise(id, idEntreprise) : findAllByFournisseurId(id);
    }

    default Page<ComandeFournisseur> findAllTenant(Pageable pageable) {
        Integer idEntreprise = CurrentEntreprise.getId();
        return idEntreprise != null ? findAllByIdEntreprise(idEntreprise, pageable) : findAll(pageable);
    }

    default Page<ComandeFournisseur> findAllTenant(String search, Pageable pageable) {
        Integer idEntreprise = CurrentEntreprise.getId();
        if (idEntreprise != null) {
            return findAllByIdEntrepriseAndCodeContainingIgnoreCase(idEntreprise, search, pageable);
        }
        return findAllByCodeContainingIgnoreCase(search, pageable);
    }

    Page<ComandeFournisseur> findAllByIdEntreprise(Integer idEntreprise, Pageable pageable);

    Page<ComandeFournisseur> findAllByIdEntrepriseAndCodeContainingIgnoreCase(Integer idEntreprise, String search, Pageable pageable);

    Page<ComandeFournisseur> findAllByCodeContainingIgnoreCase(String search, Pageable pageable);
}
