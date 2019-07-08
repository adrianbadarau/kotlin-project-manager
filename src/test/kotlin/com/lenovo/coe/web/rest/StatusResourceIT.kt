package com.lenovo.coe.web.rest

import com.lenovo.coe.PmAppApp
import com.lenovo.coe.domain.Status
import com.lenovo.coe.repository.StatusRepository
import com.lenovo.coe.service.StatusService
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
 * Test class for the StatusResource REST controller.
 *
 * @see StatusResource
 */
@SpringBootTest(classes = [PmAppApp::class])
class StatusResourceIT {

    @Autowired
    private lateinit var statusRepository: StatusRepository

    @Autowired
    private lateinit var statusService: StatusService

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var validator: Validator

    private lateinit var restStatusMockMvc: MockMvc

    private lateinit var status: Status

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val statusResource = StatusResource(statusService)
        this.restStatusMockMvc = MockMvcBuilders.standaloneSetup(statusResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        statusRepository.deleteAll()
        status = createEntity()
    }

    @Test
    fun createStatus() {
        val databaseSizeBeforeCreate = statusRepository.findAll().size

        // Create the Status
        restStatusMockMvc.perform(
            post("/api/statuses")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(status))
        ).andExpect(status().isCreated)

        // Validate the Status in the database
        val statusList = statusRepository.findAll()
        assertThat(statusList).hasSize(databaseSizeBeforeCreate + 1)
        val testStatus = statusList[statusList.size - 1]
        assertThat(testStatus.name).isEqualTo(DEFAULT_NAME)
    }

    @Test
    fun createStatusWithExistingId() {
        val databaseSizeBeforeCreate = statusRepository.findAll().size

        // Create the Status with an existing ID
        status.id = "existing_id"

        // An entity with an existing ID cannot be created, so this API call must fail
        restStatusMockMvc.perform(
            post("/api/statuses")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(status))
        ).andExpect(status().isBadRequest)

        // Validate the Status in the database
        val statusList = statusRepository.findAll()
        assertThat(statusList).hasSize(databaseSizeBeforeCreate)
    }


    @Test
    fun checkNameIsRequired() {
        val databaseSizeBeforeTest = statusRepository.findAll().size
        // set the field null
        status.name = null

        // Create the Status, which fails.

        restStatusMockMvc.perform(
            post("/api/statuses")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(status))
        ).andExpect(status().isBadRequest)

        val statusList = statusRepository.findAll()
        assertThat(statusList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    fun getAllStatuses() {
        // Initialize the database
        statusRepository.save(status)

        // Get all the statusList
        restStatusMockMvc.perform(get("/api/statuses?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(status.id)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
    }
    
    @Test
    fun getStatus() {
        // Initialize the database
        statusRepository.save(status)

        val id = status.id
        assertNotNull(id)

        // Get the status
        restStatusMockMvc.perform(get("/api/statuses/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
    }

    @Test
    fun getNonExistingStatus() {
        // Get the status
        restStatusMockMvc.perform(get("/api/statuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    fun updateStatus() {
        // Initialize the database
        statusService.save(status)

        val databaseSizeBeforeUpdate = statusRepository.findAll().size

        // Update the status
        val id = status.id
        assertNotNull(id)
        val updatedStatus = statusRepository.findById(id).get()
        updatedStatus.name = UPDATED_NAME

        restStatusMockMvc.perform(
            put("/api/statuses")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(updatedStatus))
        ).andExpect(status().isOk)

        // Validate the Status in the database
        val statusList = statusRepository.findAll()
        assertThat(statusList).hasSize(databaseSizeBeforeUpdate)
        val testStatus = statusList[statusList.size - 1]
        assertThat(testStatus.name).isEqualTo(UPDATED_NAME)
    }

    @Test
    fun updateNonExistingStatus() {
        val databaseSizeBeforeUpdate = statusRepository.findAll().size

        // Create the Status

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStatusMockMvc.perform(
            put("/api/statuses")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(status))
        ).andExpect(status().isBadRequest)

        // Validate the Status in the database
        val statusList = statusRepository.findAll()
        assertThat(statusList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    fun deleteStatus() {
        // Initialize the database
        statusService.save(status)

        val databaseSizeBeforeDelete = statusRepository.findAll().size

        val id = status.id
        assertNotNull(id)

        // Delete the status
        restStatusMockMvc.perform(
            delete("/api/statuses/{id}", id)
                .accept(APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val statusList = statusRepository.findAll()
        assertThat(statusList).hasSize(databaseSizeBeforeDelete - 1)
    }

    @Test
    fun equalsVerifier() {
        equalsVerifier(Status::class)
        val status1 = Status()
        status1.id = "id1"
        val status2 = Status()
        status2.id = status1.id
        assertThat(status1).isEqualTo(status2)
        status2.id = "id2"
        assertThat(status1).isNotEqualTo(status2)
        status1.id = null
        assertThat(status1).isNotEqualTo(status2)
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
        fun createEntity(): Status {
            val status = Status(
                name = DEFAULT_NAME
            )

            return status
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(): Status {
            val status = Status(
                name = UPDATED_NAME
            )

            return status
        }
    }
}
