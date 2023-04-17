package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Javob;
import com.mycompany.myapp.repository.JavobRepository;
import com.mycompany.myapp.service.dto.JavobDTO;
import com.mycompany.myapp.service.mapper.JavobMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Javob}.
 */
@Service
@Transactional
public class JavobService {

    private final Logger log = LoggerFactory.getLogger(JavobService.class);

    private final JavobRepository javobRepository;

    private final JavobMapper javobMapper;

    public JavobService(JavobRepository javobRepository, JavobMapper javobMapper) {
        this.javobRepository = javobRepository;
        this.javobMapper = javobMapper;
    }

    /**
     * Save a javob.
     *
     * @param javobDTO the entity to save.
     * @return the persisted entity.
     */
    public JavobDTO save(JavobDTO javobDTO) {
        log.debug("Request to save Javob : {}", javobDTO);
        Javob javob = javobMapper.toEntity(javobDTO);
        javob = javobRepository.save(javob);
        return javobMapper.toDto(javob);
    }

    /**
     * Update a javob.
     *
     * @param javobDTO the entity to save.
     * @return the persisted entity.
     */
    public JavobDTO update(JavobDTO javobDTO) {
        log.debug("Request to update Javob : {}", javobDTO);
        Javob javob = javobMapper.toEntity(javobDTO);
        javob = javobRepository.save(javob);
        return javobMapper.toDto(javob);
    }

    /**
     * Partially update a javob.
     *
     * @param javobDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<JavobDTO> partialUpdate(JavobDTO javobDTO) {
        log.debug("Request to partially update Javob : {}", javobDTO);

        return javobRepository
            .findById(javobDTO.getId())
            .map(existingJavob -> {
                javobMapper.partialUpdate(existingJavob, javobDTO);

                return existingJavob;
            })
            .map(javobRepository::save)
            .map(javobMapper::toDto);
    }

    /**
     * Get all the javobs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<JavobDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Javobs");
        return javobRepository.findAll(pageable).map(javobMapper::toDto);
    }

    /**
     * Get all the javobs with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<JavobDTO> findAllWithEagerRelationships(Pageable pageable) {
        return javobRepository.findAllWithEagerRelationships(pageable).map(javobMapper::toDto);
    }

    /**
     * Get one javob by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<JavobDTO> findOne(Long id) {
        log.debug("Request to get Javob : {}", id);
        return javobRepository.findOneWithEagerRelationships(id).map(javobMapper::toDto);
    }

    /**
     * Delete the javob by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Javob : {}", id);
        javobRepository.deleteById(id);
    }
}
