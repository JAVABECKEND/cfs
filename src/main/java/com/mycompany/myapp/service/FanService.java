package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Fan;
import com.mycompany.myapp.repository.FanRepository;
import com.mycompany.myapp.service.dto.FanDTO;
import com.mycompany.myapp.service.mapper.FanMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Fan}.
 */
@Service
@Transactional
public class FanService {

    private final Logger log = LoggerFactory.getLogger(FanService.class);

    private final FanRepository fanRepository;

    private final FanMapper fanMapper;

    public FanService(FanRepository fanRepository, FanMapper fanMapper) {
        this.fanRepository = fanRepository;
        this.fanMapper = fanMapper;
    }

    /**
     * Save a fan.
     *
     * @param fanDTO the entity to save.
     * @return the persisted entity.
     */
    public FanDTO save(FanDTO fanDTO) {
        log.debug("Request to save Fan : {}", fanDTO);
        Fan fan = fanMapper.toEntity(fanDTO);
        fan = fanRepository.save(fan);
        return fanMapper.toDto(fan);
    }

    /**
     * Update a fan.
     *
     * @param fanDTO the entity to save.
     * @return the persisted entity.
     */
    public FanDTO update(FanDTO fanDTO) {
        log.debug("Request to update Fan : {}", fanDTO);
        Fan fan = fanMapper.toEntity(fanDTO);
        fan = fanRepository.save(fan);
        return fanMapper.toDto(fan);
    }

    /**
     * Partially update a fan.
     *
     * @param fanDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FanDTO> partialUpdate(FanDTO fanDTO) {
        log.debug("Request to partially update Fan : {}", fanDTO);

        return fanRepository
            .findById(fanDTO.getId())
            .map(existingFan -> {
                fanMapper.partialUpdate(existingFan, fanDTO);

                return existingFan;
            })
            .map(fanRepository::save)
            .map(fanMapper::toDto);
    }

    /**
     * Get all the fans.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<FanDTO> findAll() {
        log.debug("Request to get all Fans");
        return fanRepository.findAll().stream().map(fanMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one fan by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FanDTO> findOne(Long id) {
        log.debug("Request to get Fan : {}", id);
        return fanRepository.findById(id).map(fanMapper::toDto);
    }

    /**
     * Delete the fan by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Fan : {}", id);
        fanRepository.deleteById(id);
    }
}
