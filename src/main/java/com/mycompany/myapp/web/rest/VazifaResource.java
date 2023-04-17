package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.VazifaRepository;
import com.mycompany.myapp.service.VazifaService;
import com.mycompany.myapp.service.dto.VazifaDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Vazifa}.
 */
@RestController
@RequestMapping("/api")
public class VazifaResource {

    private final Logger log = LoggerFactory.getLogger(VazifaResource.class);

    private static final String ENTITY_NAME = "vazifa";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VazifaService vazifaService;

    private final VazifaRepository vazifaRepository;

    public VazifaResource(VazifaService vazifaService, VazifaRepository vazifaRepository) {
        this.vazifaService = vazifaService;
        this.vazifaRepository = vazifaRepository;
    }

    /**
     * {@code POST  /vazifas} : Create a new vazifa.
     *
     * @param vazifaDTO the vazifaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vazifaDTO, or with status {@code 400 (Bad Request)} if the vazifa has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vazifas")
    public ResponseEntity<VazifaDTO> createVazifa(@RequestBody VazifaDTO vazifaDTO) throws URISyntaxException {
        log.debug("REST request to save Vazifa : {}", vazifaDTO);
        if (vazifaDTO.getId() != null) {
            throw new BadRequestAlertException("A new vazifa cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VazifaDTO result = vazifaService.save(vazifaDTO);
        return ResponseEntity
            .created(new URI("/api/vazifas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vazifas/:id} : Updates an existing vazifa.
     *
     * @param id the id of the vazifaDTO to save.
     * @param vazifaDTO the vazifaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vazifaDTO,
     * or with status {@code 400 (Bad Request)} if the vazifaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vazifaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vazifas/{id}")
    public ResponseEntity<VazifaDTO> updateVazifa(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VazifaDTO vazifaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Vazifa : {}, {}", id, vazifaDTO);
        if (vazifaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vazifaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vazifaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VazifaDTO result = vazifaService.update(vazifaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vazifaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vazifas/:id} : Partial updates given fields of an existing vazifa, field will ignore if it is null
     *
     * @param id the id of the vazifaDTO to save.
     * @param vazifaDTO the vazifaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vazifaDTO,
     * or with status {@code 400 (Bad Request)} if the vazifaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vazifaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vazifaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vazifas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VazifaDTO> partialUpdateVazifa(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VazifaDTO vazifaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Vazifa partially : {}, {}", id, vazifaDTO);
        if (vazifaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vazifaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vazifaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VazifaDTO> result = vazifaService.partialUpdate(vazifaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vazifaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vazifas} : get all the vazifas.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vazifas in body.
     */
    @GetMapping("/vazifas")
    public ResponseEntity<List<VazifaDTO>> getAllVazifas(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Vazifas");
        Page<VazifaDTO> page;
        if (eagerload) {
            page = vazifaService.findAllWithEagerRelationships(pageable);
        } else {
            page = vazifaService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vazifas/:id} : get the "id" vazifa.
     *
     * @param id the id of the vazifaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vazifaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vazifas/{id}")
    public ResponseEntity<VazifaDTO> getVazifa(@PathVariable Long id) {
        log.debug("REST request to get Vazifa : {}", id);
        Optional<VazifaDTO> vazifaDTO = vazifaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vazifaDTO);
    }

    /**
     * {@code DELETE  /vazifas/:id} : delete the "id" vazifa.
     *
     * @param id the id of the vazifaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vazifas/{id}")
    public ResponseEntity<Void> deleteVazifa(@PathVariable Long id) {
        log.debug("REST request to delete Vazifa : {}", id);
        vazifaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
