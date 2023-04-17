package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.UstozRepository;
import com.mycompany.myapp.service.UstozService;
import com.mycompany.myapp.service.dto.UstozDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Ustoz}.
 */
@RestController
@RequestMapping("/api")
public class UstozResource {

    private final Logger log = LoggerFactory.getLogger(UstozResource.class);

    private static final String ENTITY_NAME = "ustoz";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UstozService ustozService;

    private final UstozRepository ustozRepository;

    public UstozResource(UstozService ustozService, UstozRepository ustozRepository) {
        this.ustozService = ustozService;
        this.ustozRepository = ustozRepository;
    }

    /**
     * {@code POST  /ustozs} : Create a new ustoz.
     *
     * @param ustozDTO the ustozDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ustozDTO, or with status {@code 400 (Bad Request)} if the ustoz has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ustozs")
    public ResponseEntity<UstozDTO> createUstoz(@RequestBody UstozDTO ustozDTO) throws URISyntaxException {
        log.debug("REST request to save Ustoz : {}", ustozDTO);
        if (ustozDTO.getId() != null) {
            throw new BadRequestAlertException("A new ustoz cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UstozDTO result = ustozService.save(ustozDTO);
        return ResponseEntity
            .created(new URI("/api/ustozs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ustozs/:id} : Updates an existing ustoz.
     *
     * @param id the id of the ustozDTO to save.
     * @param ustozDTO the ustozDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ustozDTO,
     * or with status {@code 400 (Bad Request)} if the ustozDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ustozDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ustozs/{id}")
    public ResponseEntity<UstozDTO> updateUstoz(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UstozDTO ustozDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Ustoz : {}, {}", id, ustozDTO);
        if (ustozDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ustozDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ustozRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UstozDTO result = ustozService.update(ustozDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ustozDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ustozs/:id} : Partial updates given fields of an existing ustoz, field will ignore if it is null
     *
     * @param id the id of the ustozDTO to save.
     * @param ustozDTO the ustozDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ustozDTO,
     * or with status {@code 400 (Bad Request)} if the ustozDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ustozDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ustozDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ustozs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UstozDTO> partialUpdateUstoz(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UstozDTO ustozDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ustoz partially : {}, {}", id, ustozDTO);
        if (ustozDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ustozDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ustozRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UstozDTO> result = ustozService.partialUpdate(ustozDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ustozDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ustozs} : get all the ustozs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ustozs in body.
     */
    @GetMapping("/ustozs")
    public List<UstozDTO> getAllUstozs() {
        log.debug("REST request to get all Ustozs");
        return ustozService.findAll();
    }

    /**
     * {@code GET  /ustozs/:id} : get the "id" ustoz.
     *
     * @param id the id of the ustozDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ustozDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ustozs/{id}")
    public ResponseEntity<UstozDTO> getUstoz(@PathVariable Long id) {
        log.debug("REST request to get Ustoz : {}", id);
        Optional<UstozDTO> ustozDTO = ustozService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ustozDTO);
    }

    /**
     * {@code DELETE  /ustozs/:id} : delete the "id" ustoz.
     *
     * @param id the id of the ustozDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ustozs/{id}")
    public ResponseEntity<Void> deleteUstoz(@PathVariable Long id) {
        log.debug("REST request to delete Ustoz : {}", id);
        ustozService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
