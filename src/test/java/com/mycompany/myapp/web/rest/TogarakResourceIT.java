package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Togarak;
import com.mycompany.myapp.domain.enumeration.Nomi;
import com.mycompany.myapp.repository.TogarakRepository;
import com.mycompany.myapp.service.TogarakService;
import com.mycompany.myapp.service.dto.TogarakDTO;
import com.mycompany.myapp.service.mapper.TogarakMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TogarakResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TogarakResourceIT {

    private static final Nomi DEFAULT_TOGARAK_NOMI = Nomi.KRIMINALIST;
    private static final Nomi UPDATED_TOGARAK_NOMI = Nomi.EKSPERT;

    private static final String ENTITY_API_URL = "/api/togaraks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TogarakRepository togarakRepository;

    @Mock
    private TogarakRepository togarakRepositoryMock;

    @Autowired
    private TogarakMapper togarakMapper;

    @Mock
    private TogarakService togarakServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTogarakMockMvc;

    private Togarak togarak;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Togarak createEntity(EntityManager em) {
        Togarak togarak = new Togarak().togarakNomi(DEFAULT_TOGARAK_NOMI);
        return togarak;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Togarak createUpdatedEntity(EntityManager em) {
        Togarak togarak = new Togarak().togarakNomi(UPDATED_TOGARAK_NOMI);
        return togarak;
    }

    @BeforeEach
    public void initTest() {
        togarak = createEntity(em);
    }

    @Test
    @Transactional
    void createTogarak() throws Exception {
        int databaseSizeBeforeCreate = togarakRepository.findAll().size();
        // Create the Togarak
        TogarakDTO togarakDTO = togarakMapper.toDto(togarak);
        restTogarakMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(togarakDTO)))
            .andExpect(status().isCreated());

        // Validate the Togarak in the database
        List<Togarak> togarakList = togarakRepository.findAll();
        assertThat(togarakList).hasSize(databaseSizeBeforeCreate + 1);
        Togarak testTogarak = togarakList.get(togarakList.size() - 1);
        assertThat(testTogarak.getTogarakNomi()).isEqualTo(DEFAULT_TOGARAK_NOMI);
    }

    @Test
    @Transactional
    void createTogarakWithExistingId() throws Exception {
        // Create the Togarak with an existing ID
        togarak.setId(1L);
        TogarakDTO togarakDTO = togarakMapper.toDto(togarak);

        int databaseSizeBeforeCreate = togarakRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTogarakMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(togarakDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Togarak in the database
        List<Togarak> togarakList = togarakRepository.findAll();
        assertThat(togarakList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTogaraks() throws Exception {
        // Initialize the database
        togarakRepository.saveAndFlush(togarak);

        // Get all the togarakList
        restTogarakMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(togarak.getId().intValue())))
            .andExpect(jsonPath("$.[*].togarakNomi").value(hasItem(DEFAULT_TOGARAK_NOMI.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTogaraksWithEagerRelationshipsIsEnabled() throws Exception {
        when(togarakServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTogarakMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(togarakServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTogaraksWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(togarakServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTogarakMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(togarakRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTogarak() throws Exception {
        // Initialize the database
        togarakRepository.saveAndFlush(togarak);

        // Get the togarak
        restTogarakMockMvc
            .perform(get(ENTITY_API_URL_ID, togarak.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(togarak.getId().intValue()))
            .andExpect(jsonPath("$.togarakNomi").value(DEFAULT_TOGARAK_NOMI.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTogarak() throws Exception {
        // Get the togarak
        restTogarakMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTogarak() throws Exception {
        // Initialize the database
        togarakRepository.saveAndFlush(togarak);

        int databaseSizeBeforeUpdate = togarakRepository.findAll().size();

        // Update the togarak
        Togarak updatedTogarak = togarakRepository.findById(togarak.getId()).get();
        // Disconnect from session so that the updates on updatedTogarak are not directly saved in db
        em.detach(updatedTogarak);
        updatedTogarak.togarakNomi(UPDATED_TOGARAK_NOMI);
        TogarakDTO togarakDTO = togarakMapper.toDto(updatedTogarak);

        restTogarakMockMvc
            .perform(
                put(ENTITY_API_URL_ID, togarakDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(togarakDTO))
            )
            .andExpect(status().isOk());

        // Validate the Togarak in the database
        List<Togarak> togarakList = togarakRepository.findAll();
        assertThat(togarakList).hasSize(databaseSizeBeforeUpdate);
        Togarak testTogarak = togarakList.get(togarakList.size() - 1);
        assertThat(testTogarak.getTogarakNomi()).isEqualTo(UPDATED_TOGARAK_NOMI);
    }

    @Test
    @Transactional
    void putNonExistingTogarak() throws Exception {
        int databaseSizeBeforeUpdate = togarakRepository.findAll().size();
        togarak.setId(count.incrementAndGet());

        // Create the Togarak
        TogarakDTO togarakDTO = togarakMapper.toDto(togarak);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTogarakMockMvc
            .perform(
                put(ENTITY_API_URL_ID, togarakDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(togarakDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Togarak in the database
        List<Togarak> togarakList = togarakRepository.findAll();
        assertThat(togarakList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTogarak() throws Exception {
        int databaseSizeBeforeUpdate = togarakRepository.findAll().size();
        togarak.setId(count.incrementAndGet());

        // Create the Togarak
        TogarakDTO togarakDTO = togarakMapper.toDto(togarak);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTogarakMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(togarakDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Togarak in the database
        List<Togarak> togarakList = togarakRepository.findAll();
        assertThat(togarakList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTogarak() throws Exception {
        int databaseSizeBeforeUpdate = togarakRepository.findAll().size();
        togarak.setId(count.incrementAndGet());

        // Create the Togarak
        TogarakDTO togarakDTO = togarakMapper.toDto(togarak);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTogarakMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(togarakDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Togarak in the database
        List<Togarak> togarakList = togarakRepository.findAll();
        assertThat(togarakList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTogarakWithPatch() throws Exception {
        // Initialize the database
        togarakRepository.saveAndFlush(togarak);

        int databaseSizeBeforeUpdate = togarakRepository.findAll().size();

        // Update the togarak using partial update
        Togarak partialUpdatedTogarak = new Togarak();
        partialUpdatedTogarak.setId(togarak.getId());

        restTogarakMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTogarak.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTogarak))
            )
            .andExpect(status().isOk());

        // Validate the Togarak in the database
        List<Togarak> togarakList = togarakRepository.findAll();
        assertThat(togarakList).hasSize(databaseSizeBeforeUpdate);
        Togarak testTogarak = togarakList.get(togarakList.size() - 1);
        assertThat(testTogarak.getTogarakNomi()).isEqualTo(DEFAULT_TOGARAK_NOMI);
    }

    @Test
    @Transactional
    void fullUpdateTogarakWithPatch() throws Exception {
        // Initialize the database
        togarakRepository.saveAndFlush(togarak);

        int databaseSizeBeforeUpdate = togarakRepository.findAll().size();

        // Update the togarak using partial update
        Togarak partialUpdatedTogarak = new Togarak();
        partialUpdatedTogarak.setId(togarak.getId());

        partialUpdatedTogarak.togarakNomi(UPDATED_TOGARAK_NOMI);

        restTogarakMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTogarak.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTogarak))
            )
            .andExpect(status().isOk());

        // Validate the Togarak in the database
        List<Togarak> togarakList = togarakRepository.findAll();
        assertThat(togarakList).hasSize(databaseSizeBeforeUpdate);
        Togarak testTogarak = togarakList.get(togarakList.size() - 1);
        assertThat(testTogarak.getTogarakNomi()).isEqualTo(UPDATED_TOGARAK_NOMI);
    }

    @Test
    @Transactional
    void patchNonExistingTogarak() throws Exception {
        int databaseSizeBeforeUpdate = togarakRepository.findAll().size();
        togarak.setId(count.incrementAndGet());

        // Create the Togarak
        TogarakDTO togarakDTO = togarakMapper.toDto(togarak);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTogarakMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, togarakDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(togarakDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Togarak in the database
        List<Togarak> togarakList = togarakRepository.findAll();
        assertThat(togarakList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTogarak() throws Exception {
        int databaseSizeBeforeUpdate = togarakRepository.findAll().size();
        togarak.setId(count.incrementAndGet());

        // Create the Togarak
        TogarakDTO togarakDTO = togarakMapper.toDto(togarak);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTogarakMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(togarakDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Togarak in the database
        List<Togarak> togarakList = togarakRepository.findAll();
        assertThat(togarakList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTogarak() throws Exception {
        int databaseSizeBeforeUpdate = togarakRepository.findAll().size();
        togarak.setId(count.incrementAndGet());

        // Create the Togarak
        TogarakDTO togarakDTO = togarakMapper.toDto(togarak);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTogarakMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(togarakDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Togarak in the database
        List<Togarak> togarakList = togarakRepository.findAll();
        assertThat(togarakList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTogarak() throws Exception {
        // Initialize the database
        togarakRepository.saveAndFlush(togarak);

        int databaseSizeBeforeDelete = togarakRepository.findAll().size();

        // Delete the togarak
        restTogarakMockMvc
            .perform(delete(ENTITY_API_URL_ID, togarak.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Togarak> togarakList = togarakRepository.findAll();
        assertThat(togarakList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
