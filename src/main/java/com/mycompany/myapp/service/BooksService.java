package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Books;
import com.mycompany.myapp.repository.BooksRepository;
import com.mycompany.myapp.service.dto.BooksDTO;
import com.mycompany.myapp.service.mapper.BooksMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Books}.
 */
@Service
@Transactional
public class BooksService {

    private final Logger log = LoggerFactory.getLogger(BooksService.class);

    private final BooksRepository booksRepository;

    private final BooksMapper booksMapper;

    public BooksService(BooksRepository booksRepository, BooksMapper booksMapper) {
        this.booksRepository = booksRepository;
        this.booksMapper = booksMapper;
    }

    /**
     * Save a books.
     *
     * @param booksDTO the entity to save.
     * @return the persisted entity.
     */
    public BooksDTO save(BooksDTO booksDTO) {
        log.debug("Request to save Books : {}", booksDTO);
        Books books = booksMapper.toEntity(booksDTO);
        books = booksRepository.save(books);
        return booksMapper.toDto(books);
    }

    /**
     * Update a books.
     *
     * @param booksDTO the entity to save.
     * @return the persisted entity.
     */
    public BooksDTO update(BooksDTO booksDTO) {
        log.debug("Request to update Books : {}", booksDTO);
        Books books = booksMapper.toEntity(booksDTO);
        books = booksRepository.save(books);
        return booksMapper.toDto(books);
    }

    /**
     * Partially update a books.
     *
     * @param booksDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BooksDTO> partialUpdate(BooksDTO booksDTO) {
        log.debug("Request to partially update Books : {}", booksDTO);

        return booksRepository
            .findById(booksDTO.getId())
            .map(existingBooks -> {
                booksMapper.partialUpdate(existingBooks, booksDTO);

                return existingBooks;
            })
            .map(booksRepository::save)
            .map(booksMapper::toDto);
    }

    /**
     * Get all the books.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BooksDTO> findAll() {
        log.debug("Request to get all Books");
        return booksRepository.findAll().stream().map(booksMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one books by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BooksDTO> findOne(Long id) {
        log.debug("Request to get Books : {}", id);
        return booksRepository.findById(id).map(booksMapper::toDto);
    }

    /**
     * Delete the books by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Books : {}", id);
        booksRepository.deleteById(id);
    }
}
