package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.JavobRepository;
import com.mycompany.myapp.service.JavobService;
import com.mycompany.myapp.service.dto.JavobDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Javob}.
 */
@RestController
@RequestMapping("/api")
public class JavobResource {

    private final Logger log = LoggerFactory.getLogger(JavobResource.class);

    private static final String ENTITY_NAME = "javob";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JavobService javobService;

    private final JavobRepository javobRepository;

    public JavobResource(JavobService javobService, JavobRepository javobRepository) {
        this.javobService = javobService;
        this.javobRepository = javobRepository;
    }

    /**
     * {@code POST  /javobs} : Create a new javob.
     *
     * @param javobDTO the javobDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new javobDTO, or with status {@code 400 (Bad Request)} if the javob has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/javobs")
    public ResponseEntity<JavobDTO> createJavob(@RequestBody JavobDTO javobDTO) throws URISyntaxException {
        log.debug("REST request to save Javob : {}", javobDTO);
        if (javobDTO.getId() != null) {
            throw new BadRequestAlertException("A new javob cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JavobDTO result = javobService.save(javobDTO);
        return ResponseEntity
            .created(new URI("/api/javobs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /javobs/:id} : Updates an existing javob.
     *
     * @param id the id of the javobDTO to save.
     * @param javobDTO the javobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated javobDTO,
     * or with status {@code 400 (Bad Request)} if the javobDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the javobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/javobs/{id}")
    public ResponseEntity<JavobDTO> updateJavob(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody JavobDTO javobDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Javob : {}, {}", id, javobDTO);
        if (javobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, javobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!javobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        JavobDTO result = javobService.update(javobDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, javobDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /javobs/:id} : Partial updates given fields of an existing javob, field will ignore if it is null
     *
     * @param id the id of the javobDTO to save.
     * @param javobDTO the javobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated javobDTO,
     * or with status {@code 400 (Bad Request)} if the javobDTO is not valid,
     * or with status {@code 404 (Not Found)} if the javobDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the javobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/javobs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<JavobDTO> partialUpdateJavob(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody JavobDTO javobDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Javob partially : {}, {}", id, javobDTO);
        if (javobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, javobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!javobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<JavobDTO> result = javobService.partialUpdate(javobDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, javobDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /javobs} : get all the javobs.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of javobs in body.
     */
    @GetMapping("/javobs")
    public ResponseEntity<List<JavobDTO>> getAllJavobs(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Javobs");
        Page<JavobDTO> page;
        if (eagerload) {
            page = javobService.findAllWithEagerRelationships(pageable);
        } else {
            page = javobService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /javobs/:id} : get the "id" javob.
     *
     * @param id the id of the javobDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the javobDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/javobs/{id}")
    public ResponseEntity<JavobDTO> getJavob(@PathVariable Long id) {
        log.debug("REST request to get Javob : {}", id);
        Optional<JavobDTO> javobDTO = javobService.findOne(id);
        return ResponseUtil.wrapOrNotFound(javobDTO);
    }

    /**
     * {@code DELETE  /javobs/:id} : delete the "id" javob.
     *
     * @param id the id of the javobDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/javobs/{id}")
    public ResponseEntity<Void> deleteJavob(@PathVariable Long id) {
        log.debug("REST request to delete Javob : {}", id);
        javobService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
