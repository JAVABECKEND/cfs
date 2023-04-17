package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.FanRepository;
import com.mycompany.myapp.service.FanService;
import com.mycompany.myapp.service.dto.FanDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Fan}.
 */
@RestController
@RequestMapping("/api")
public class FanResource {

    private final Logger log = LoggerFactory.getLogger(FanResource.class);

    private static final String ENTITY_NAME = "fan";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FanService fanService;

    private final FanRepository fanRepository;

    public FanResource(FanService fanService, FanRepository fanRepository) {
        this.fanService = fanService;
        this.fanRepository = fanRepository;
    }

    /**
     * {@code POST  /fans} : Create a new fan.
     *
     * @param fanDTO the fanDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fanDTO, or with status {@code 400 (Bad Request)} if the fan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fans")
    public ResponseEntity<FanDTO> createFan(@RequestBody FanDTO fanDTO) throws URISyntaxException {
        log.debug("REST request to save Fan : {}", fanDTO);
        if (fanDTO.getId() != null) {
            throw new BadRequestAlertException("A new fan cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FanDTO result = fanService.save(fanDTO);
        return ResponseEntity
            .created(new URI("/api/fans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fans/:id} : Updates an existing fan.
     *
     * @param id the id of the fanDTO to save.
     * @param fanDTO the fanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fanDTO,
     * or with status {@code 400 (Bad Request)} if the fanDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fans/{id}")
    public ResponseEntity<FanDTO> updateFan(@PathVariable(value = "id", required = false) final Long id, @RequestBody FanDTO fanDTO)
        throws URISyntaxException {
        log.debug("REST request to update Fan : {}, {}", id, fanDTO);
        if (fanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fanDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FanDTO result = fanService.update(fanDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fanDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fans/:id} : Partial updates given fields of an existing fan, field will ignore if it is null
     *
     * @param id the id of the fanDTO to save.
     * @param fanDTO the fanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fanDTO,
     * or with status {@code 400 (Bad Request)} if the fanDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fanDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fans/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FanDTO> partialUpdateFan(@PathVariable(value = "id", required = false) final Long id, @RequestBody FanDTO fanDTO)
        throws URISyntaxException {
        log.debug("REST request to partial update Fan partially : {}, {}", id, fanDTO);
        if (fanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fanDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FanDTO> result = fanService.partialUpdate(fanDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fanDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /fans} : get all the fans.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fans in body.
     */
    @GetMapping("/fans")
    public List<FanDTO> getAllFans() {
        log.debug("REST request to get all Fans");
        return fanService.findAll();
    }

    /**
     * {@code GET  /fans/:id} : get the "id" fan.
     *
     * @param id the id of the fanDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fanDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fans/{id}")
    public ResponseEntity<FanDTO> getFan(@PathVariable Long id) {
        log.debug("REST request to get Fan : {}", id);
        Optional<FanDTO> fanDTO = fanService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fanDTO);
    }

    /**
     * {@code DELETE  /fans/:id} : delete the "id" fan.
     *
     * @param id the id of the fanDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fans/{id}")
    public ResponseEntity<Void> deleteFan(@PathVariable Long id) {
        log.debug("REST request to delete Fan : {}", id);
        fanService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
