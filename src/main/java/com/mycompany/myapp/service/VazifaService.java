package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Vazifa;
import com.mycompany.myapp.repository.VazifaRepository;
import com.mycompany.myapp.service.dto.VazifaDTO;
import com.mycompany.myapp.service.mapper.VazifaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Vazifa}.
 */
@Service
@Transactional
public class VazifaService {

    private final Logger log = LoggerFactory.getLogger(VazifaService.class);

    private final VazifaRepository vazifaRepository;

    private final VazifaMapper vazifaMapper;

    public VazifaService(VazifaRepository vazifaRepository, VazifaMapper vazifaMapper) {
        this.vazifaRepository = vazifaRepository;
        this.vazifaMapper = vazifaMapper;
    }

    /**
     * Save a vazifa.
     *
     * @param vazifaDTO the entity to save.
     * @return the persisted entity.
     */
    public VazifaDTO save(VazifaDTO vazifaDTO) {
        log.debug("Request to save Vazifa : {}", vazifaDTO);
        Vazifa vazifa = vazifaMapper.toEntity(vazifaDTO);
        vazifa = vazifaRepository.save(vazifa);
        return vazifaMapper.toDto(vazifa);
    }

    /**
     * Update a vazifa.
     *
     * @param vazifaDTO the entity to save.
     * @return the persisted entity.
     */
    public VazifaDTO update(VazifaDTO vazifaDTO) {
        log.debug("Request to update Vazifa : {}", vazifaDTO);
        Vazifa vazifa = vazifaMapper.toEntity(vazifaDTO);
        vazifa = vazifaRepository.save(vazifa);
        return vazifaMapper.toDto(vazifa);
    }

    /**
     * Partially update a vazifa.
     *
     * @param vazifaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VazifaDTO> partialUpdate(VazifaDTO vazifaDTO) {
        log.debug("Request to partially update Vazifa : {}", vazifaDTO);

        return vazifaRepository
            .findById(vazifaDTO.getId())
            .map(existingVazifa -> {
                vazifaMapper.partialUpdate(existingVazifa, vazifaDTO);

                return existingVazifa;
            })
            .map(vazifaRepository::save)
            .map(vazifaMapper::toDto);
    }

    /**
     * Get all the vazifas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<VazifaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Vazifas");
        return vazifaRepository.findAll(pageable).map(vazifaMapper::toDto);
    }

    /**
     * Get all the vazifas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<VazifaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return vazifaRepository.findAllWithEagerRelationships(pageable).map(vazifaMapper::toDto);
    }

    /**
     * Get one vazifa by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VazifaDTO> findOne(Long id) {
        log.debug("Request to get Vazifa : {}", id);
        return vazifaRepository.findOneWithEagerRelationships(id).map(vazifaMapper::toDto);
    }

    /**
     * Delete the vazifa by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Vazifa : {}", id);
        vazifaRepository.deleteById(id);
    }
}
