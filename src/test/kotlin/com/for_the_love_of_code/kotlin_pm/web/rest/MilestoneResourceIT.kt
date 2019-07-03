package com.for_the_love_of_code.kotlin_pm.web.rest

import com.for_the_love_of_code.kotlin_pm.KotlinPmApp
import com.for_the_love_of_code.kotlin_pm.domain.Milestone
import com.for_the_love_of_code.kotlin_pm.repository.MilestoneRepository
import com.for_the_love_of_code.kotlin_pm.repository.search.MilestoneSearchRepository
import com.for_the_love_of_code.kotlin_pm.web.rest.errors.ExceptionTranslator

import kotlin.test.assertNotNull

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator
import javax.persistence.EntityManager
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Collections

import com.for_the_love_of_code.kotlin_pm.web.rest.TestUtil.createFormattingConversionService
import org.assertj.core.api.Assertions.assertThat
import org.elasticsearch.index.query.QueryBuilders.queryStringQuery
import org.hamcrest.Matchers.hasItem
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.mockito.Mockito.reset
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * Test class for the MilestoneResource REST controller.
 *
 * @see MilestoneResource
 */
@SpringBootTest(classes = [KotlinPmApp::class])
class MilestoneResourceIT {

    @Autowired
    private lateinit var milestoneRepository: MilestoneRepository

    @Mock
    private lateinit var milestoneRepositoryMock: MilestoneRepository

    /**
     * This repository is mocked in the com.for_the_love_of_code.kotlin_pm.repository.search test package.
     *
     * @see com.for_the_love_of_code.kotlin_pm.repository.search.MilestoneSearchRepositoryMockConfiguration
     */
    @Autowired
    private lateinit var mockMilestoneSearchRepository: MilestoneSearchRepository

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var validator: Validator

    private lateinit var restMilestoneMockMvc: MockMvc

    private lateinit var milestone: Milestone

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val milestoneResource = MilestoneResource(milestoneRepository, mockMilestoneSearchRepository)
        this.restMilestoneMockMvc = MockMvcBuilders.standaloneSetup(milestoneResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        milestone = createEntity(em)
    }

    @Test
    @Transactional
    fun createMilestone() {
        val databaseSizeBeforeCreate = milestoneRepository.findAll().size

        // Create the Milestone
        restMilestoneMockMvc.perform(
            post("/api/milestones")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(milestone))
        ).andExpect(status().isCreated)

        // Validate the Milestone in the database
        val milestoneList = milestoneRepository.findAll()
        assertThat(milestoneList).hasSize(databaseSizeBeforeCreate + 1)
        val testMilestone = milestoneList[milestoneList.size - 1]
        assertThat(testMilestone.name).isEqualTo(DEFAULT_NAME)
        assertThat(testMilestone.estimatedEndDate).isEqualTo(DEFAULT_ESTIMATED_END_DATE)
        assertThat(testMilestone.endDate).isEqualTo(DEFAULT_END_DATE)

        // Validate the Milestone in Elasticsearch
    }

    @Test
    @Transactional
    fun createMilestoneWithExistingId() {
        val databaseSizeBeforeCreate = milestoneRepository.findAll().size

        // Create the Milestone with an existing ID
        milestone.id = 1L

        // An entity with an existing ID cannot be created, so this API call must fail
        restMilestoneMockMvc.perform(
            post("/api/milestones")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(milestone))
        ).andExpect(status().isBadRequest)

        // Validate the Milestone in the database
        val milestoneList = milestoneRepository.findAll()
        assertThat(milestoneList).hasSize(databaseSizeBeforeCreate)

        // Validate the Milestone in Elasticsearch
        verify(mockMilestoneSearchRepository, times(0)).save(milestone)
    }


    @Test
    @Transactional
    fun checkNameIsRequired() {
        val databaseSizeBeforeTest = milestoneRepository.findAll().size
        // set the field null
        milestone.name = null

        // Create the Milestone, which fails.

        restMilestoneMockMvc.perform(
            post("/api/milestones")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(milestone))
        ).andExpect(status().isBadRequest)

        val milestoneList = milestoneRepository.findAll()
        assertThat(milestoneList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun getAllMilestones() {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone)

        // Get all the milestoneList
        restMilestoneMockMvc.perform(get("/api/milestones?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(milestone.id?.toInt())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].estimatedEndDate").value(hasItem(DEFAULT_ESTIMATED_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
    }
    
    @SuppressWarnings("unchecked")
    fun getAllMilestonesWithEagerRelationshipsIsEnabled() {
        val milestoneResource = MilestoneResource(milestoneRepositoryMock, mockMilestoneSearchRepository)
        `when`(milestoneRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(PageImpl(mutableListOf()))

        val restMilestoneMockMvc = MockMvcBuilders.standaloneSetup(milestoneResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build()

        restMilestoneMockMvc.perform(get("/api/milestones?eagerload=true"))
        .andExpect(status().isOk)

        verify(milestoneRepositoryMock, times(1)).findAllWithEagerRelationships(any())
    }

    @SuppressWarnings("unchecked")
    fun getAllMilestonesWithEagerRelationshipsIsNotEnabled() {
        val milestoneResource = MilestoneResource(milestoneRepositoryMock, mockMilestoneSearchRepository)
            `when`(milestoneRepositoryMock.findAllWithEagerRelationships(any())).thenReturn( PageImpl( mutableListOf()))
            val restMilestoneMockMvc = MockMvcBuilders.standaloneSetup(milestoneResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build()

        restMilestoneMockMvc.perform(get("/api/milestones?eagerload=true"))
        .andExpect(status().isOk)

            verify(milestoneRepositoryMock, times(1)).findAllWithEagerRelationships(any())
    }

    @Test
    @Transactional
    fun getMilestone() {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone)

        val id = milestone.id
        assertNotNull(id)

        // Get the milestone
        restMilestoneMockMvc.perform(get("/api/milestones/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id.toInt()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.estimatedEndDate").value(DEFAULT_ESTIMATED_END_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
    }

    @Test
    @Transactional
    fun getNonExistingMilestone() {
        // Get the milestone
        restMilestoneMockMvc.perform(get("/api/milestones/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    @Transactional
    fun updateMilestone() {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone)

        val databaseSizeBeforeUpdate = milestoneRepository.findAll().size

        // Update the milestone
        val id = milestone.id
        assertNotNull(id)
        val updatedMilestone = milestoneRepository.findById(id).get()
        // Disconnect from session so that the updates on updatedMilestone are not directly saved in db
        em.detach(updatedMilestone)
        updatedMilestone.name = UPDATED_NAME
        updatedMilestone.estimatedEndDate = UPDATED_ESTIMATED_END_DATE
        updatedMilestone.endDate = UPDATED_END_DATE

        restMilestoneMockMvc.perform(
            put("/api/milestones")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMilestone))
        ).andExpect(status().isOk)

        // Validate the Milestone in the database
        val milestoneList = milestoneRepository.findAll()
        assertThat(milestoneList).hasSize(databaseSizeBeforeUpdate)
        val testMilestone = milestoneList[milestoneList.size - 1]
        assertThat(testMilestone.name).isEqualTo(UPDATED_NAME)
        assertThat(testMilestone.estimatedEndDate).isEqualTo(UPDATED_ESTIMATED_END_DATE)
        assertThat(testMilestone.endDate).isEqualTo(UPDATED_END_DATE)

        // Validate the Milestone in Elasticsearch
        verify(mockMilestoneSearchRepository, times(1)).save(testMilestone)
    }

    @Test
    @Transactional
    fun updateNonExistingMilestone() {
        val databaseSizeBeforeUpdate = milestoneRepository.findAll().size

        // Create the Milestone

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMilestoneMockMvc.perform(
            put("/api/milestones")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(milestone))
        ).andExpect(status().isBadRequest)

        // Validate the Milestone in the database
        val milestoneList = milestoneRepository.findAll()
        assertThat(milestoneList).hasSize(databaseSizeBeforeUpdate)

        // Validate the Milestone in Elasticsearch
        verify(mockMilestoneSearchRepository, times(0)).save(milestone)
    }

    @Test
    @Transactional
    fun deleteMilestone() {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone)

        val databaseSizeBeforeDelete = milestoneRepository.findAll().size

        val id = milestone.id
        assertNotNull(id)

        // Delete the milestone
        restMilestoneMockMvc.perform(
            delete("/api/milestones/{id}", id)
                .accept(TestUtil.APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database is empty
        val milestoneList = milestoneRepository.findAll()
        assertThat(milestoneList).hasSize(databaseSizeBeforeDelete - 1)

        // Validate the Milestone in Elasticsearch
        verify(mockMilestoneSearchRepository, times(1)).deleteById(id)
    }

    @Test
    @Transactional
    fun searchMilestone() {
        // Initialize the database
        milestoneRepository.saveAndFlush(milestone)
        `when`(mockMilestoneSearchRepository.search(queryStringQuery("id:" + milestone.id)))
            .thenReturn(Collections.singletonList(milestone))
        // Search the milestone
        restMilestoneMockMvc.perform(get("/api/_search/milestones?query=id:" + milestone.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(milestone.id?.toInt())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].estimatedEndDate").value(hasItem(DEFAULT_ESTIMATED_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
    }

    @Test
    @Transactional
    fun equalsVerifier() {
        TestUtil.equalsVerifier(Milestone::class.java)
        val milestone1 = Milestone()
        milestone1.id = 1L
        val milestone2 = Milestone()
        milestone2.id = milestone1.id
        assertThat(milestone1).isEqualTo(milestone2)
        milestone2.id = 2L
        assertThat(milestone1).isNotEqualTo(milestone2)
        milestone1.id = null
        assertThat(milestone1).isNotEqualTo(milestone2)
    }

    companion object {

        private const val DEFAULT_NAME: String = "AAAAAAAAAA"
        private const val UPDATED_NAME = "BBBBBBBBBB"

        private val DEFAULT_ESTIMATED_END_DATE: Instant = Instant.ofEpochMilli(0L)
        private val UPDATED_ESTIMATED_END_DATE: Instant = Instant.now().truncatedTo(ChronoUnit.MILLIS)

        private val DEFAULT_END_DATE: Instant = Instant.ofEpochMilli(0L)
        private val UPDATED_END_DATE: Instant = Instant.now().truncatedTo(ChronoUnit.MILLIS)
        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager): Milestone {
            val milestone = Milestone()
            milestone.name = DEFAULT_NAME
            milestone.estimatedEndDate = DEFAULT_ESTIMATED_END_DATE
            milestone.endDate = DEFAULT_END_DATE

        return milestone
        }
        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): Milestone {
            val milestone = Milestone()
            milestone.name = UPDATED_NAME
            milestone.estimatedEndDate = UPDATED_ESTIMATED_END_DATE
            milestone.endDate = UPDATED_END_DATE

        return milestone
        }
    }
}
