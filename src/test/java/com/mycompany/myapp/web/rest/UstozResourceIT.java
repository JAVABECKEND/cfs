package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Ustoz;
import com.mycompany.myapp.repository.UstozRepository;
import com.mycompany.myapp.service.dto.UstozDTO;
import com.mycompany.myapp.service.mapper.UstozMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UstozResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UstozResourceIT {

    private static final String DEFAULT_ISM = "AAAAAAAAAA";
    private static final String UPDATED_ISM = "BBBBBBBBBB";

    private static final String DEFAULT_FAMILYA = "AAAAAAAAAA";
    private static final String UPDATED_FAMILYA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ustozs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UstozRepository ustozRepository;

    @Autowired
    private UstozMapper ustozMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUstozMockMvc;

    private Ustoz ustoz;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ustoz createEntity(EntityManager em) {
        Ustoz ustoz = new Ustoz().ism(DEFAULT_ISM).familya(DEFAULT_FAMILYA);
        return ustoz;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ustoz createUpdatedEntity(EntityManager em) {
        Ustoz ustoz = new Ustoz().ism(UPDATED_ISM).familya(UPDATED_FAMILYA);
        return ustoz;
    }

    @BeforeEach
    public void initTest() {
        ustoz = createEntity(em);
    }

    @Test
    @Transactional
    void createUstoz() throws Exception {
        int databaseSizeBeforeCreate = ustozRepository.findAll().size();
        // Create the Ustoz
        UstozDTO ustozDTO = ustozMapper.toDto(ustoz);
        restUstozMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ustozDTO)))
            .andExpect(status().isCreated());

        // Validate the Ustoz in the database
        List<Ustoz> ustozList = ustozRepository.findAll();
        assertThat(ustozList).hasSize(databaseSizeBeforeCreate + 1);
        Ustoz testUstoz = ustozList.get(ustozList.size() - 1);
        assertThat(testUstoz.getIsm()).isEqualTo(DEFAULT_ISM);
        assertThat(testUstoz.getFamilya()).isEqualTo(DEFAULT_FAMILYA);
    }

    @Test
    @Transactional
    void createUstozWithExistingId() throws Exception {
        // Create the Ustoz with an existing ID
        ustoz.setId(1L);
        UstozDTO ustozDTO = ustozMapper.toDto(ustoz);

        int databaseSizeBeforeCreate = ustozRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUstozMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ustozDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ustoz in the database
        List<Ustoz> ustozList = ustozRepository.findAll();
        assertThat(ustozList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUstozs() throws Exception {
        // Initialize the database
        ustozRepository.saveAndFlush(ustoz);

        // Get all the ustozList
        restUstozMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ustoz.getId().intValue())))
            .andExpect(jsonPath("$.[*].ism").value(hasItem(DEFAULT_ISM)))
            .andExpect(jsonPath("$.[*].familya").value(hasItem(DEFAULT_FAMILYA)));
    }

    @Test
    @Transactional
    void getUstoz() throws Exception {
        // Initialize the database
        ustozRepository.saveAndFlush(ustoz);

        // Get the ustoz
        restUstozMockMvc
            .perform(get(ENTITY_API_URL_ID, ustoz.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ustoz.getId().intValue()))
            .andExpect(jsonPath("$.ism").value(DEFAULT_ISM))
            .andExpect(jsonPath("$.familya").value(DEFAULT_FAMILYA));
    }

    @Test
    @Transactional
    void getNonExistingUstoz() throws Exception {
        // Get the ustoz
        restUstozMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUstoz() throws Exception {
        // Initialize the database
        ustozRepository.saveAndFlush(ustoz);

        int databaseSizeBeforeUpdate = ustozRepository.findAll().size();

        // Update the ustoz
        Ustoz updatedUstoz = ustozRepository.findById(ustoz.getId()).get();
        // Disconnect from session so that the updates on updatedUstoz are not directly saved in db
        em.detach(updatedUstoz);
        updatedUstoz.ism(UPDATED_ISM).familya(UPDATED_FAMILYA);
        UstozDTO ustozDTO = ustozMapper.toDto(updatedUstoz);

        restUstozMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ustozDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ustozDTO))
            )
            .andExpect(status().isOk());

        // Validate the Ustoz in the database
        List<Ustoz> ustozList = ustozRepository.findAll();
        assertThat(ustozList).hasSize(databaseSizeBeforeUpdate);
        Ustoz testUstoz = ustozList.get(ustozList.size() - 1);
        assertThat(testUstoz.getIsm()).isEqualTo(UPDATED_ISM);
        assertThat(testUstoz.getFamilya()).isEqualTo(UPDATED_FAMILYA);
    }

    @Test
    @Transactional
    void putNonExistingUstoz() throws Exception {
        int databaseSizeBeforeUpdate = ustozRepository.findAll().size();
        ustoz.setId(count.incrementAndGet());

        // Create the Ustoz
        UstozDTO ustozDTO = ustozMapper.toDto(ustoz);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUstozMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ustozDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ustozDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ustoz in the database
        List<Ustoz> ustozList = ustozRepository.findAll();
        assertThat(ustozList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUstoz() throws Exception {
        int databaseSizeBeforeUpdate = ustozRepository.findAll().size();
        ustoz.setId(count.incrementAndGet());

        // Create the Ustoz
        UstozDTO ustozDTO = ustozMapper.toDto(ustoz);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUstozMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ustozDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ustoz in the database
        List<Ustoz> ustozList = ustozRepository.findAll();
        assertThat(ustozList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUstoz() throws Exception {
        int databaseSizeBeforeUpdate = ustozRepository.findAll().size();
        ustoz.setId(count.incrementAndGet());

        // Create the Ustoz
        UstozDTO ustozDTO = ustozMapper.toDto(ustoz);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUstozMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ustozDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ustoz in the database
        List<Ustoz> ustozList = ustozRepository.findAll();
        assertThat(ustozList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUstozWithPatch() throws Exception {
        // Initialize the database
        ustozRepository.saveAndFlush(ustoz);

        int databaseSizeBeforeUpdate = ustozRepository.findAll().size();

        // Update the ustoz using partial update
        Ustoz partialUpdatedUstoz = new Ustoz();
        partialUpdatedUstoz.setId(ustoz.getId());

        partialUpdatedUstoz.ism(UPDATED_ISM).familya(UPDATED_FAMILYA);

        restUstozMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUstoz.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUstoz))
            )
            .andExpect(status().isOk());

        // Validate the Ustoz in the database
        List<Ustoz> ustozList = ustozRepository.findAll();
        assertThat(ustozList).hasSize(databaseSizeBeforeUpdate);
        Ustoz testUstoz = ustozList.get(ustozList.size() - 1);
        assertThat(testUstoz.getIsm()).isEqualTo(UPDATED_ISM);
        assertThat(testUstoz.getFamilya()).isEqualTo(UPDATED_FAMILYA);
    }

    @Test
    @Transactional
    void fullUpdateUstozWithPatch() throws Exception {
        // Initialize the database
        ustozRepository.saveAndFlush(ustoz);

        int databaseSizeBeforeUpdate = ustozRepository.findAll().size();

        // Update the ustoz using partial update
        Ustoz partialUpdatedUstoz = new Ustoz();
        partialUpdatedUstoz.setId(ustoz.getId());

        partialUpdatedUstoz.ism(UPDATED_ISM).familya(UPDATED_FAMILYA);

        restUstozMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUstoz.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUstoz))
            )
            .andExpect(status().isOk());

        // Validate the Ustoz in the database
        List<Ustoz> ustozList = ustozRepository.findAll();
        assertThat(ustozList).hasSize(databaseSizeBeforeUpdate);
        Ustoz testUstoz = ustozList.get(ustozList.size() - 1);
        assertThat(testUstoz.getIsm()).isEqualTo(UPDATED_ISM);
        assertThat(testUstoz.getFamilya()).isEqualTo(UPDATED_FAMILYA);
    }

    @Test
    @Transactional
    void patchNonExistingUstoz() throws Exception {
        int databaseSizeBeforeUpdate = ustozRepository.findAll().size();
        ustoz.setId(count.incrementAndGet());

        // Create the Ustoz
        UstozDTO ustozDTO = ustozMapper.toDto(ustoz);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUstozMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ustozDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ustozDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ustoz in the database
        List<Ustoz> ustozList = ustozRepository.findAll();
        assertThat(ustozList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUstoz() throws Exception {
        int databaseSizeBeforeUpdate = ustozRepository.findAll().size();
        ustoz.setId(count.incrementAndGet());

        // Create the Ustoz
        UstozDTO ustozDTO = ustozMapper.toDto(ustoz);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUstozMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ustozDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ustoz in the database
        List<Ustoz> ustozList = ustozRepository.findAll();
        assertThat(ustozList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUstoz() throws Exception {
        int databaseSizeBeforeUpdate = ustozRepository.findAll().size();
        ustoz.setId(count.incrementAndGet());

        // Create the Ustoz
        UstozDTO ustozDTO = ustozMapper.toDto(ustoz);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUstozMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ustozDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ustoz in the database
        List<Ustoz> ustozList = ustozRepository.findAll();
        assertThat(ustozList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUstoz() throws Exception {
        // Initialize the database
        ustozRepository.saveAndFlush(ustoz);

        int databaseSizeBeforeDelete = ustozRepository.findAll().size();

        // Delete the ustoz
        restUstozMockMvc
            .perform(delete(ENTITY_API_URL_ID, ustoz.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ustoz> ustozList = ustozRepository.findAll();
        assertThat(ustozList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
