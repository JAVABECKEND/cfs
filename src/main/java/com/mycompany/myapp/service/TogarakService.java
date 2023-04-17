package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Togarak;
import com.mycompany.myapp.repository.TogarakRepository;
import com.mycompany.myapp.service.dto.TogarakDTO;
import com.mycompany.myapp.service.mapper.TogarakMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Togarak}.
 */
@Service
@Transactional
public class TogarakService {

    private final Logger log = LoggerFactory.getLogger(TogarakService.class);

    private final TogarakRepository togarakRepository;

    private final TogarakMapper togarakMapper;

    public TogarakService(TogarakRepository togarakRepository, TogarakMapper togarakMapper) {
        this.togarakRepository = togarakRepository;
        this.togarakMapper = togarakMapper;
    }

    /**
     * Save a togarak.
     *
     * @param togarakDTO the entity to save.
     * @return the persisted entity.
     */
    public TogarakDTO save(TogarakDTO togarakDTO) {
        log.debug("Request to save Togarak : {}", togarakDTO);
        Togarak togarak = togarakMapper.toEntity(togarakDTO);
        togarak = togarakRepository.save(togarak);
        return togarakMapper.toDto(togarak);
    }

    /**
     * Update a togarak.
     *
     * @param togarakDTO the entity to save.
     * @return the persisted entity.
     */
    public TogarakDTO update(TogarakDTO togarakDTO) {
        log.debug("Request to update Togarak : {}", togarakDTO);
        Togarak togarak = togarakMapper.toEntity(togarakDTO);
        togarak = togarakRepository.save(togarak);
        return togarakMapper.toDto(togarak);
    }

    /**
     * Partially update a togarak.
     *
     * @param togarakDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TogarakDTO> partialUpdate(TogarakDTO togarakDTO) {
        log.debug("Request to partially update Togarak : {}", togarakDTO);

        return togarakRepository
            .findById(togarakDTO.getId())
            .map(existingTogarak -> {
                togarakMapper.partialUpdate(existingTogarak, togarakDTO);

                return existingTogarak;
            })
            .map(togarakRepository::save)
            .map(togarakMapper::toDto);
    }

    /**
     * Get all the togaraks.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TogarakDTO> findAll() {
        log.debug("Request to get all Togaraks");
        return togarakRepository.findAll().stream().map(togarakMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the togaraks with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<TogarakDTO> findAllWithEagerRelationships(Pageable pageable) {
        return togarakRepository.findAllWithEagerRelationships(pageable).map(togarakMapper::toDto);
    }

    /**
     * Get one togarak by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TogarakDTO> findOne(Long id) {
        log.debug("Request to get Togarak : {}", id);
        return togarakRepository.findOneWithEagerRelationships(id).map(togarakMapper::toDto);
    }

    /**
     * Delete the togarak by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Togarak : {}", id);
        togarakRepository.deleteById(id);
    }
}
