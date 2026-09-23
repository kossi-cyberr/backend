package com.example.versuion.services;

import com.example.versuion.Dto.PageResponse;
import com.example.versuion.Dto.VentesDto;

import java.util.List;

public interface VentesService {

    VentesDto save(VentesDto dto);

    VentesDto findById(Long id);

    VentesDto findByCode(String code);

    List<VentesDto> findAll();

    PageResponse<VentesDto> findAllPaginated(int page, int size, String sortBy, String sortDir, String search);

    /** Historique paginé des ventes enregistrées par l'utilisateur connecté. */
    PageResponse<VentesDto> findMesVentes(int page, int size);

    void delete(Long id);
}
