package com.lenovo.coe.web.rest

import com.lenovo.coe.PmAppApp
import com.lenovo.coe.domain.BusinessCase
import com.lenovo.coe.repository.BusinessCaseRepository
import com.lenovo.coe.service.BusinessCaseService
import com.lenovo.coe.web.rest.errors.ExceptionTranslator

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.validation.Validator


import org.assertj.core.api.Assertions.assertThat
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
 * Test class for the BusinessCaseResource REST controller.
 *
 * @see BusinessCaseResource
 */
@SpringBootTest(classes = [PmAppApp::class])
class BusinessCaseResourceIT {

    @Autowired
    private lateinit var businessCaseRepository: BusinessCaseRepository

    @Mock
    private lateinit var businessCaseRepositoryMock: BusinessCaseRepository

    @Mock
    private lateinit var businessCaseServiceMock: BusinessCaseService

    @Autowired
    private lateinit var businessCaseService: BusinessCaseService

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var validator: Validator

    private lateinit var restBusinessCaseMockMvc: MockMvc

    private lateinit var businessCase: BusinessCase

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val businessCaseResource = BusinessCaseResource(businessCaseService)
        this.restBusinessCaseMockMvc = MockMvcBuilders.standaloneSetup(businessCaseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        businessCaseRepository.deleteAll()
        businessCase = createEntity()
    }

    @Test
    fun createBusinessCase() {
        val databaseSizeBeforeCreate = businessCaseRepository.findAll().size

        // Create the BusinessCase
        restBusinessCaseMockMvc.perform(
            post("/api/business-cases")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(businessCase))
        ).andExpect(status().isCreated)

        // Validate the BusinessCase in the database
        val businessCaseList = businessCaseRepository.findAll()
        assertThat(businessCaseList).hasSize(databaseSizeBeforeCreate + 1)
        val testBusinessCase = businessCaseList[businessCaseList.size - 1]
        assertThat(testBusinessCase.summary).isEqualTo(DEFAULT_SUMMARY)
    }

    @Test
    fun createBusinessCaseWithExistingId() {
        val databaseSizeBeforeCreate = businessCaseRepository.findAll().size

        // Create the BusinessCase with an existing ID
        businessCase.id = "existing_id"

        // An entity with an existing ID cannot be created, so this API call must fail
        restBusinessCaseMockMvc.perform(
            post("/api/business-cases")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(businessCase))
        ).andExpect(status().isBadRequest)

        // Validate the BusinessCase in the database
        val businessCaseList = businessCaseRepository.findAll()
        assertThat(businessCaseList).hasSize(databaseSizeBeforeCreate)
    }


    @Test
    fun getAllBusinessCases() {
        // Initialize the database
        businessCaseRepository.save(businessCase)

        // Get all the businessCaseList
        restBusinessCaseMockMvc.perform(get("/api/business-cases?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(businessCase.id)))
            .andExpect(jsonPath("$.[*].summary").value(hasItem(DEFAULT_SUMMARY)))
    }
    
    @Suppress("unchecked")
    fun getAllBusinessCasesWithEagerRelationshipsIsEnabled() {
        val businessCaseResource = BusinessCaseResource(businessCaseServiceMock)
        `when`(businessCaseServiceMock.findAllWithEagerRelationships(any())).thenReturn(PageImpl(mutableListOf()))

        val restBusinessCaseMockMvc = MockMvcBuilders.standaloneSetup(businessCaseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build()

        restBusinessCaseMockMvc.perform(get("/api/business-cases?eagerload=true"))
            .andExpect(status().isOk)

        verify(businessCaseServiceMock, times(1)).findAllWithEagerRelationships(any())
    }

    @Suppress("unchecked")
    fun getAllBusinessCasesWithEagerRelationshipsIsNotEnabled() {
        val businessCaseResource = BusinessCaseResource(businessCaseServiceMock)
            `when`(businessCaseServiceMock.findAllWithEagerRelationships(any())).thenReturn( PageImpl( mutableListOf()))
        val restBusinessCaseMockMvc = MockMvcBuilders.standaloneSetup(businessCaseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build()

        restBusinessCaseMockMvc.perform(get("/api/business-cases?eagerload=true"))
            .andExpect(status().isOk)

        verify(businessCaseServiceMock, times(1)).findAllWithEagerRelationships(any())
    }

    @Test
    fun getBusinessCase() {
        // Initialize the database
        businessCaseRepository.save(businessCase)

        val id = businessCase.id
        assertNotNull(id)

        // Get the businessCase
        restBusinessCaseMockMvc.perform(get("/api/business-cases/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.summary").value(DEFAULT_SUMMARY))
    }

    @Test
    fun getNonExistingBusinessCase() {
        // Get the businessCase
        restBusinessCaseMockMvc.perform(get("/api/business-cases/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    fun updateBusinessCase() {
        // Initialize the database
        businessCaseService.save(businessCase)

        val databaseSizeBeforeUpdate = businessCaseRepository.findAll().size

        // Update the businessCase
        val id = businessCase.id
        assertNotNull(id)
        val updatedBusinessCase = businessCaseRepository.findById(id).get()
        updatedBusinessCase.summary = UPDATED_SUMMARY

        restBusinessCaseMockMvc.perform(
            put("/api/business-cases")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(updatedBusinessCase))
        ).andExpect(status().isOk)

        // Validate the BusinessCase in the database
        val businessCaseList = businessCaseRepository.findAll()
        assertThat(businessCaseList).hasSize(databaseSizeBeforeUpdate)
        val testBusinessCase = businessCaseList[businessCaseList.size - 1]
        assertThat(testBusinessCase.summary).isEqualTo(UPDATED_SUMMARY)
    }

    @Test
    fun updateNonExistingBusinessCase() {
        val databaseSizeBeforeUpdate = businessCaseRepository.findAll().size

        // Create the BusinessCase

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusinessCaseMockMvc.perform(
            put("/api/business-cases")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(businessCase))
        ).andExpect(status().isBadRequest)

        // Validate the BusinessCase in the database
        val businessCaseList = businessCaseRepository.findAll()
        assertThat(businessCaseList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    fun deleteBusinessCase() {
        // Initialize the database
        businessCaseService.save(businessCase)

        val databaseSizeBeforeDelete = businessCaseRepository.findAll().size

        val id = businessCase.id
        assertNotNull(id)

        // Delete the businessCase
        restBusinessCaseMockMvc.perform(
            delete("/api/business-cases/{id}", id)
                .accept(APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val businessCaseList = businessCaseRepository.findAll()
        assertThat(businessCaseList).hasSize(databaseSizeBeforeDelete - 1)
    }

    @Test
    fun equalsVerifier() {
        equalsVerifier(BusinessCase::class)
        val businessCase1 = BusinessCase()
        businessCase1.id = "id1"
        val businessCase2 = BusinessCase()
        businessCase2.id = businessCase1.id
        assertThat(businessCase1).isEqualTo(businessCase2)
        businessCase2.id = "id2"
        assertThat(businessCase1).isNotEqualTo(businessCase2)
        businessCase1.id = null
        assertThat(businessCase1).isNotEqualTo(businessCase2)
    }

    companion object {

        private const val DEFAULT_SUMMARY: String = "AAAAAAAAAA"
        private const val UPDATED_SUMMARY = "BBBBBBBBBB"

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(): BusinessCase {
            val businessCase = BusinessCase(
                summary = DEFAULT_SUMMARY
            )

            return businessCase
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(): BusinessCase {
            val businessCase = BusinessCase(
                summary = UPDATED_SUMMARY
            )

            return businessCase
        }
    }
}
