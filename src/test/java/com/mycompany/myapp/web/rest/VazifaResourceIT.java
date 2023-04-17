package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Vazifa;
import com.mycompany.myapp.repository.VazifaRepository;
import com.mycompany.myapp.service.VazifaService;
import com.mycompany.myapp.service.dto.VazifaDTO;
import com.mycompany.myapp.service.mapper.VazifaMapper;
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
 * Integration tests for the {@link VazifaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VazifaResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/vazifas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VazifaRepository vazifaRepository;

    @Mock
    private VazifaRepository vazifaRepositoryMock;

    @Autowired
    private VazifaMapper vazifaMapper;

    @Mock
    private VazifaService vazifaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVazifaMockMvc;

    private Vazifa vazifa;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vazifa createEntity(EntityManager em) {
        Vazifa vazifa = new Vazifa().title(DEFAULT_TITLE).description(DEFAULT_DESCRIPTION);
        return vazifa;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vazifa createUpdatedEntity(EntityManager em) {
        Vazifa vazifa = new Vazifa().title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);
        return vazifa;
    }

    @BeforeEach
    public void initTest() {
        vazifa = createEntity(em);
    }

    @Test
    @Transactional
    void createVazifa() throws Exception {
        int databaseSizeBeforeCreate = vazifaRepository.findAll().size();
        // Create the Vazifa
        VazifaDTO vazifaDTO = vazifaMapper.toDto(vazifa);
        restVazifaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vazifaDTO)))
            .andExpect(status().isCreated());

        // Validate the Vazifa in the database
        List<Vazifa> vazifaList = vazifaRepository.findAll();
        assertThat(vazifaList).hasSize(databaseSizeBeforeCreate + 1);
        Vazifa testVazifa = vazifaList.get(vazifaList.size() - 1);
        assertThat(testVazifa.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testVazifa.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createVazifaWithExistingId() throws Exception {
        // Create the Vazifa with an existing ID
        vazifa.setId(1L);
        VazifaDTO vazifaDTO = vazifaMapper.toDto(vazifa);

        int databaseSizeBeforeCreate = vazifaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVazifaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vazifaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vazifa in the database
        List<Vazifa> vazifaList = vazifaRepository.findAll();
        assertThat(vazifaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVazifas() throws Exception {
        // Initialize the database
        vazifaRepository.saveAndFlush(vazifa);

        // Get all the vazifaList
        restVazifaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vazifa.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVazifasWithEagerRelationshipsIsEnabled() throws Exception {
        when(vazifaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVazifaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(vazifaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVazifasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(vazifaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVazifaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(vazifaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getVazifa() throws Exception {
        // Initialize the database
        vazifaRepository.saveAndFlush(vazifa);

        // Get the vazifa
        restVazifaMockMvc
            .perform(get(ENTITY_API_URL_ID, vazifa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vazifa.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingVazifa() throws Exception {
        // Get the vazifa
        restVazifaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVazifa() throws Exception {
        // Initialize the database
        vazifaRepository.saveAndFlush(vazifa);

        int databaseSizeBeforeUpdate = vazifaRepository.findAll().size();

        // Update the vazifa
        Vazifa updatedVazifa = vazifaRepository.findById(vazifa.getId()).get();
        // Disconnect from session so that the updates on updatedVazifa are not directly saved in db
        em.detach(updatedVazifa);
        updatedVazifa.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);
        VazifaDTO vazifaDTO = vazifaMapper.toDto(updatedVazifa);

        restVazifaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vazifaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vazifaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Vazifa in the database
        List<Vazifa> vazifaList = vazifaRepository.findAll();
        assertThat(vazifaList).hasSize(databaseSizeBeforeUpdate);
        Vazifa testVazifa = vazifaList.get(vazifaList.size() - 1);
        assertThat(testVazifa.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testVazifa.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingVazifa() throws Exception {
        int databaseSizeBeforeUpdate = vazifaRepository.findAll().size();
        vazifa.setId(count.incrementAndGet());

        // Create the Vazifa
        VazifaDTO vazifaDTO = vazifaMapper.toDto(vazifa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVazifaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vazifaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vazifaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vazifa in the database
        List<Vazifa> vazifaList = vazifaRepository.findAll();
        assertThat(vazifaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVazifa() throws Exception {
        int databaseSizeBeforeUpdate = vazifaRepository.findAll().size();
        vazifa.setId(count.incrementAndGet());

        // Create the Vazifa
        VazifaDTO vazifaDTO = vazifaMapper.toDto(vazifa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVazifaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vazifaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vazifa in the database
        List<Vazifa> vazifaList = vazifaRepository.findAll();
        assertThat(vazifaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVazifa() throws Exception {
        int databaseSizeBeforeUpdate = vazifaRepository.findAll().size();
        vazifa.setId(count.incrementAndGet());

        // Create the Vazifa
        VazifaDTO vazifaDTO = vazifaMapper.toDto(vazifa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVazifaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vazifaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vazifa in the database
        List<Vazifa> vazifaList = vazifaRepository.findAll();
        assertThat(vazifaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVazifaWithPatch() throws Exception {
        // Initialize the database
        vazifaRepository.saveAndFlush(vazifa);

        int databaseSizeBeforeUpdate = vazifaRepository.findAll().size();

        // Update the vazifa using partial update
        Vazifa partialUpdatedVazifa = new Vazifa();
        partialUpdatedVazifa.setId(vazifa.getId());

        partialUpdatedVazifa.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restVazifaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVazifa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVazifa))
            )
            .andExpect(status().isOk());

        // Validate the Vazifa in the database
        List<Vazifa> vazifaList = vazifaRepository.findAll();
        assertThat(vazifaList).hasSize(databaseSizeBeforeUpdate);
        Vazifa testVazifa = vazifaList.get(vazifaList.size() - 1);
        assertThat(testVazifa.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testVazifa.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateVazifaWithPatch() throws Exception {
        // Initialize the database
        vazifaRepository.saveAndFlush(vazifa);

        int databaseSizeBeforeUpdate = vazifaRepository.findAll().size();

        // Update the vazifa using partial update
        Vazifa partialUpdatedVazifa = new Vazifa();
        partialUpdatedVazifa.setId(vazifa.getId());

        partialUpdatedVazifa.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restVazifaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVazifa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVazifa))
            )
            .andExpect(status().isOk());

        // Validate the Vazifa in the database
        List<Vazifa> vazifaList = vazifaRepository.findAll();
        assertThat(vazifaList).hasSize(databaseSizeBeforeUpdate);
        Vazifa testVazifa = vazifaList.get(vazifaList.size() - 1);
        assertThat(testVazifa.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testVazifa.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingVazifa() throws Exception {
        int databaseSizeBeforeUpdate = vazifaRepository.findAll().size();
        vazifa.setId(count.incrementAndGet());

        // Create the Vazifa
        VazifaDTO vazifaDTO = vazifaMapper.toDto(vazifa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVazifaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vazifaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vazifaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vazifa in the database
        List<Vazifa> vazifaList = vazifaRepository.findAll();
        assertThat(vazifaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVazifa() throws Exception {
        int databaseSizeBeforeUpdate = vazifaRepository.findAll().size();
        vazifa.setId(count.incrementAndGet());

        // Create the Vazifa
        VazifaDTO vazifaDTO = vazifaMapper.toDto(vazifa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVazifaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vazifaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vazifa in the database
        List<Vazifa> vazifaList = vazifaRepository.findAll();
        assertThat(vazifaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVazifa() throws Exception {
        int databaseSizeBeforeUpdate = vazifaRepository.findAll().size();
        vazifa.setId(count.incrementAndGet());

        // Create the Vazifa
        VazifaDTO vazifaDTO = vazifaMapper.toDto(vazifa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVazifaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vazifaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vazifa in the database
        List<Vazifa> vazifaList = vazifaRepository.findAll();
        assertThat(vazifaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVazifa() throws Exception {
        // Initialize the database
        vazifaRepository.saveAndFlush(vazifa);

        int databaseSizeBeforeDelete = vazifaRepository.findAll().size();

        // Delete the vazifa
        restVazifaMockMvc
            .perform(delete(ENTITY_API_URL_ID, vazifa.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vazifa> vazifaList = vazifaRepository.findAll();
        assertThat(vazifaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
