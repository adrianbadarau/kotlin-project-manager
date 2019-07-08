package com.lenovo.coe.web.rest

import com.lenovo.coe.PmAppApp
import com.lenovo.coe.domain.BenefitType
import com.lenovo.coe.repository.BenefitTypeRepository
import com.lenovo.coe.service.BenefitTypeService
import com.lenovo.coe.web.rest.errors.ExceptionTranslator

import kotlin.test.assertNotNull

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.validation.Validator


import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.hasItem
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


/**
 * Test class for the BenefitTypeResource REST controller.
 *
 * @see BenefitTypeResource
 */
@SpringBootTest(classes = [PmAppApp::class])
class BenefitTypeResourceIT {

    @Autowired
    private lateinit var benefitTypeRepository: BenefitTypeRepository

    @Autowired
    private lateinit var benefitTypeService: BenefitTypeService

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var validator: Validator

    private lateinit var restBenefitTypeMockMvc: MockMvc

    private lateinit var benefitType: BenefitType

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val benefitTypeResource = BenefitTypeResource(benefitTypeService)
        this.restBenefitTypeMockMvc = MockMvcBuilders.standaloneSetup(benefitTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        benefitTypeRepository.deleteAll()
        benefitType = createEntity()
    }

    @Test
    fun createBenefitType() {
        val databaseSizeBeforeCreate = benefitTypeRepository.findAll().size

        // Create the BenefitType
        restBenefitTypeMockMvc.perform(
            post("/api/benefit-types")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(benefitType))
        ).andExpect(status().isCreated)

        // Validate the BenefitType in the database
        val benefitTypeList = benefitTypeRepository.findAll()
        assertThat(benefitTypeList).hasSize(databaseSizeBeforeCreate + 1)
        val testBenefitType = benefitTypeList[benefitTypeList.size - 1]
        assertThat(testBenefitType.name).isEqualTo(DEFAULT_NAME)
    }

    @Test
    fun createBenefitTypeWithExistingId() {
        val databaseSizeBeforeCreate = benefitTypeRepository.findAll().size

        // Create the BenefitType with an existing ID
        benefitType.id = "existing_id"

        // An entity with an existing ID cannot be created, so this API call must fail
        restBenefitTypeMockMvc.perform(
            post("/api/benefit-types")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(benefitType))
        ).andExpect(status().isBadRequest)

        // Validate the BenefitType in the database
        val benefitTypeList = benefitTypeRepository.findAll()
        assertThat(benefitTypeList).hasSize(databaseSizeBeforeCreate)
    }


    @Test
    fun checkNameIsRequired() {
        val databaseSizeBeforeTest = benefitTypeRepository.findAll().size
        // set the field null
        benefitType.name = null

        // Create the BenefitType, which fails.

        restBenefitTypeMockMvc.perform(
            post("/api/benefit-types")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(benefitType))
        ).andExpect(status().isBadRequest)

        val benefitTypeList = benefitTypeRepository.findAll()
        assertThat(benefitTypeList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    fun getAllBenefitTypes() {
        // Initialize the database
        benefitTypeRepository.save(benefitType)

        // Get all the benefitTypeList
        restBenefitTypeMockMvc.perform(get("/api/benefit-types?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(benefitType.id)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
    }
    
    @Test
    fun getBenefitType() {
        // Initialize the database
        benefitTypeRepository.save(benefitType)

        val id = benefitType.id
        assertNotNull(id)

        // Get the benefitType
        restBenefitTypeMockMvc.perform(get("/api/benefit-types/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
    }

    @Test
    fun getNonExistingBenefitType() {
        // Get the benefitType
        restBenefitTypeMockMvc.perform(get("/api/benefit-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    fun updateBenefitType() {
        // Initialize the database
        benefitTypeService.save(benefitType)

        val databaseSizeBeforeUpdate = benefitTypeRepository.findAll().size

        // Update the benefitType
        val id = benefitType.id
        assertNotNull(id)
        val updatedBenefitType = benefitTypeRepository.findById(id).get()
        updatedBenefitType.name = UPDATED_NAME

        restBenefitTypeMockMvc.perform(
            put("/api/benefit-types")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(updatedBenefitType))
        ).andExpect(status().isOk)

        // Validate the BenefitType in the database
        val benefitTypeList = benefitTypeRepository.findAll()
        assertThat(benefitTypeList).hasSize(databaseSizeBeforeUpdate)
        val testBenefitType = benefitTypeList[benefitTypeList.size - 1]
        assertThat(testBenefitType.name).isEqualTo(UPDATED_NAME)
    }

    @Test
    fun updateNonExistingBenefitType() {
        val databaseSizeBeforeUpdate = benefitTypeRepository.findAll().size

        // Create the BenefitType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBenefitTypeMockMvc.perform(
            put("/api/benefit-types")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(benefitType))
        ).andExpect(status().isBadRequest)

        // Validate the BenefitType in the database
        val benefitTypeList = benefitTypeRepository.findAll()
        assertThat(benefitTypeList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    fun deleteBenefitType() {
        // Initialize the database
        benefitTypeService.save(benefitType)

        val databaseSizeBeforeDelete = benefitTypeRepository.findAll().size

        val id = benefitType.id
        assertNotNull(id)

        // Delete the benefitType
        restBenefitTypeMockMvc.perform(
            delete("/api/benefit-types/{id}", id)
                .accept(APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val benefitTypeList = benefitTypeRepository.findAll()
        assertThat(benefitTypeList).hasSize(databaseSizeBeforeDelete - 1)
    }

    @Test
    fun equalsVerifier() {
        equalsVerifier(BenefitType::class)
        val benefitType1 = BenefitType()
        benefitType1.id = "id1"
        val benefitType2 = BenefitType()
        benefitType2.id = benefitType1.id
        assertThat(benefitType1).isEqualTo(benefitType2)
        benefitType2.id = "id2"
        assertThat(benefitType1).isNotEqualTo(benefitType2)
        benefitType1.id = null
        assertThat(benefitType1).isNotEqualTo(benefitType2)
    }

    companion object {

        private const val DEFAULT_NAME: String = "AAAAAAAAAA"
        private const val UPDATED_NAME = "BBBBBBBBBB"

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(): BenefitType {
            val benefitType = BenefitType(
                name = DEFAULT_NAME
            )

            return benefitType
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(): BenefitType {
            val benefitType = BenefitType(
                name = UPDATED_NAME
            )

            return benefitType
        }
    }
}
