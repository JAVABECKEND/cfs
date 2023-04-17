package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.BooksRepository;
import com.mycompany.myapp.service.BooksService;
import com.mycompany.myapp.service.dto.BooksDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Books}.
 */
@RestController
@RequestMapping("/api")
public class BooksResource {

    private final Logger log = LoggerFactory.getLogger(BooksResource.class);

    private static final String ENTITY_NAME = "books";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BooksService booksService;

    private final BooksRepository booksRepository;

    public BooksResource(BooksService booksService, BooksRepository booksRepository) {
        this.booksService = booksService;
        this.booksRepository = booksRepository;
    }

    /**
     * {@code POST  /books} : Create a new books.
     *
     * @param booksDTO the booksDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new booksDTO, or with status {@code 400 (Bad Request)} if the books has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/books")
    public ResponseEntity<BooksDTO> createBooks(@RequestBody BooksDTO booksDTO) throws URISyntaxException {
        log.debug("REST request to save Books : {}", booksDTO);
        if (booksDTO.getId() != null) {
            throw new BadRequestAlertException("A new books cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BooksDTO result = booksService.save(booksDTO);
        return ResponseEntity
            .created(new URI("/api/books/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /books/:id} : Updates an existing books.
     *
     * @param id the id of the booksDTO to save.
     * @param booksDTO the booksDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated booksDTO,
     * or with status {@code 400 (Bad Request)} if the booksDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the booksDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/books/{id}")
    public ResponseEntity<BooksDTO> updateBooks(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BooksDTO booksDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Books : {}, {}", id, booksDTO);
        if (booksDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, booksDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!booksRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BooksDTO result = booksService.update(booksDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, booksDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /books/:id} : Partial updates given fields of an existing books, field will ignore if it is null
     *
     * @param id the id of the booksDTO to save.
     * @param booksDTO the booksDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated booksDTO,
     * or with status {@code 400 (Bad Request)} if the booksDTO is not valid,
     * or with status {@code 404 (Not Found)} if the booksDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the booksDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/books/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BooksDTO> partialUpdateBooks(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BooksDTO booksDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Books partially : {}, {}", id, booksDTO);
        if (booksDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, booksDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!booksRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BooksDTO> result = booksService.partialUpdate(booksDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, booksDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /books} : get all the books.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of books in body.
     */
    @GetMapping("/books")
    public List<BooksDTO> getAllBooks() {
        log.debug("REST request to get all Books");
        return booksService.findAll();
    }

    /**
     * {@code GET  /books/:id} : get the "id" books.
     *
     * @param id the id of the booksDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the booksDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/books/{id}")
    public ResponseEntity<BooksDTO> getBooks(@PathVariable Long id) {
        log.debug("REST request to get Books : {}", id);
        Optional<BooksDTO> booksDTO = booksService.findOne(id);
        return ResponseUtil.wrapOrNotFound(booksDTO);
    }

    /**
     * {@code DELETE  /books/:id} : delete the "id" books.
     *
     * @param id the id of the booksDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBooks(@PathVariable Long id) {
        log.debug("REST request to delete Books : {}", id);
        booksService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
