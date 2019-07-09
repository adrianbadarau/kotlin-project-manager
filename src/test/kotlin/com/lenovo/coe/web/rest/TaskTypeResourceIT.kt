package com.lenovo.coe.web.rest

import com.lenovo.coe.PmAppApp
import com.lenovo.coe.domain.TaskType
import com.lenovo.coe.repository.TaskTypeRepository
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
 * Test class for the TaskTypeResource REST controller.
 *
 * @see TaskTypeResource
 */
@SpringBootTest(classes = [PmAppApp::class])
class TaskTypeResourceIT {

    @Autowired
    private lateinit var taskTypeRepository: TaskTypeRepository

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var validator: Validator

    private lateinit var restTaskTypeMockMvc: MockMvc

    private lateinit var taskType: TaskType

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val taskTypeResource = TaskTypeResource(taskTypeRepository)
        this.restTaskTypeMockMvc = MockMvcBuilders.standaloneSetup(taskTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        taskTypeRepository.deleteAll()
        taskType = createEntity()
    }

    @Test
    fun createTaskType() {
        val databaseSizeBeforeCreate = taskTypeRepository.findAll().size

        // Create the TaskType
        restTaskTypeMockMvc.perform(
            post("/api/task-types")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(taskType))
        ).andExpect(status().isCreated)

        // Validate the TaskType in the database
        val taskTypeList = taskTypeRepository.findAll()
        assertThat(taskTypeList).hasSize(databaseSizeBeforeCreate + 1)
        val testTaskType = taskTypeList[taskTypeList.size - 1]
        assertThat(testTaskType.name).isEqualTo(DEFAULT_NAME)
    }

    @Test
    fun createTaskTypeWithExistingId() {
        val databaseSizeBeforeCreate = taskTypeRepository.findAll().size

        // Create the TaskType with an existing ID
        taskType.id = "existing_id"

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskTypeMockMvc.perform(
            post("/api/task-types")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(taskType))
        ).andExpect(status().isBadRequest)

        // Validate the TaskType in the database
        val taskTypeList = taskTypeRepository.findAll()
        assertThat(taskTypeList).hasSize(databaseSizeBeforeCreate)
    }


    @Test
    fun checkNameIsRequired() {
        val databaseSizeBeforeTest = taskTypeRepository.findAll().size
        // set the field null
        taskType.name = null

        // Create the TaskType, which fails.

        restTaskTypeMockMvc.perform(
            post("/api/task-types")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(taskType))
        ).andExpect(status().isBadRequest)

        val taskTypeList = taskTypeRepository.findAll()
        assertThat(taskTypeList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    fun getAllTaskTypes() {
        // Initialize the database
        taskTypeRepository.save(taskType)

        // Get all the taskTypeList
        restTaskTypeMockMvc.perform(get("/api/task-types?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskType.id)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
    }
    
    @Test
    fun getTaskType() {
        // Initialize the database
        taskTypeRepository.save(taskType)

        val id = taskType.id
        assertNotNull(id)

        // Get the taskType
        restTaskTypeMockMvc.perform(get("/api/task-types/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
    }

    @Test
    fun getNonExistingTaskType() {
        // Get the taskType
        restTaskTypeMockMvc.perform(get("/api/task-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    fun updateTaskType() {
        // Initialize the database
        taskTypeRepository.save(taskType)

        val databaseSizeBeforeUpdate = taskTypeRepository.findAll().size

        // Update the taskType
        val id = taskType.id
        assertNotNull(id)
        val updatedTaskType = taskTypeRepository.findById(id).get()
        updatedTaskType.name = UPDATED_NAME

        restTaskTypeMockMvc.perform(
            put("/api/task-types")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(updatedTaskType))
        ).andExpect(status().isOk)

        // Validate the TaskType in the database
        val taskTypeList = taskTypeRepository.findAll()
        assertThat(taskTypeList).hasSize(databaseSizeBeforeUpdate)
        val testTaskType = taskTypeList[taskTypeList.size - 1]
        assertThat(testTaskType.name).isEqualTo(UPDATED_NAME)
    }

    @Test
    fun updateNonExistingTaskType() {
        val databaseSizeBeforeUpdate = taskTypeRepository.findAll().size

        // Create the TaskType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskTypeMockMvc.perform(
            put("/api/task-types")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(taskType))
        ).andExpect(status().isBadRequest)

        // Validate the TaskType in the database
        val taskTypeList = taskTypeRepository.findAll()
        assertThat(taskTypeList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    fun deleteTaskType() {
        // Initialize the database
        taskTypeRepository.save(taskType)

        val databaseSizeBeforeDelete = taskTypeRepository.findAll().size

        val id = taskType.id
        assertNotNull(id)

        // Delete the taskType
        restTaskTypeMockMvc.perform(
            delete("/api/task-types/{id}", id)
                .accept(APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val taskTypeList = taskTypeRepository.findAll()
        assertThat(taskTypeList).hasSize(databaseSizeBeforeDelete - 1)
    }

    @Test
    fun equalsVerifier() {
        equalsVerifier(TaskType::class)
        val taskType1 = TaskType()
        taskType1.id = "id1"
        val taskType2 = TaskType()
        taskType2.id = taskType1.id
        assertThat(taskType1).isEqualTo(taskType2)
        taskType2.id = "id2"
        assertThat(taskType1).isNotEqualTo(taskType2)
        taskType1.id = null
        assertThat(taskType1).isNotEqualTo(taskType2)
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
        fun createEntity(): TaskType {
            val taskType = TaskType(
                name = DEFAULT_NAME
            )

            return taskType
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(): TaskType {
            val taskType = TaskType(
                name = UPDATED_NAME
            )

            return taskType
        }
    }
}
