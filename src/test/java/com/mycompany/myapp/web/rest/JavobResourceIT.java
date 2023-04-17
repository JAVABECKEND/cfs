package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Javob;
import com.mycompany.myapp.domain.enumeration.Language;
import com.mycompany.myapp.repository.JavobRepository;
import com.mycompany.myapp.service.JavobService;
import com.mycompany.myapp.service.dto.JavobDTO;
import com.mycompany.myapp.service.mapper.JavobMapper;
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
 * Integration tests for the {@link JavobResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class JavobResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_KURSI = 1;
    private static final Integer UPDATED_KURSI = 2;

    private static final String DEFAULT_GURUH = "AAAAAAAAAA";
    private static final String UPDATED_GURUH = "BBBBBBBBBB";

    private static final String DEFAULT_JAVOB = "AAAAAAAAAA";
    private static final String UPDATED_JAVOB = "BBBBBBBBBB";

    private static final Language DEFAULT_LANGUAGE = Language.UZBEK;
    private static final Language UPDATED_LANGUAGE = Language.ENGLISH;

    private static final String ENTITY_API_URL = "/api/javobs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private JavobRepository javobRepository;

    @Mock
    private JavobRepository javobRepositoryMock;

    @Autowired
    private JavobMapper javobMapper;

    @Mock
    private JavobService javobServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJavobMockMvc;

    private Javob javob;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Javob createEntity(EntityManager em) {
        Javob javob = new Javob()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .kursi(DEFAULT_KURSI)
            .guruh(DEFAULT_GURUH)
            .javob(DEFAULT_JAVOB)
            .language(DEFAULT_LANGUAGE);
        return javob;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Javob createUpdatedEntity(EntityManager em) {
        Javob javob = new Javob()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .kursi(UPDATED_KURSI)
            .guruh(UPDATED_GURUH)
            .javob(UPDATED_JAVOB)
            .language(UPDATED_LANGUAGE);
        return javob;
    }

    @BeforeEach
    public void initTest() {
        javob = createEntity(em);
    }

    @Test
    @Transactional
    void createJavob() throws Exception {
        int databaseSizeBeforeCreate = javobRepository.findAll().size();
        // Create the Javob
        JavobDTO javobDTO = javobMapper.toDto(javob);
        restJavobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(javobDTO)))
            .andExpect(status().isCreated());

        // Validate the Javob in the database
        List<Javob> javobList = javobRepository.findAll();
        assertThat(javobList).hasSize(databaseSizeBeforeCreate + 1);
        Javob testJavob = javobList.get(javobList.size() - 1);
        assertThat(testJavob.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testJavob.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testJavob.getKursi()).isEqualTo(DEFAULT_KURSI);
        assertThat(testJavob.getGuruh()).isEqualTo(DEFAULT_GURUH);
        assertThat(testJavob.getJavob()).isEqualTo(DEFAULT_JAVOB);
        assertThat(testJavob.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
    }

    @Test
    @Transactional
    void createJavobWithExistingId() throws Exception {
        // Create the Javob with an existing ID
        javob.setId(1L);
        JavobDTO javobDTO = javobMapper.toDto(javob);

        int databaseSizeBeforeCreate = javobRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restJavobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(javobDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Javob in the database
        List<Javob> javobList = javobRepository.findAll();
        assertThat(javobList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllJavobs() throws Exception {
        // Initialize the database
        javobRepository.saveAndFlush(javob);

        // Get all the javobList
        restJavobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(javob.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].kursi").value(hasItem(DEFAULT_KURSI)))
            .andExpect(jsonPath("$.[*].guruh").value(hasItem(DEFAULT_GURUH)))
            .andExpect(jsonPath("$.[*].javob").value(hasItem(DEFAULT_JAVOB)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllJavobsWithEagerRelationshipsIsEnabled() throws Exception {
        when(javobServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restJavobMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(javobServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllJavobsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(javobServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restJavobMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(javobRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getJavob() throws Exception {
        // Initialize the database
        javobRepository.saveAndFlush(javob);

        // Get the javob
        restJavobMockMvc
            .perform(get(ENTITY_API_URL_ID, javob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(javob.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.kursi").value(DEFAULT_KURSI))
            .andExpect(jsonPath("$.guruh").value(DEFAULT_GURUH))
            .andExpect(jsonPath("$.javob").value(DEFAULT_JAVOB))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingJavob() throws Exception {
        // Get the javob
        restJavobMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingJavob() throws Exception {
        // Initialize the database
        javobRepository.saveAndFlush(javob);

        int databaseSizeBeforeUpdate = javobRepository.findAll().size();

        // Update the javob
        Javob updatedJavob = javobRepository.findById(javob.getId()).get();
        // Disconnect from session so that the updates on updatedJavob are not directly saved in db
        em.detach(updatedJavob);
        updatedJavob
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .kursi(UPDATED_KURSI)
            .guruh(UPDATED_GURUH)
            .javob(UPDATED_JAVOB)
            .language(UPDATED_LANGUAGE);
        JavobDTO javobDTO = javobMapper.toDto(updatedJavob);

        restJavobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, javobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(javobDTO))
            )
            .andExpect(status().isOk());

        // Validate the Javob in the database
        List<Javob> javobList = javobRepository.findAll();
        assertThat(javobList).hasSize(databaseSizeBeforeUpdate);
        Javob testJavob = javobList.get(javobList.size() - 1);
        assertThat(testJavob.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testJavob.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testJavob.getKursi()).isEqualTo(UPDATED_KURSI);
        assertThat(testJavob.getGuruh()).isEqualTo(UPDATED_GURUH);
        assertThat(testJavob.getJavob()).isEqualTo(UPDATED_JAVOB);
        assertThat(testJavob.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void putNonExistingJavob() throws Exception {
        int databaseSizeBeforeUpdate = javobRepository.findAll().size();
        javob.setId(count.incrementAndGet());

        // Create the Javob
        JavobDTO javobDTO = javobMapper.toDto(javob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJavobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, javobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(javobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Javob in the database
        List<Javob> javobList = javobRepository.findAll();
        assertThat(javobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchJavob() throws Exception {
        int databaseSizeBeforeUpdate = javobRepository.findAll().size();
        javob.setId(count.incrementAndGet());

        // Create the Javob
        JavobDTO javobDTO = javobMapper.toDto(javob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJavobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(javobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Javob in the database
        List<Javob> javobList = javobRepository.findAll();
        assertThat(javobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamJavob() throws Exception {
        int databaseSizeBeforeUpdate = javobRepository.findAll().size();
        javob.setId(count.incrementAndGet());

        // Create the Javob
        JavobDTO javobDTO = javobMapper.toDto(javob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJavobMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(javobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Javob in the database
        List<Javob> javobList = javobRepository.findAll();
        assertThat(javobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateJavobWithPatch() throws Exception {
        // Initialize the database
        javobRepository.saveAndFlush(javob);

        int databaseSizeBeforeUpdate = javobRepository.findAll().size();

        // Update the javob using partial update
        Javob partialUpdatedJavob = new Javob();
        partialUpdatedJavob.setId(javob.getId());

        partialUpdatedJavob
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .guruh(UPDATED_GURUH)
            .javob(UPDATED_JAVOB)
            .language(UPDATED_LANGUAGE);

        restJavobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJavob.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJavob))
            )
            .andExpect(status().isOk());

        // Validate the Javob in the database
        List<Javob> javobList = javobRepository.findAll();
        assertThat(javobList).hasSize(databaseSizeBeforeUpdate);
        Javob testJavob = javobList.get(javobList.size() - 1);
        assertThat(testJavob.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testJavob.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testJavob.getKursi()).isEqualTo(DEFAULT_KURSI);
        assertThat(testJavob.getGuruh()).isEqualTo(UPDATED_GURUH);
        assertThat(testJavob.getJavob()).isEqualTo(UPDATED_JAVOB);
        assertThat(testJavob.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void fullUpdateJavobWithPatch() throws Exception {
        // Initialize the database
        javobRepository.saveAndFlush(javob);

        int databaseSizeBeforeUpdate = javobRepository.findAll().size();

        // Update the javob using partial update
        Javob partialUpdatedJavob = new Javob();
        partialUpdatedJavob.setId(javob.getId());

        partialUpdatedJavob
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .kursi(UPDATED_KURSI)
            .guruh(UPDATED_GURUH)
            .javob(UPDATED_JAVOB)
            .language(UPDATED_LANGUAGE);

        restJavobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJavob.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJavob))
            )
            .andExpect(status().isOk());

        // Validate the Javob in the database
        List<Javob> javobList = javobRepository.findAll();
        assertThat(javobList).hasSize(databaseSizeBeforeUpdate);
        Javob testJavob = javobList.get(javobList.size() - 1);
        assertThat(testJavob.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testJavob.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testJavob.getKursi()).isEqualTo(UPDATED_KURSI);
        assertThat(testJavob.getGuruh()).isEqualTo(UPDATED_GURUH);
        assertThat(testJavob.getJavob()).isEqualTo(UPDATED_JAVOB);
        assertThat(testJavob.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void patchNonExistingJavob() throws Exception {
        int databaseSizeBeforeUpdate = javobRepository.findAll().size();
        javob.setId(count.incrementAndGet());

        // Create the Javob
        JavobDTO javobDTO = javobMapper.toDto(javob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJavobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, javobDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(javobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Javob in the database
        List<Javob> javobList = javobRepository.findAll();
        assertThat(javobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchJavob() throws Exception {
        int databaseSizeBeforeUpdate = javobRepository.findAll().size();
        javob.setId(count.incrementAndGet());

        // Create the Javob
        JavobDTO javobDTO = javobMapper.toDto(javob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJavobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(javobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Javob in the database
        List<Javob> javobList = javobRepository.findAll();
        assertThat(javobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamJavob() throws Exception {
        int databaseSizeBeforeUpdate = javobRepository.findAll().size();
        javob.setId(count.incrementAndGet());

        // Create the Javob
        JavobDTO javobDTO = javobMapper.toDto(javob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJavobMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(javobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Javob in the database
        List<Javob> javobList = javobRepository.findAll();
        assertThat(javobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteJavob() throws Exception {
        // Initialize the database
        javobRepository.saveAndFlush(javob);

        int databaseSizeBeforeDelete = javobRepository.findAll().size();

        // Delete the javob
        restJavobMockMvc
            .perform(delete(ENTITY_API_URL_ID, javob.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Javob> javobList = javobRepository.findAll();
        assertThat(javobList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
