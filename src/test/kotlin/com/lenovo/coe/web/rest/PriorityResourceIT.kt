package com.lenovo.coe.web.rest

import com.lenovo.coe.PmAppApp
import com.lenovo.coe.domain.Priority
import com.lenovo.coe.repository.PriorityRepository
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
 * Test class for the PriorityResource REST controller.
 *
 * @see PriorityResource
 */
@SpringBootTest(classes = [PmAppApp::class])
class PriorityResourceIT {

    @Autowired
    private lateinit var priorityRepository: PriorityRepository

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var validator: Validator

    private lateinit var restPriorityMockMvc: MockMvc

    private lateinit var priority: Priority

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val priorityResource = PriorityResource(priorityRepository)
        this.restPriorityMockMvc = MockMvcBuilders.standaloneSetup(priorityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        priorityRepository.deleteAll()
        priority = createEntity()
    }

    @Test
    fun createPriority() {
        val databaseSizeBeforeCreate = priorityRepository.findAll().size

        // Create the Priority
        restPriorityMockMvc.perform(
            post("/api/priorities")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(priority))
        ).andExpect(status().isCreated)

        // Validate the Priority in the database
        val priorityList = priorityRepository.findAll()
        assertThat(priorityList).hasSize(databaseSizeBeforeCreate + 1)
        val testPriority = priorityList[priorityList.size - 1]
        assertThat(testPriority.name).isEqualTo(DEFAULT_NAME)
    }

    @Test
    fun createPriorityWithExistingId() {
        val databaseSizeBeforeCreate = priorityRepository.findAll().size

        // Create the Priority with an existing ID
        priority.id = "existing_id"

        // An entity with an existing ID cannot be created, so this API call must fail
        restPriorityMockMvc.perform(
            post("/api/priorities")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(priority))
        ).andExpect(status().isBadRequest)

        // Validate the Priority in the database
        val priorityList = priorityRepository.findAll()
        assertThat(priorityList).hasSize(databaseSizeBeforeCreate)
    }


    @Test
    fun checkNameIsRequired() {
        val databaseSizeBeforeTest = priorityRepository.findAll().size
        // set the field null
        priority.name = null

        // Create the Priority, which fails.

        restPriorityMockMvc.perform(
            post("/api/priorities")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(priority))
        ).andExpect(status().isBadRequest)

        val priorityList = priorityRepository.findAll()
        assertThat(priorityList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    fun getAllPriorities() {
        // Initialize the database
        priorityRepository.save(priority)

        // Get all the priorityList
        restPriorityMockMvc.perform(get("/api/priorities?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(priority.id)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
    }
    
    @Test
    fun getPriority() {
        // Initialize the database
        priorityRepository.save(priority)

        val id = priority.id
        assertNotNull(id)

        // Get the priority
        restPriorityMockMvc.perform(get("/api/priorities/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
    }

    @Test
    fun getNonExistingPriority() {
        // Get the priority
        restPriorityMockMvc.perform(get("/api/priorities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    fun updatePriority() {
        // Initialize the database
        priorityRepository.save(priority)

        val databaseSizeBeforeUpdate = priorityRepository.findAll().size

        // Update the priority
        val id = priority.id
        assertNotNull(id)
        val updatedPriority = priorityRepository.findById(id).get()
        updatedPriority.name = UPDATED_NAME

        restPriorityMockMvc.perform(
            put("/api/priorities")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(updatedPriority))
        ).andExpect(status().isOk)

        // Validate the Priority in the database
        val priorityList = priorityRepository.findAll()
        assertThat(priorityList).hasSize(databaseSizeBeforeUpdate)
        val testPriority = priorityList[priorityList.size - 1]
        assertThat(testPriority.name).isEqualTo(UPDATED_NAME)
    }

    @Test
    fun updateNonExistingPriority() {
        val databaseSizeBeforeUpdate = priorityRepository.findAll().size

        // Create the Priority

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPriorityMockMvc.perform(
            put("/api/priorities")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(priority))
        ).andExpect(status().isBadRequest)

        // Validate the Priority in the database
        val priorityList = priorityRepository.findAll()
        assertThat(priorityList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    fun deletePriority() {
        // Initialize the database
        priorityRepository.save(priority)

        val databaseSizeBeforeDelete = priorityRepository.findAll().size

        val id = priority.id
        assertNotNull(id)

        // Delete the priority
        restPriorityMockMvc.perform(
            delete("/api/priorities/{id}", id)
                .accept(APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val priorityList = priorityRepository.findAll()
        assertThat(priorityList).hasSize(databaseSizeBeforeDelete - 1)
    }

    @Test
    fun equalsVerifier() {
        equalsVerifier(Priority::class)
        val priority1 = Priority()
        priority1.id = "id1"
        val priority2 = Priority()
        priority2.id = priority1.id
        assertThat(priority1).isEqualTo(priority2)
        priority2.id = "id2"
        assertThat(priority1).isNotEqualTo(priority2)
        priority1.id = null
        assertThat(priority1).isNotEqualTo(priority2)
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
        fun createEntity(): Priority {
            val priority = Priority(
                name = DEFAULT_NAME
            )

            return priority
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(): Priority {
            val priority = Priority(
                name = UPDATED_NAME
            )

            return priority
        }
    }
}
