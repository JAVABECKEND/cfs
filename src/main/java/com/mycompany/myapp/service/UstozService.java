package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Ustoz;
import com.mycompany.myapp.repository.UstozRepository;
import com.mycompany.myapp.service.dto.UstozDTO;
import com.mycompany.myapp.service.mapper.UstozMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Ustoz}.
 */
@Service
@Transactional
public class UstozService {

    private final Logger log = LoggerFactory.getLogger(UstozService.class);

    private final UstozRepository ustozRepository;

    private final UstozMapper ustozMapper;

    public UstozService(UstozRepository ustozRepository, UstozMapper ustozMapper) {
        this.ustozRepository = ustozRepository;
        this.ustozMapper = ustozMapper;
    }

    /**
     * Save a ustoz.
     *
     * @param ustozDTO the entity to save.
     * @return the persisted entity.
     */
    public UstozDTO save(UstozDTO ustozDTO) {
        log.debug("Request to save Ustoz : {}", ustozDTO);
        Ustoz ustoz = ustozMapper.toEntity(ustozDTO);
        ustoz = ustozRepository.save(ustoz);
        return ustozMapper.toDto(ustoz);
    }

    /**
     * Update a ustoz.
     *
     * @param ustozDTO the entity to save.
     * @return the persisted entity.
     */
    public UstozDTO update(UstozDTO ustozDTO) {
        log.debug("Request to update Ustoz : {}", ustozDTO);
        Ustoz ustoz = ustozMapper.toEntity(ustozDTO);
        ustoz = ustozRepository.save(ustoz);
        return ustozMapper.toDto(ustoz);
    }

    /**
     * Partially update a ustoz.
     *
     * @param ustozDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UstozDTO> partialUpdate(UstozDTO ustozDTO) {
        log.debug("Request to partially update Ustoz : {}", ustozDTO);

        return ustozRepository
            .findById(ustozDTO.getId())
            .map(existingUstoz -> {
                ustozMapper.partialUpdate(existingUstoz, ustozDTO);

                return existingUstoz;
            })
            .map(ustozRepository::save)
            .map(ustozMapper::toDto);
    }

    /**
     * Get all the ustozs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UstozDTO> findAll() {
        log.debug("Request to get all Ustozs");
        return ustozRepository.findAll().stream().map(ustozMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one ustoz by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UstozDTO> findOne(Long id) {
        log.debug("Request to get Ustoz : {}", id);
        return ustozRepository.findById(id).map(ustozMapper::toDto);
    }

    /**
     * Delete the ustoz by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Ustoz : {}", id);
        ustozRepository.deleteById(id);
    }
}
