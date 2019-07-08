package com.lenovo.coe.web.rest

import com.lenovo.coe.PmAppApp
import com.lenovo.coe.domain.Delivrable
import com.lenovo.coe.repository.DelivrableRepository
import com.lenovo.coe.service.DelivrableService
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

import java.time.Instant
import java.time.temporal.ChronoUnit

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
 * Test class for the DelivrableResource REST controller.
 *
 * @see DelivrableResource
 */
@SpringBootTest(classes = [PmAppApp::class])
class DelivrableResourceIT {

    @Autowired
    private lateinit var delivrableRepository: DelivrableRepository

    @Mock
    private lateinit var delivrableRepositoryMock: DelivrableRepository

    @Mock
    private lateinit var delivrableServiceMock: DelivrableService

    @Autowired
    private lateinit var delivrableService: DelivrableService

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var validator: Validator

    private lateinit var restDelivrableMockMvc: MockMvc

    private lateinit var delivrable: Delivrable

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val delivrableResource = DelivrableResource(delivrableService)
        this.restDelivrableMockMvc = MockMvcBuilders.standaloneSetup(delivrableResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        delivrableRepository.deleteAll()
        delivrable = createEntity()
    }

    @Test
    fun createDelivrable() {
        val databaseSizeBeforeCreate = delivrableRepository.findAll().size

        // Create the Delivrable
        restDelivrableMockMvc.perform(
            post("/api/delivrables")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(delivrable))
        ).andExpect(status().isCreated)

        // Validate the Delivrable in the database
        val delivrableList = delivrableRepository.findAll()
        assertThat(delivrableList).hasSize(databaseSizeBeforeCreate + 1)
        val testDelivrable = delivrableList[delivrableList.size - 1]
        assertThat(testDelivrable.name).isEqualTo(DEFAULT_NAME)
        assertThat(testDelivrable.description).isEqualTo(DEFAULT_DESCRIPTION)
        assertThat(testDelivrable.target).isEqualTo(DEFAULT_TARGET)
    }

    @Test
    fun createDelivrableWithExistingId() {
        val databaseSizeBeforeCreate = delivrableRepository.findAll().size

        // Create the Delivrable with an existing ID
        delivrable.id = "existing_id"

        // An entity with an existing ID cannot be created, so this API call must fail
        restDelivrableMockMvc.perform(
            post("/api/delivrables")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(delivrable))
        ).andExpect(status().isBadRequest)

        // Validate the Delivrable in the database
        val delivrableList = delivrableRepository.findAll()
        assertThat(delivrableList).hasSize(databaseSizeBeforeCreate)
    }


    @Test
    fun checkNameIsRequired() {
        val databaseSizeBeforeTest = delivrableRepository.findAll().size
        // set the field null
        delivrable.name = null

        // Create the Delivrable, which fails.

        restDelivrableMockMvc.perform(
            post("/api/delivrables")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(delivrable))
        ).andExpect(status().isBadRequest)

        val delivrableList = delivrableRepository.findAll()
        assertThat(delivrableList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    fun checkDescriptionIsRequired() {
        val databaseSizeBeforeTest = delivrableRepository.findAll().size
        // set the field null
        delivrable.description = null

        // Create the Delivrable, which fails.

        restDelivrableMockMvc.perform(
            post("/api/delivrables")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(delivrable))
        ).andExpect(status().isBadRequest)

        val delivrableList = delivrableRepository.findAll()
        assertThat(delivrableList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    fun checkTargetIsRequired() {
        val databaseSizeBeforeTest = delivrableRepository.findAll().size
        // set the field null
        delivrable.target = null

        // Create the Delivrable, which fails.

        restDelivrableMockMvc.perform(
            post("/api/delivrables")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(delivrable))
        ).andExpect(status().isBadRequest)

        val delivrableList = delivrableRepository.findAll()
        assertThat(delivrableList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    fun getAllDelivrables() {
        // Initialize the database
        delivrableRepository.save(delivrable)

        // Get all the delivrableList
        restDelivrableMockMvc.perform(get("/api/delivrables?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(delivrable.id)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].target").value(hasItem(DEFAULT_TARGET.toString())))
    }
    
    @Suppress("unchecked")
    fun getAllDelivrablesWithEagerRelationshipsIsEnabled() {
        val delivrableResource = DelivrableResource(delivrableServiceMock)
        `when`(delivrableServiceMock.findAllWithEagerRelationships(any())).thenReturn(PageImpl(mutableListOf()))

        val restDelivrableMockMvc = MockMvcBuilders.standaloneSetup(delivrableResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build()

        restDelivrableMockMvc.perform(get("/api/delivrables?eagerload=true"))
            .andExpect(status().isOk)

        verify(delivrableServiceMock, times(1)).findAllWithEagerRelationships(any())
    }

    @Suppress("unchecked")
    fun getAllDelivrablesWithEagerRelationshipsIsNotEnabled() {
        val delivrableResource = DelivrableResource(delivrableServiceMock)
            `when`(delivrableServiceMock.findAllWithEagerRelationships(any())).thenReturn( PageImpl( mutableListOf()))
        val restDelivrableMockMvc = MockMvcBuilders.standaloneSetup(delivrableResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build()

        restDelivrableMockMvc.perform(get("/api/delivrables?eagerload=true"))
            .andExpect(status().isOk)

        verify(delivrableServiceMock, times(1)).findAllWithEagerRelationships(any())
    }

    @Test
    fun getDelivrable() {
        // Initialize the database
        delivrableRepository.save(delivrable)

        val id = delivrable.id
        assertNotNull(id)

        // Get the delivrable
        restDelivrableMockMvc.perform(get("/api/delivrables/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.target").value(DEFAULT_TARGET.toString()))
    }

    @Test
    fun getNonExistingDelivrable() {
        // Get the delivrable
        restDelivrableMockMvc.perform(get("/api/delivrables/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    fun updateDelivrable() {
        // Initialize the database
        delivrableService.save(delivrable)

        val databaseSizeBeforeUpdate = delivrableRepository.findAll().size

        // Update the delivrable
        val id = delivrable.id
        assertNotNull(id)
        val updatedDelivrable = delivrableRepository.findById(id).get()
        updatedDelivrable.name = UPDATED_NAME
        updatedDelivrable.description = UPDATED_DESCRIPTION
        updatedDelivrable.target = UPDATED_TARGET

        restDelivrableMockMvc.perform(
            put("/api/delivrables")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(updatedDelivrable))
        ).andExpect(status().isOk)

        // Validate the Delivrable in the database
        val delivrableList = delivrableRepository.findAll()
        assertThat(delivrableList).hasSize(databaseSizeBeforeUpdate)
        val testDelivrable = delivrableList[delivrableList.size - 1]
        assertThat(testDelivrable.name).isEqualTo(UPDATED_NAME)
        assertThat(testDelivrable.description).isEqualTo(UPDATED_DESCRIPTION)
        assertThat(testDelivrable.target).isEqualTo(UPDATED_TARGET)
    }

    @Test
    fun updateNonExistingDelivrable() {
        val databaseSizeBeforeUpdate = delivrableRepository.findAll().size

        // Create the Delivrable

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDelivrableMockMvc.perform(
            put("/api/delivrables")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(delivrable))
        ).andExpect(status().isBadRequest)

        // Validate the Delivrable in the database
        val delivrableList = delivrableRepository.findAll()
        assertThat(delivrableList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    fun deleteDelivrable() {
        // Initialize the database
        delivrableService.save(delivrable)

        val databaseSizeBeforeDelete = delivrableRepository.findAll().size

        val id = delivrable.id
        assertNotNull(id)

        // Delete the delivrable
        restDelivrableMockMvc.perform(
            delete("/api/delivrables/{id}", id)
                .accept(APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val delivrableList = delivrableRepository.findAll()
        assertThat(delivrableList).hasSize(databaseSizeBeforeDelete - 1)
    }

    @Test
    fun equalsVerifier() {
        equalsVerifier(Delivrable::class)
        val delivrable1 = Delivrable()
        delivrable1.id = "id1"
        val delivrable2 = Delivrable()
        delivrable2.id = delivrable1.id
        assertThat(delivrable1).isEqualTo(delivrable2)
        delivrable2.id = "id2"
        assertThat(delivrable1).isNotEqualTo(delivrable2)
        delivrable1.id = null
        assertThat(delivrable1).isNotEqualTo(delivrable2)
    }

    companion object {

        private const val DEFAULT_NAME: String = "AAAAAAAAAA"
        private const val UPDATED_NAME = "BBBBBBBBBB"

        private const val DEFAULT_DESCRIPTION: String = "AAAAAAAAAA"
        private const val UPDATED_DESCRIPTION = "BBBBBBBBBB"

        private val DEFAULT_TARGET: Instant = Instant.ofEpochMilli(0L)
        private val UPDATED_TARGET: Instant = Instant.now().truncatedTo(ChronoUnit.MILLIS)

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(): Delivrable {
            val delivrable = Delivrable(
                name = DEFAULT_NAME,
                description = DEFAULT_DESCRIPTION,
                target = DEFAULT_TARGET
            )

            return delivrable
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(): Delivrable {
            val delivrable = Delivrable(
                name = UPDATED_NAME,
                description = UPDATED_DESCRIPTION,
                target = UPDATED_TARGET
            )

            return delivrable
        }
    }
}
