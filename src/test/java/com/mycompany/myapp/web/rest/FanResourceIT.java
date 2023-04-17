package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Fan;
import com.mycompany.myapp.repository.FanRepository;
import com.mycompany.myapp.service.dto.FanDTO;
import com.mycompany.myapp.service.mapper.FanMapper;
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
 * Integration tests for the {@link FanResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FanResourceIT {

    private static final String DEFAULT_NOMI = "AAAAAAAAAA";
    private static final String UPDATED_NOMI = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fans";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FanRepository fanRepository;

    @Autowired
    private FanMapper fanMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFanMockMvc;

    private Fan fan;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fan createEntity(EntityManager em) {
        Fan fan = new Fan().nomi(DEFAULT_NOMI);
        return fan;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fan createUpdatedEntity(EntityManager em) {
        Fan fan = new Fan().nomi(UPDATED_NOMI);
        return fan;
    }

    @BeforeEach
    public void initTest() {
        fan = createEntity(em);
    }

    @Test
    @Transactional
    void createFan() throws Exception {
        int databaseSizeBeforeCreate = fanRepository.findAll().size();
        // Create the Fan
        FanDTO fanDTO = fanMapper.toDto(fan);
        restFanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fanDTO)))
            .andExpect(status().isCreated());

        // Validate the Fan in the database
        List<Fan> fanList = fanRepository.findAll();
        assertThat(fanList).hasSize(databaseSizeBeforeCreate + 1);
        Fan testFan = fanList.get(fanList.size() - 1);
        assertThat(testFan.getNomi()).isEqualTo(DEFAULT_NOMI);
    }

    @Test
    @Transactional
    void createFanWithExistingId() throws Exception {
        // Create the Fan with an existing ID
        fan.setId(1L);
        FanDTO fanDTO = fanMapper.toDto(fan);

        int databaseSizeBeforeCreate = fanRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fanDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Fan in the database
        List<Fan> fanList = fanRepository.findAll();
        assertThat(fanList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFans() throws Exception {
        // Initialize the database
        fanRepository.saveAndFlush(fan);

        // Get all the fanList
        restFanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fan.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomi").value(hasItem(DEFAULT_NOMI)));
    }

    @Test
    @Transactional
    void getFan() throws Exception {
        // Initialize the database
        fanRepository.saveAndFlush(fan);

        // Get the fan
        restFanMockMvc
            .perform(get(ENTITY_API_URL_ID, fan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fan.getId().intValue()))
            .andExpect(jsonPath("$.nomi").value(DEFAULT_NOMI));
    }

    @Test
    @Transactional
    void getNonExistingFan() throws Exception {
        // Get the fan
        restFanMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFan() throws Exception {
        // Initialize the database
        fanRepository.saveAndFlush(fan);

        int databaseSizeBeforeUpdate = fanRepository.findAll().size();

        // Update the fan
        Fan updatedFan = fanRepository.findById(fan.getId()).get();
        // Disconnect from session so that the updates on updatedFan are not directly saved in db
        em.detach(updatedFan);
        updatedFan.nomi(UPDATED_NOMI);
        FanDTO fanDTO = fanMapper.toDto(updatedFan);

        restFanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fanDTO))
            )
            .andExpect(status().isOk());

        // Validate the Fan in the database
        List<Fan> fanList = fanRepository.findAll();
        assertThat(fanList).hasSize(databaseSizeBeforeUpdate);
        Fan testFan = fanList.get(fanList.size() - 1);
        assertThat(testFan.getNomi()).isEqualTo(UPDATED_NOMI);
    }

    @Test
    @Transactional
    void putNonExistingFan() throws Exception {
        int databaseSizeBeforeUpdate = fanRepository.findAll().size();
        fan.setId(count.incrementAndGet());

        // Create the Fan
        FanDTO fanDTO = fanMapper.toDto(fan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fan in the database
        List<Fan> fanList = fanRepository.findAll();
        assertThat(fanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFan() throws Exception {
        int databaseSizeBeforeUpdate = fanRepository.findAll().size();
        fan.setId(count.incrementAndGet());

        // Create the Fan
        FanDTO fanDTO = fanMapper.toDto(fan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fan in the database
        List<Fan> fanList = fanRepository.findAll();
        assertThat(fanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFan() throws Exception {
        int databaseSizeBeforeUpdate = fanRepository.findAll().size();
        fan.setId(count.incrementAndGet());

        // Create the Fan
        FanDTO fanDTO = fanMapper.toDto(fan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFanMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fanDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fan in the database
        List<Fan> fanList = fanRepository.findAll();
        assertThat(fanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFanWithPatch() throws Exception {
        // Initialize the database
        fanRepository.saveAndFlush(fan);

        int databaseSizeBeforeUpdate = fanRepository.findAll().size();

        // Update the fan using partial update
        Fan partialUpdatedFan = new Fan();
        partialUpdatedFan.setId(fan.getId());

        partialUpdatedFan.nomi(UPDATED_NOMI);

        restFanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFan))
            )
            .andExpect(status().isOk());

        // Validate the Fan in the database
        List<Fan> fanList = fanRepository.findAll();
        assertThat(fanList).hasSize(databaseSizeBeforeUpdate);
        Fan testFan = fanList.get(fanList.size() - 1);
        assertThat(testFan.getNomi()).isEqualTo(UPDATED_NOMI);
    }

    @Test
    @Transactional
    void fullUpdateFanWithPatch() throws Exception {
        // Initialize the database
        fanRepository.saveAndFlush(fan);

        int databaseSizeBeforeUpdate = fanRepository.findAll().size();

        // Update the fan using partial update
        Fan partialUpdatedFan = new Fan();
        partialUpdatedFan.setId(fan.getId());

        partialUpdatedFan.nomi(UPDATED_NOMI);

        restFanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFan))
            )
            .andExpect(status().isOk());

        // Validate the Fan in the database
        List<Fan> fanList = fanRepository.findAll();
        assertThat(fanList).hasSize(databaseSizeBeforeUpdate);
        Fan testFan = fanList.get(fanList.size() - 1);
        assertThat(testFan.getNomi()).isEqualTo(UPDATED_NOMI);
    }

    @Test
    @Transactional
    void patchNonExistingFan() throws Exception {
        int databaseSizeBeforeUpdate = fanRepository.findAll().size();
        fan.setId(count.incrementAndGet());

        // Create the Fan
        FanDTO fanDTO = fanMapper.toDto(fan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fanDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fan in the database
        List<Fan> fanList = fanRepository.findAll();
        assertThat(fanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFan() throws Exception {
        int databaseSizeBeforeUpdate = fanRepository.findAll().size();
        fan.setId(count.incrementAndGet());

        // Create the Fan
        FanDTO fanDTO = fanMapper.toDto(fan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fan in the database
        List<Fan> fanList = fanRepository.findAll();
        assertThat(fanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFan() throws Exception {
        int databaseSizeBeforeUpdate = fanRepository.findAll().size();
        fan.setId(count.incrementAndGet());

        // Create the Fan
        FanDTO fanDTO = fanMapper.toDto(fan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFanMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fanDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fan in the database
        List<Fan> fanList = fanRepository.findAll();
        assertThat(fanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFan() throws Exception {
        // Initialize the database
        fanRepository.saveAndFlush(fan);

        int databaseSizeBeforeDelete = fanRepository.findAll().size();

        // Delete the fan
        restFanMockMvc.perform(delete(ENTITY_API_URL_ID, fan.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Fan> fanList = fanRepository.findAll();
        assertThat(fanList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
