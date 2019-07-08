package com.lenovo.coe.web.rest

import com.lenovo.coe.PmAppApp
import com.lenovo.coe.domain.Benefit
import com.lenovo.coe.repository.BenefitRepository
import com.lenovo.coe.service.BenefitService
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
 * Test class for the BenefitResource REST controller.
 *
 * @see BenefitResource
 */
@SpringBootTest(classes = [PmAppApp::class])
class BenefitResourceIT {

    @Autowired
    private lateinit var benefitRepository: BenefitRepository

    @Mock
    private lateinit var benefitRepositoryMock: BenefitRepository

    @Mock
    private lateinit var benefitServiceMock: BenefitService

    @Autowired
    private lateinit var benefitService: BenefitService

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var validator: Validator

    private lateinit var restBenefitMockMvc: MockMvc

    private lateinit var benefit: Benefit

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val benefitResource = BenefitResource(benefitService)
        this.restBenefitMockMvc = MockMvcBuilders.standaloneSetup(benefitResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        benefitRepository.deleteAll()
        benefit = createEntity()
    }

    @Test
    fun createBenefit() {
        val databaseSizeBeforeCreate = benefitRepository.findAll().size

        // Create the Benefit
        restBenefitMockMvc.perform(
            post("/api/benefits")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(benefit))
        ).andExpect(status().isCreated)

        // Validate the Benefit in the database
        val benefitList = benefitRepository.findAll()
        assertThat(benefitList).hasSize(databaseSizeBeforeCreate + 1)
        val testBenefit = benefitList[benefitList.size - 1]
        assertThat(testBenefit.description).isEqualTo(DEFAULT_DESCRIPTION)
    }

    @Test
    fun createBenefitWithExistingId() {
        val databaseSizeBeforeCreate = benefitRepository.findAll().size

        // Create the Benefit with an existing ID
        benefit.id = "existing_id"

        // An entity with an existing ID cannot be created, so this API call must fail
        restBenefitMockMvc.perform(
            post("/api/benefits")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(benefit))
        ).andExpect(status().isBadRequest)

        // Validate the Benefit in the database
        val benefitList = benefitRepository.findAll()
        assertThat(benefitList).hasSize(databaseSizeBeforeCreate)
    }


    @Test
    fun checkDescriptionIsRequired() {
        val databaseSizeBeforeTest = benefitRepository.findAll().size
        // set the field null
        benefit.description = null

        // Create the Benefit, which fails.

        restBenefitMockMvc.perform(
            post("/api/benefits")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(benefit))
        ).andExpect(status().isBadRequest)

        val benefitList = benefitRepository.findAll()
        assertThat(benefitList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    fun getAllBenefits() {
        // Initialize the database
        benefitRepository.save(benefit)

        // Get all the benefitList
        restBenefitMockMvc.perform(get("/api/benefits?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(benefit.id)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
    }
    
    @Suppress("unchecked")
    fun getAllBenefitsWithEagerRelationshipsIsEnabled() {
        val benefitResource = BenefitResource(benefitServiceMock)
        `when`(benefitServiceMock.findAllWithEagerRelationships(any())).thenReturn(PageImpl(mutableListOf()))

        val restBenefitMockMvc = MockMvcBuilders.standaloneSetup(benefitResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build()

        restBenefitMockMvc.perform(get("/api/benefits?eagerload=true"))
            .andExpect(status().isOk)

        verify(benefitServiceMock, times(1)).findAllWithEagerRelationships(any())
    }

    @Suppress("unchecked")
    fun getAllBenefitsWithEagerRelationshipsIsNotEnabled() {
        val benefitResource = BenefitResource(benefitServiceMock)
            `when`(benefitServiceMock.findAllWithEagerRelationships(any())).thenReturn( PageImpl( mutableListOf()))
        val restBenefitMockMvc = MockMvcBuilders.standaloneSetup(benefitResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build()

        restBenefitMockMvc.perform(get("/api/benefits?eagerload=true"))
            .andExpect(status().isOk)

        verify(benefitServiceMock, times(1)).findAllWithEagerRelationships(any())
    }

    @Test
    fun getBenefit() {
        // Initialize the database
        benefitRepository.save(benefit)

        val id = benefit.id
        assertNotNull(id)

        // Get the benefit
        restBenefitMockMvc.perform(get("/api/benefits/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
    }

    @Test
    fun getNonExistingBenefit() {
        // Get the benefit
        restBenefitMockMvc.perform(get("/api/benefits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    fun updateBenefit() {
        // Initialize the database
        benefitService.save(benefit)

        val databaseSizeBeforeUpdate = benefitRepository.findAll().size

        // Update the benefit
        val id = benefit.id
        assertNotNull(id)
        val updatedBenefit = benefitRepository.findById(id).get()
        updatedBenefit.description = UPDATED_DESCRIPTION

        restBenefitMockMvc.perform(
            put("/api/benefits")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(updatedBenefit))
        ).andExpect(status().isOk)

        // Validate the Benefit in the database
        val benefitList = benefitRepository.findAll()
        assertThat(benefitList).hasSize(databaseSizeBeforeUpdate)
        val testBenefit = benefitList[benefitList.size - 1]
        assertThat(testBenefit.description).isEqualTo(UPDATED_DESCRIPTION)
    }

    @Test
    fun updateNonExistingBenefit() {
        val databaseSizeBeforeUpdate = benefitRepository.findAll().size

        // Create the Benefit

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBenefitMockMvc.perform(
            put("/api/benefits")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(benefit))
        ).andExpect(status().isBadRequest)

        // Validate the Benefit in the database
        val benefitList = benefitRepository.findAll()
        assertThat(benefitList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    fun deleteBenefit() {
        // Initialize the database
        benefitService.save(benefit)

        val databaseSizeBeforeDelete = benefitRepository.findAll().size

        val id = benefit.id
        assertNotNull(id)

        // Delete the benefit
        restBenefitMockMvc.perform(
            delete("/api/benefits/{id}", id)
                .accept(APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val benefitList = benefitRepository.findAll()
        assertThat(benefitList).hasSize(databaseSizeBeforeDelete - 1)
    }

    @Test
    fun equalsVerifier() {
        equalsVerifier(Benefit::class)
        val benefit1 = Benefit()
        benefit1.id = "id1"
        val benefit2 = Benefit()
        benefit2.id = benefit1.id
        assertThat(benefit1).isEqualTo(benefit2)
        benefit2.id = "id2"
        assertThat(benefit1).isNotEqualTo(benefit2)
        benefit1.id = null
        assertThat(benefit1).isNotEqualTo(benefit2)
    }

    companion object {

        private const val DEFAULT_DESCRIPTION: String = "AAAAAAAAAA"
        private const val UPDATED_DESCRIPTION = "BBBBBBBBBB"

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(): Benefit {
            val benefit = Benefit(
                description = DEFAULT_DESCRIPTION
            )

            return benefit
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(): Benefit {
            val benefit = Benefit(
                description = UPDATED_DESCRIPTION
            )

            return benefit
        }
    }
}
