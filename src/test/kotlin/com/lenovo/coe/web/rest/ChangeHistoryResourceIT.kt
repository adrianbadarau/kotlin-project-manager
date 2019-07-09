package com.lenovo.coe.web.rest

import com.lenovo.coe.PmAppApp
import com.lenovo.coe.domain.ChangeHistory
import com.lenovo.coe.repository.ChangeHistoryRepository
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
 * Test class for the ChangeHistoryResource REST controller.
 *
 * @see ChangeHistoryResource
 */
@SpringBootTest(classes = [PmAppApp::class])
class ChangeHistoryResourceIT {

    @Autowired
    private lateinit var changeHistoryRepository: ChangeHistoryRepository

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var validator: Validator

    private lateinit var restChangeHistoryMockMvc: MockMvc

    private lateinit var changeHistory: ChangeHistory

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val changeHistoryResource = ChangeHistoryResource(changeHistoryRepository)
        this.restChangeHistoryMockMvc = MockMvcBuilders.standaloneSetup(changeHistoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        changeHistoryRepository.deleteAll()
        changeHistory = createEntity()
    }

    @Test
    fun createChangeHistory() {
        val databaseSizeBeforeCreate = changeHistoryRepository.findAll().size

        // Create the ChangeHistory
        restChangeHistoryMockMvc.perform(
            post("/api/change-histories")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(changeHistory))
        ).andExpect(status().isCreated)

        // Validate the ChangeHistory in the database
        val changeHistoryList = changeHistoryRepository.findAll()
        assertThat(changeHistoryList).hasSize(databaseSizeBeforeCreate + 1)
        val testChangeHistory = changeHistoryList[changeHistoryList.size - 1]
        assertThat(testChangeHistory.subject).isEqualTo(DEFAULT_SUBJECT)
        assertThat(testChangeHistory.fromValue).isEqualTo(DEFAULT_FROM_VALUE)
        assertThat(testChangeHistory.toValue).isEqualTo(DEFAULT_TO_VALUE)
    }

    @Test
    fun createChangeHistoryWithExistingId() {
        val databaseSizeBeforeCreate = changeHistoryRepository.findAll().size

        // Create the ChangeHistory with an existing ID
        changeHistory.id = "existing_id"

        // An entity with an existing ID cannot be created, so this API call must fail
        restChangeHistoryMockMvc.perform(
            post("/api/change-histories")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(changeHistory))
        ).andExpect(status().isBadRequest)

        // Validate the ChangeHistory in the database
        val changeHistoryList = changeHistoryRepository.findAll()
        assertThat(changeHistoryList).hasSize(databaseSizeBeforeCreate)
    }


    @Test
    fun checkSubjectIsRequired() {
        val databaseSizeBeforeTest = changeHistoryRepository.findAll().size
        // set the field null
        changeHistory.subject = null

        // Create the ChangeHistory, which fails.

        restChangeHistoryMockMvc.perform(
            post("/api/change-histories")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(changeHistory))
        ).andExpect(status().isBadRequest)

        val changeHistoryList = changeHistoryRepository.findAll()
        assertThat(changeHistoryList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    fun checkToValueIsRequired() {
        val databaseSizeBeforeTest = changeHistoryRepository.findAll().size
        // set the field null
        changeHistory.toValue = null

        // Create the ChangeHistory, which fails.

        restChangeHistoryMockMvc.perform(
            post("/api/change-histories")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(changeHistory))
        ).andExpect(status().isBadRequest)

        val changeHistoryList = changeHistoryRepository.findAll()
        assertThat(changeHistoryList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    fun getAllChangeHistories() {
        // Initialize the database
        changeHistoryRepository.save(changeHistory)

        // Get all the changeHistoryList
        restChangeHistoryMockMvc.perform(get("/api/change-histories?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(changeHistory.id)))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].fromValue").value(hasItem(DEFAULT_FROM_VALUE)))
            .andExpect(jsonPath("$.[*].toValue").value(hasItem(DEFAULT_TO_VALUE)))
    }
    
    @Test
    fun getChangeHistory() {
        // Initialize the database
        changeHistoryRepository.save(changeHistory)

        val id = changeHistory.id
        assertNotNull(id)

        // Get the changeHistory
        restChangeHistoryMockMvc.perform(get("/api/change-histories/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT))
            .andExpect(jsonPath("$.fromValue").value(DEFAULT_FROM_VALUE))
            .andExpect(jsonPath("$.toValue").value(DEFAULT_TO_VALUE))
    }

    @Test
    fun getNonExistingChangeHistory() {
        // Get the changeHistory
        restChangeHistoryMockMvc.perform(get("/api/change-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    fun updateChangeHistory() {
        // Initialize the database
        changeHistoryRepository.save(changeHistory)

        val databaseSizeBeforeUpdate = changeHistoryRepository.findAll().size

        // Update the changeHistory
        val id = changeHistory.id
        assertNotNull(id)
        val updatedChangeHistory = changeHistoryRepository.findById(id).get()
        updatedChangeHistory.subject = UPDATED_SUBJECT
        updatedChangeHistory.fromValue = UPDATED_FROM_VALUE
        updatedChangeHistory.toValue = UPDATED_TO_VALUE

        restChangeHistoryMockMvc.perform(
            put("/api/change-histories")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(updatedChangeHistory))
        ).andExpect(status().isOk)

        // Validate the ChangeHistory in the database
        val changeHistoryList = changeHistoryRepository.findAll()
        assertThat(changeHistoryList).hasSize(databaseSizeBeforeUpdate)
        val testChangeHistory = changeHistoryList[changeHistoryList.size - 1]
        assertThat(testChangeHistory.subject).isEqualTo(UPDATED_SUBJECT)
        assertThat(testChangeHistory.fromValue).isEqualTo(UPDATED_FROM_VALUE)
        assertThat(testChangeHistory.toValue).isEqualTo(UPDATED_TO_VALUE)
    }

    @Test
    fun updateNonExistingChangeHistory() {
        val databaseSizeBeforeUpdate = changeHistoryRepository.findAll().size

        // Create the ChangeHistory

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChangeHistoryMockMvc.perform(
            put("/api/change-histories")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(changeHistory))
        ).andExpect(status().isBadRequest)

        // Validate the ChangeHistory in the database
        val changeHistoryList = changeHistoryRepository.findAll()
        assertThat(changeHistoryList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    fun deleteChangeHistory() {
        // Initialize the database
        changeHistoryRepository.save(changeHistory)

        val databaseSizeBeforeDelete = changeHistoryRepository.findAll().size

        val id = changeHistory.id
        assertNotNull(id)

        // Delete the changeHistory
        restChangeHistoryMockMvc.perform(
            delete("/api/change-histories/{id}", id)
                .accept(APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val changeHistoryList = changeHistoryRepository.findAll()
        assertThat(changeHistoryList).hasSize(databaseSizeBeforeDelete - 1)
    }

    @Test
    fun equalsVerifier() {
        equalsVerifier(ChangeHistory::class)
        val changeHistory1 = ChangeHistory()
        changeHistory1.id = "id1"
        val changeHistory2 = ChangeHistory()
        changeHistory2.id = changeHistory1.id
        assertThat(changeHistory1).isEqualTo(changeHistory2)
        changeHistory2.id = "id2"
        assertThat(changeHistory1).isNotEqualTo(changeHistory2)
        changeHistory1.id = null
        assertThat(changeHistory1).isNotEqualTo(changeHistory2)
    }

    companion object {

        private const val DEFAULT_SUBJECT: String = "AAAAAAAAAA"
        private const val UPDATED_SUBJECT = "BBBBBBBBBB"

        private const val DEFAULT_FROM_VALUE: String = "AAAAAAAAAA"
        private const val UPDATED_FROM_VALUE = "BBBBBBBBBB"

        private const val DEFAULT_TO_VALUE: String = "AAAAAAAAAA"
        private const val UPDATED_TO_VALUE = "BBBBBBBBBB"

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(): ChangeHistory {
            val changeHistory = ChangeHistory(
                subject = DEFAULT_SUBJECT,
                fromValue = DEFAULT_FROM_VALUE,
                toValue = DEFAULT_TO_VALUE
            )

            return changeHistory
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(): ChangeHistory {
            val changeHistory = ChangeHistory(
                subject = UPDATED_SUBJECT,
                fromValue = UPDATED_FROM_VALUE,
                toValue = UPDATED_TO_VALUE
            )

            return changeHistory
        }
    }
}
