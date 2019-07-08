package com.lenovo.coe.web.rest

import com.lenovo.coe.PmAppApp
import com.lenovo.coe.domain.Task
import com.lenovo.coe.repository.TaskRepository
import com.lenovo.coe.service.TaskService
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
 * Test class for the TaskResource REST controller.
 *
 * @see TaskResource
 */
@SpringBootTest(classes = [PmAppApp::class])
class TaskResourceIT {

    @Autowired
    private lateinit var taskRepository: TaskRepository

    @Mock
    private lateinit var taskRepositoryMock: TaskRepository

    @Mock
    private lateinit var taskServiceMock: TaskService

    @Autowired
    private lateinit var taskService: TaskService

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var validator: Validator

    private lateinit var restTaskMockMvc: MockMvc

    private lateinit var task: Task

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val taskResource = TaskResource(taskService)
        this.restTaskMockMvc = MockMvcBuilders.standaloneSetup(taskResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        taskRepository.deleteAll()
        task = createEntity()
    }

    @Test
    fun createTask() {
        val databaseSizeBeforeCreate = taskRepository.findAll().size

        // Create the Task
        restTaskMockMvc.perform(
            post("/api/tasks")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(task))
        ).andExpect(status().isCreated)

        // Validate the Task in the database
        val taskList = taskRepository.findAll()
        assertThat(taskList).hasSize(databaseSizeBeforeCreate + 1)
        val testTask = taskList[taskList.size - 1]
        assertThat(testTask.name).isEqualTo(DEFAULT_NAME)
        assertThat(testTask.estimatedDate).isEqualTo(DEFAULT_ESTIMATED_DATE)
        assertThat(testTask.details).isEqualTo(DEFAULT_DETAILS)
    }

    @Test
    fun createTaskWithExistingId() {
        val databaseSizeBeforeCreate = taskRepository.findAll().size

        // Create the Task with an existing ID
        task.id = "existing_id"

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskMockMvc.perform(
            post("/api/tasks")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(task))
        ).andExpect(status().isBadRequest)

        // Validate the Task in the database
        val taskList = taskRepository.findAll()
        assertThat(taskList).hasSize(databaseSizeBeforeCreate)
    }


    @Test
    fun checkNameIsRequired() {
        val databaseSizeBeforeTest = taskRepository.findAll().size
        // set the field null
        task.name = null

        // Create the Task, which fails.

        restTaskMockMvc.perform(
            post("/api/tasks")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(task))
        ).andExpect(status().isBadRequest)

        val taskList = taskRepository.findAll()
        assertThat(taskList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    fun getAllTasks() {
        // Initialize the database
        taskRepository.save(task)

        // Get all the taskList
        restTaskMockMvc.perform(get("/api/tasks?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(task.id)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].estimatedDate").value(hasItem(DEFAULT_ESTIMATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS)))
    }
    
    @Suppress("unchecked")
    fun getAllTasksWithEagerRelationshipsIsEnabled() {
        val taskResource = TaskResource(taskServiceMock)
        `when`(taskServiceMock.findAllWithEagerRelationships(any())).thenReturn(PageImpl(mutableListOf()))

        val restTaskMockMvc = MockMvcBuilders.standaloneSetup(taskResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build()

        restTaskMockMvc.perform(get("/api/tasks?eagerload=true"))
            .andExpect(status().isOk)

        verify(taskServiceMock, times(1)).findAllWithEagerRelationships(any())
    }

    @Suppress("unchecked")
    fun getAllTasksWithEagerRelationshipsIsNotEnabled() {
        val taskResource = TaskResource(taskServiceMock)
            `when`(taskServiceMock.findAllWithEagerRelationships(any())).thenReturn( PageImpl( mutableListOf()))
        val restTaskMockMvc = MockMvcBuilders.standaloneSetup(taskResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build()

        restTaskMockMvc.perform(get("/api/tasks?eagerload=true"))
            .andExpect(status().isOk)

        verify(taskServiceMock, times(1)).findAllWithEagerRelationships(any())
    }

    @Test
    fun getTask() {
        // Initialize the database
        taskRepository.save(task)

        val id = task.id
        assertNotNull(id)

        // Get the task
        restTaskMockMvc.perform(get("/api/tasks/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.estimatedDate").value(DEFAULT_ESTIMATED_DATE.toString()))
            .andExpect(jsonPath("$.details").value(DEFAULT_DETAILS))
    }

    @Test
    fun getNonExistingTask() {
        // Get the task
        restTaskMockMvc.perform(get("/api/tasks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    fun updateTask() {
        // Initialize the database
        taskService.save(task)

        val databaseSizeBeforeUpdate = taskRepository.findAll().size

        // Update the task
        val id = task.id
        assertNotNull(id)
        val updatedTask = taskRepository.findById(id).get()
        updatedTask.name = UPDATED_NAME
        updatedTask.estimatedDate = UPDATED_ESTIMATED_DATE
        updatedTask.details = UPDATED_DETAILS

        restTaskMockMvc.perform(
            put("/api/tasks")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(updatedTask))
        ).andExpect(status().isOk)

        // Validate the Task in the database
        val taskList = taskRepository.findAll()
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate)
        val testTask = taskList[taskList.size - 1]
        assertThat(testTask.name).isEqualTo(UPDATED_NAME)
        assertThat(testTask.estimatedDate).isEqualTo(UPDATED_ESTIMATED_DATE)
        assertThat(testTask.details).isEqualTo(UPDATED_DETAILS)
    }

    @Test
    fun updateNonExistingTask() {
        val databaseSizeBeforeUpdate = taskRepository.findAll().size

        // Create the Task

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskMockMvc.perform(
            put("/api/tasks")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(task))
        ).andExpect(status().isBadRequest)

        // Validate the Task in the database
        val taskList = taskRepository.findAll()
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    fun deleteTask() {
        // Initialize the database
        taskService.save(task)

        val databaseSizeBeforeDelete = taskRepository.findAll().size

        val id = task.id
        assertNotNull(id)

        // Delete the task
        restTaskMockMvc.perform(
            delete("/api/tasks/{id}", id)
                .accept(APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val taskList = taskRepository.findAll()
        assertThat(taskList).hasSize(databaseSizeBeforeDelete - 1)
    }

    @Test
    fun equalsVerifier() {
        equalsVerifier(Task::class)
        val task1 = Task()
        task1.id = "id1"
        val task2 = Task()
        task2.id = task1.id
        assertThat(task1).isEqualTo(task2)
        task2.id = "id2"
        assertThat(task1).isNotEqualTo(task2)
        task1.id = null
        assertThat(task1).isNotEqualTo(task2)
    }

    companion object {

        private const val DEFAULT_NAME: String = "AAAAAAAAAA"
        private const val UPDATED_NAME = "BBBBBBBBBB"

        private val DEFAULT_ESTIMATED_DATE: Instant = Instant.ofEpochMilli(0L)
        private val UPDATED_ESTIMATED_DATE: Instant = Instant.now().truncatedTo(ChronoUnit.MILLIS)

        private const val DEFAULT_DETAILS: String = "AAAAAAAAAA"
        private const val UPDATED_DETAILS = "BBBBBBBBBB"

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(): Task {
            val task = Task(
                name = DEFAULT_NAME,
                estimatedDate = DEFAULT_ESTIMATED_DATE,
                details = DEFAULT_DETAILS
            )

            return task
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(): Task {
            val task = Task(
                name = UPDATED_NAME,
                estimatedDate = UPDATED_ESTIMATED_DATE,
                details = UPDATED_DETAILS
            )

            return task
        }
    }
}
