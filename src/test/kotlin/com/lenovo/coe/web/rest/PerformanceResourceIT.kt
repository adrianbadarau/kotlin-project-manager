package com.lenovo.coe.web.rest

import com.lenovo.coe.PmAppApp
import com.lenovo.coe.domain.Performance
import com.lenovo.coe.repository.PerformanceRepository
import com.lenovo.coe.service.PerformanceService
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
 * Test class for the PerformanceResource REST controller.
 *
 * @see PerformanceResource
 */
@SpringBootTest(classes = [PmAppApp::class])
class PerformanceResourceIT {

    @Autowired
    private lateinit var performanceRepository: PerformanceRepository

    @Autowired
    private lateinit var performanceService: PerformanceService

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var validator: Validator

    private lateinit var restPerformanceMockMvc: MockMvc

    private lateinit var performance: Performance

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val performanceResource = PerformanceResource(performanceService)
        this.restPerformanceMockMvc = MockMvcBuilders.standaloneSetup(performanceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        performanceRepository.deleteAll()
        performance = createEntity()
    }

    @Test
    fun createPerformance() {
        val databaseSizeBeforeCreate = performanceRepository.findAll().size

        // Create the Performance
        restPerformanceMockMvc.perform(
            post("/api/performances")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(performance))
        ).andExpect(status().isCreated)

        // Validate the Performance in the database
        val performanceList = performanceRepository.findAll()
        assertThat(performanceList).hasSize(databaseSizeBeforeCreate + 1)
        val testPerformance = performanceList[performanceList.size - 1]
        assertThat(testPerformance.timelinePerformance).isEqualTo(DEFAULT_TIMELINE_PERFORMANCE)
        assertThat(testPerformance.riskRegister).isEqualTo(DEFAULT_RISK_REGISTER)
        assertThat(testPerformance.mitigationPlan).isEqualTo(DEFAULT_MITIGATION_PLAN)
    }

    @Test
    fun createPerformanceWithExistingId() {
        val databaseSizeBeforeCreate = performanceRepository.findAll().size

        // Create the Performance with an existing ID
        performance.id = "existing_id"

        // An entity with an existing ID cannot be created, so this API call must fail
        restPerformanceMockMvc.perform(
            post("/api/performances")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(performance))
        ).andExpect(status().isBadRequest)

        // Validate the Performance in the database
        val performanceList = performanceRepository.findAll()
        assertThat(performanceList).hasSize(databaseSizeBeforeCreate)
    }


    @Test
    fun getAllPerformances() {
        // Initialize the database
        performanceRepository.save(performance)

        // Get all the performanceList
        restPerformanceMockMvc.perform(get("/api/performances?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(performance.id)))
            .andExpect(jsonPath("$.[*].timelinePerformance").value(hasItem(DEFAULT_TIMELINE_PERFORMANCE.toDouble())))
            .andExpect(jsonPath("$.[*].riskRegister").value(hasItem(DEFAULT_RISK_REGISTER)))
            .andExpect(jsonPath("$.[*].mitigationPlan").value(hasItem(DEFAULT_MITIGATION_PLAN)))
    }
    
    @Test
    fun getPerformance() {
        // Initialize the database
        performanceRepository.save(performance)

        val id = performance.id
        assertNotNull(id)

        // Get the performance
        restPerformanceMockMvc.perform(get("/api/performances/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.timelinePerformance").value(DEFAULT_TIMELINE_PERFORMANCE.toDouble()))
            .andExpect(jsonPath("$.riskRegister").value(DEFAULT_RISK_REGISTER))
            .andExpect(jsonPath("$.mitigationPlan").value(DEFAULT_MITIGATION_PLAN))
    }

    @Test
    fun getNonExistingPerformance() {
        // Get the performance
        restPerformanceMockMvc.perform(get("/api/performances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    fun updatePerformance() {
        // Initialize the database
        performanceService.save(performance)

        val databaseSizeBeforeUpdate = performanceRepository.findAll().size

        // Update the performance
        val id = performance.id
        assertNotNull(id)
        val updatedPerformance = performanceRepository.findById(id).get()
        updatedPerformance.timelinePerformance = UPDATED_TIMELINE_PERFORMANCE
        updatedPerformance.riskRegister = UPDATED_RISK_REGISTER
        updatedPerformance.mitigationPlan = UPDATED_MITIGATION_PLAN

        restPerformanceMockMvc.perform(
            put("/api/performances")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(updatedPerformance))
        ).andExpect(status().isOk)

        // Validate the Performance in the database
        val performanceList = performanceRepository.findAll()
        assertThat(performanceList).hasSize(databaseSizeBeforeUpdate)
        val testPerformance = performanceList[performanceList.size - 1]
        assertThat(testPerformance.timelinePerformance).isEqualTo(UPDATED_TIMELINE_PERFORMANCE)
        assertThat(testPerformance.riskRegister).isEqualTo(UPDATED_RISK_REGISTER)
        assertThat(testPerformance.mitigationPlan).isEqualTo(UPDATED_MITIGATION_PLAN)
    }

    @Test
    fun updateNonExistingPerformance() {
        val databaseSizeBeforeUpdate = performanceRepository.findAll().size

        // Create the Performance

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPerformanceMockMvc.perform(
            put("/api/performances")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(performance))
        ).andExpect(status().isBadRequest)

        // Validate the Performance in the database
        val performanceList = performanceRepository.findAll()
        assertThat(performanceList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    fun deletePerformance() {
        // Initialize the database
        performanceService.save(performance)

        val databaseSizeBeforeDelete = performanceRepository.findAll().size

        val id = performance.id
        assertNotNull(id)

        // Delete the performance
        restPerformanceMockMvc.perform(
            delete("/api/performances/{id}", id)
                .accept(APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val performanceList = performanceRepository.findAll()
        assertThat(performanceList).hasSize(databaseSizeBeforeDelete - 1)
    }

    @Test
    fun equalsVerifier() {
        equalsVerifier(Performance::class)
        val performance1 = Performance()
        performance1.id = "id1"
        val performance2 = Performance()
        performance2.id = performance1.id
        assertThat(performance1).isEqualTo(performance2)
        performance2.id = "id2"
        assertThat(performance1).isNotEqualTo(performance2)
        performance1.id = null
        assertThat(performance1).isNotEqualTo(performance2)
    }

    companion object {

        private const val DEFAULT_TIMELINE_PERFORMANCE: Float = 1F
        private const val UPDATED_TIMELINE_PERFORMANCE: Float = 2F

        private const val DEFAULT_RISK_REGISTER: String = "AAAAAAAAAA"
        private const val UPDATED_RISK_REGISTER = "BBBBBBBBBB"

        private const val DEFAULT_MITIGATION_PLAN: String = "AAAAAAAAAA"
        private const val UPDATED_MITIGATION_PLAN = "BBBBBBBBBB"

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(): Performance {
            val performance = Performance(
                timelinePerformance = DEFAULT_TIMELINE_PERFORMANCE,
                riskRegister = DEFAULT_RISK_REGISTER,
                mitigationPlan = DEFAULT_MITIGATION_PLAN
            )

            return performance
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(): Performance {
            val performance = Performance(
                timelinePerformance = UPDATED_TIMELINE_PERFORMANCE,
                riskRegister = UPDATED_RISK_REGISTER,
                mitigationPlan = UPDATED_MITIGATION_PLAN
            )

            return performance
        }
    }
}
