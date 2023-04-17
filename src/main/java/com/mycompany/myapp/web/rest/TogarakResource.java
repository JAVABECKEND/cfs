package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.TogarakRepository;
import com.mycompany.myapp.service.TogarakService;
import com.mycompany.myapp.service.dto.TogarakDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Togarak}.
 */
@RestController
@RequestMapping("/api")
public class TogarakResource {

    private final Logger log = LoggerFactory.getLogger(TogarakResource.class);

    private static final String ENTITY_NAME = "togarak";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TogarakService togarakService;

    private final TogarakRepository togarakRepository;

    public TogarakResource(TogarakService togarakService, TogarakRepository togarakRepository) {
        this.togarakService = togarakService;
        this.togarakRepository = togarakRepository;
    }

    /**
     * {@code POST  /togaraks} : Create a new togarak.
     *
     * @param togarakDTO the togarakDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new togarakDTO, or with status {@code 400 (Bad Request)} if the togarak has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/togaraks")
    public ResponseEntity<TogarakDTO> createTogarak(@RequestBody TogarakDTO togarakDTO) throws URISyntaxException {
        log.debug("REST request to save Togarak : {}", togarakDTO);
        if (togarakDTO.getId() != null) {
            throw new BadRequestAlertException("A new togarak cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TogarakDTO result = togarakService.save(togarakDTO);
        return ResponseEntity
            .created(new URI("/api/togaraks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /togaraks/:id} : Updates an existing togarak.
     *
     * @param id the id of the togarakDTO to save.
     * @param togarakDTO the togarakDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated togarakDTO,
     * or with status {@code 400 (Bad Request)} if the togarakDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the togarakDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/togaraks/{id}")
    public ResponseEntity<TogarakDTO> updateTogarak(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TogarakDTO togarakDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Togarak : {}, {}", id, togarakDTO);
        if (togarakDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, togarakDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!togarakRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TogarakDTO result = togarakService.update(togarakDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, togarakDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /togaraks/:id} : Partial updates given fields of an existing togarak, field will ignore if it is null
     *
     * @param id the id of the togarakDTO to save.
     * @param togarakDTO the togarakDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated togarakDTO,
     * or with status {@code 400 (Bad Request)} if the togarakDTO is not valid,
     * or with status {@code 404 (Not Found)} if the togarakDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the togarakDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/togaraks/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TogarakDTO> partialUpdateTogarak(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TogarakDTO togarakDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Togarak partially : {}, {}", id, togarakDTO);
        if (togarakDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, togarakDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!togarakRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TogarakDTO> result = togarakService.partialUpdate(togarakDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, togarakDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /togaraks} : get all the togaraks.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of togaraks in body.
     */
    @GetMapping("/togaraks")
    public List<TogarakDTO> getAllTogaraks(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Togaraks");
        return togarakService.findAll();
    }

    /**
     * {@code GET  /togaraks/:id} : get the "id" togarak.
     *
     * @param id the id of the togarakDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the togarakDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/togaraks/{id}")
    public ResponseEntity<TogarakDTO> getTogarak(@PathVariable Long id) {
        log.debug("REST request to get Togarak : {}", id);
        Optional<TogarakDTO> togarakDTO = togarakService.findOne(id);
        return ResponseUtil.wrapOrNotFound(togarakDTO);
    }

    /**
     * {@code DELETE  /togaraks/:id} : delete the "id" togarak.
     *
     * @param id the id of the togarakDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/togaraks/{id}")
    public ResponseEntity<Void> deleteTogarak(@PathVariable Long id) {
        log.debug("REST request to delete Togarak : {}", id);
        togarakService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
