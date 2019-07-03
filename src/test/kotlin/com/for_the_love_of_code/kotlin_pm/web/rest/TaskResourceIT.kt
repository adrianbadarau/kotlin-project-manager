package com.for_the_love_of_code.kotlin_pm.web.rest

import com.for_the_love_of_code.kotlin_pm.KotlinPmApp
import com.for_the_love_of_code.kotlin_pm.domain.Task
import com.for_the_love_of_code.kotlin_pm.repository.TaskRepository
import com.for_the_love_of_code.kotlin_pm.repository.search.TaskSearchRepository
import com.for_the_love_of_code.kotlin_pm.web.rest.errors.ExceptionTranslator

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator
import javax.persistence.EntityManager
import java.util.Collections

import com.for_the_love_of_code.kotlin_pm.web.rest.TestUtil.createFormattingConversionService
import org.assertj.core.api.Assertions.assertThat
import org.elasticsearch.index.query.QueryBuilders.queryStringQuery
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
@SpringBootTest(classes = [KotlinPmApp::class])
class TaskResourceIT {

    @Autowired
    private lateinit var taskRepository: TaskRepository

    @Mock
    private lateinit var taskRepositoryMock: TaskRepository

    /**
     * This repository is mocked in the com.for_the_love_of_code.kotlin_pm.repository.search test package.
     *
     * @see com.for_the_love_of_code.kotlin_pm.repository.search.TaskSearchRepositoryMockConfiguration
     */
    @Autowired
    private lateinit var mockTaskSearchRepository: TaskSearchRepository

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var validator: Validator

    private lateinit var restTaskMockMvc: MockMvc

    private lateinit var task: Task

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val taskResource = TaskResource(taskRepository, mockTaskSearchRepository)
        this.restTaskMockMvc = MockMvcBuilders.standaloneSetup(taskResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        task = createEntity(em)
    }

    @Test
    @Transactional
    fun createTask() {
        val databaseSizeBeforeCreate = taskRepository.findAll().size

        // Create the Task
        restTaskMockMvc.perform(
            post("/api/tasks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(task))
        ).andExpect(status().isCreated)

        // Validate the Task in the database
        val taskList = taskRepository.findAll()
        assertThat(taskList).hasSize(databaseSizeBeforeCreate + 1)
        val testTask = taskList[taskList.size - 1]
        assertThat(testTask.name).isEqualTo(DEFAULT_NAME)

        // Validate the Task in Elasticsearch
    }

    @Test
    @Transactional
    fun createTaskWithExistingId() {
        val databaseSizeBeforeCreate = taskRepository.findAll().size

        // Create the Task with an existing ID
        task.id = 1L

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskMockMvc.perform(
            post("/api/tasks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(task))
        ).andExpect(status().isBadRequest)

        // Validate the Task in the database
        val taskList = taskRepository.findAll()
        assertThat(taskList).hasSize(databaseSizeBeforeCreate)

        // Validate the Task in Elasticsearch
        verify(mockTaskSearchRepository, times(0)).save(task)
    }


    @Test
    @Transactional
    fun checkNameIsRequired() {
        val databaseSizeBeforeTest = taskRepository.findAll().size
        // set the field null
        task.name = null

        // Create the Task, which fails.

        restTaskMockMvc.perform(
            post("/api/tasks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(task))
        ).andExpect(status().isBadRequest)

        val taskList = taskRepository.findAll()
        assertThat(taskList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun getAllTasks() {
        // Initialize the database
        taskRepository.saveAndFlush(task)

        // Get all the taskList
        restTaskMockMvc.perform(get("/api/tasks?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(task.id?.toInt())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
    }
    
    @SuppressWarnings("unchecked")
    fun getAllTasksWithEagerRelationshipsIsEnabled() {
        val taskResource = TaskResource(taskRepositoryMock, mockTaskSearchRepository)
        `when`(taskRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(PageImpl(mutableListOf()))

        val restTaskMockMvc = MockMvcBuilders.standaloneSetup(taskResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build()

        restTaskMockMvc.perform(get("/api/tasks?eagerload=true"))
        .andExpect(status().isOk)

        verify(taskRepositoryMock, times(1)).findAllWithEagerRelationships(any())
    }

    @SuppressWarnings("unchecked")
    fun getAllTasksWithEagerRelationshipsIsNotEnabled() {
        val taskResource = TaskResource(taskRepositoryMock, mockTaskSearchRepository)
            `when`(taskRepositoryMock.findAllWithEagerRelationships(any())).thenReturn( PageImpl( mutableListOf()))
            val restTaskMockMvc = MockMvcBuilders.standaloneSetup(taskResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build()

        restTaskMockMvc.perform(get("/api/tasks?eagerload=true"))
        .andExpect(status().isOk)

            verify(taskRepositoryMock, times(1)).findAllWithEagerRelationships(any())
    }

    @Test
    @Transactional
    fun getTask() {
        // Initialize the database
        taskRepository.saveAndFlush(task)

        val id = task.id
        assertNotNull(id)

        // Get the task
        restTaskMockMvc.perform(get("/api/tasks/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id.toInt()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
    }

    @Test
    @Transactional
    fun getNonExistingTask() {
        // Get the task
        restTaskMockMvc.perform(get("/api/tasks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    @Transactional
    fun updateTask() {
        // Initialize the database
        taskRepository.saveAndFlush(task)

        val databaseSizeBeforeUpdate = taskRepository.findAll().size

        // Update the task
        val id = task.id
        assertNotNull(id)
        val updatedTask = taskRepository.findById(id).get()
        // Disconnect from session so that the updates on updatedTask are not directly saved in db
        em.detach(updatedTask)
        updatedTask.name = UPDATED_NAME

        restTaskMockMvc.perform(
            put("/api/tasks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTask))
        ).andExpect(status().isOk)

        // Validate the Task in the database
        val taskList = taskRepository.findAll()
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate)
        val testTask = taskList[taskList.size - 1]
        assertThat(testTask.name).isEqualTo(UPDATED_NAME)

        // Validate the Task in Elasticsearch
        verify(mockTaskSearchRepository, times(1)).save(testTask)
    }

    @Test
    @Transactional
    fun updateNonExistingTask() {
        val databaseSizeBeforeUpdate = taskRepository.findAll().size

        // Create the Task

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskMockMvc.perform(
            put("/api/tasks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(task))
        ).andExpect(status().isBadRequest)

        // Validate the Task in the database
        val taskList = taskRepository.findAll()
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate)

        // Validate the Task in Elasticsearch
        verify(mockTaskSearchRepository, times(0)).save(task)
    }

    @Test
    @Transactional
    fun deleteTask() {
        // Initialize the database
        taskRepository.saveAndFlush(task)

        val databaseSizeBeforeDelete = taskRepository.findAll().size

        val id = task.id
        assertNotNull(id)

        // Delete the task
        restTaskMockMvc.perform(
            delete("/api/tasks/{id}", id)
                .accept(TestUtil.APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database is empty
        val taskList = taskRepository.findAll()
        assertThat(taskList).hasSize(databaseSizeBeforeDelete - 1)

        // Validate the Task in Elasticsearch
        verify(mockTaskSearchRepository, times(1)).deleteById(id)
    }

    @Test
    @Transactional
    fun searchTask() {
        // Initialize the database
        taskRepository.saveAndFlush(task)
        `when`(mockTaskSearchRepository.search(queryStringQuery("id:" + task.id)))
            .thenReturn(Collections.singletonList(task))
        // Search the task
        restTaskMockMvc.perform(get("/api/_search/tasks?query=id:" + task.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(task.id?.toInt())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
    }

    @Test
    @Transactional
    fun equalsVerifier() {
        TestUtil.equalsVerifier(Task::class.java)
        val task1 = Task()
        task1.id = 1L
        val task2 = Task()
        task2.id = task1.id
        assertThat(task1).isEqualTo(task2)
        task2.id = 2L
        assertThat(task1).isNotEqualTo(task2)
        task1.id = null
        assertThat(task1).isNotEqualTo(task2)
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
        fun createEntity(em: EntityManager): Task {
            val task = Task()
            task.name = DEFAULT_NAME

        return task
        }
        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): Task {
            val task = Task()
            task.name = UPDATED_NAME

        return task
        }
    }
}
