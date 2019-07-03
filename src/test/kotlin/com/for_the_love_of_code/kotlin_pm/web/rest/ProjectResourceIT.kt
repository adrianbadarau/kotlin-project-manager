package com.for_the_love_of_code.kotlin_pm.web.rest

import com.for_the_love_of_code.kotlin_pm.KotlinPmApp
import com.for_the_love_of_code.kotlin_pm.domain.Project
import com.for_the_love_of_code.kotlin_pm.repository.ProjectRepository
import com.for_the_love_of_code.kotlin_pm.repository.search.ProjectSearchRepository
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
import java.time.Instant
import java.time.temporal.ChronoUnit
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
 * Test class for the ProjectResource REST controller.
 *
 * @see ProjectResource
 */
@SpringBootTest(classes = [KotlinPmApp::class])
class ProjectResourceIT {

    @Autowired
    private lateinit var projectRepository: ProjectRepository

    @Mock
    private lateinit var projectRepositoryMock: ProjectRepository

    /**
     * This repository is mocked in the com.for_the_love_of_code.kotlin_pm.repository.search test package.
     *
     * @see com.for_the_love_of_code.kotlin_pm.repository.search.ProjectSearchRepositoryMockConfiguration
     */
    @Autowired
    private lateinit var mockProjectSearchRepository: ProjectSearchRepository

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

    private lateinit var restProjectMockMvc: MockMvc

    private lateinit var project: Project

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val projectResource = ProjectResource(projectRepository, mockProjectSearchRepository)
        this.restProjectMockMvc = MockMvcBuilders.standaloneSetup(projectResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        project = createEntity(em)
    }

    @Test
    @Transactional
    fun createProject() {
        val databaseSizeBeforeCreate = projectRepository.findAll().size

        // Create the Project
        restProjectMockMvc.perform(
            post("/api/projects")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(project))
        ).andExpect(status().isCreated)

        // Validate the Project in the database
        val projectList = projectRepository.findAll()
        assertThat(projectList).hasSize(databaseSizeBeforeCreate + 1)
        val testProject = projectList[projectList.size - 1]
        assertThat(testProject.name).isEqualTo(DEFAULT_NAME)
        assertThat(testProject.description).isEqualTo(DEFAULT_DESCRIPTION)
        assertThat(testProject.estimatedEndDate).isEqualTo(DEFAULT_ESTIMATED_END_DATE)

        // Validate the Project in Elasticsearch
    }

    @Test
    @Transactional
    fun createProjectWithExistingId() {
        val databaseSizeBeforeCreate = projectRepository.findAll().size

        // Create the Project with an existing ID
        project.id = 1L

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectMockMvc.perform(
            post("/api/projects")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(project))
        ).andExpect(status().isBadRequest)

        // Validate the Project in the database
        val projectList = projectRepository.findAll()
        assertThat(projectList).hasSize(databaseSizeBeforeCreate)

        // Validate the Project in Elasticsearch
        verify(mockProjectSearchRepository, times(0)).save(project)
    }


    @Test
    @Transactional
    fun checkNameIsRequired() {
        val databaseSizeBeforeTest = projectRepository.findAll().size
        // set the field null
        project.name = null

        // Create the Project, which fails.

        restProjectMockMvc.perform(
            post("/api/projects")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(project))
        ).andExpect(status().isBadRequest)

        val projectList = projectRepository.findAll()
        assertThat(projectList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun getAllProjects() {
        // Initialize the database
        projectRepository.saveAndFlush(project)

        // Get all the projectList
        restProjectMockMvc.perform(get("/api/projects?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(project.id?.toInt())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].estimatedEndDate").value(hasItem(DEFAULT_ESTIMATED_END_DATE.toString())))
    }
    
    @SuppressWarnings("unchecked")
    fun getAllProjectsWithEagerRelationshipsIsEnabled() {
        val projectResource = ProjectResource(projectRepositoryMock, mockProjectSearchRepository)
        `when`(projectRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(PageImpl(mutableListOf()))

        val restProjectMockMvc = MockMvcBuilders.standaloneSetup(projectResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build()

        restProjectMockMvc.perform(get("/api/projects?eagerload=true"))
        .andExpect(status().isOk)

        verify(projectRepositoryMock, times(1)).findAllWithEagerRelationships(any())
    }

    @SuppressWarnings("unchecked")
    fun getAllProjectsWithEagerRelationshipsIsNotEnabled() {
        val projectResource = ProjectResource(projectRepositoryMock, mockProjectSearchRepository)
            `when`(projectRepositoryMock.findAllWithEagerRelationships(any())).thenReturn( PageImpl( mutableListOf()))
            val restProjectMockMvc = MockMvcBuilders.standaloneSetup(projectResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build()

        restProjectMockMvc.perform(get("/api/projects?eagerload=true"))
        .andExpect(status().isOk)

            verify(projectRepositoryMock, times(1)).findAllWithEagerRelationships(any())
    }

    @Test
    @Transactional
    fun getProject() {
        // Initialize the database
        projectRepository.saveAndFlush(project)

        val id = project.id
        assertNotNull(id)

        // Get the project
        restProjectMockMvc.perform(get("/api/projects/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id.toInt()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.estimatedEndDate").value(DEFAULT_ESTIMATED_END_DATE.toString()))
    }

    @Test
    @Transactional
    fun getNonExistingProject() {
        // Get the project
        restProjectMockMvc.perform(get("/api/projects/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    @Transactional
    fun updateProject() {
        // Initialize the database
        projectRepository.saveAndFlush(project)

        val databaseSizeBeforeUpdate = projectRepository.findAll().size

        // Update the project
        val id = project.id
        assertNotNull(id)
        val updatedProject = projectRepository.findById(id).get()
        // Disconnect from session so that the updates on updatedProject are not directly saved in db
        em.detach(updatedProject)
        updatedProject.name = UPDATED_NAME
        updatedProject.description = UPDATED_DESCRIPTION
        updatedProject.estimatedEndDate = UPDATED_ESTIMATED_END_DATE

        restProjectMockMvc.perform(
            put("/api/projects")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedProject))
        ).andExpect(status().isOk)

        // Validate the Project in the database
        val projectList = projectRepository.findAll()
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate)
        val testProject = projectList[projectList.size - 1]
        assertThat(testProject.name).isEqualTo(UPDATED_NAME)
        assertThat(testProject.description).isEqualTo(UPDATED_DESCRIPTION)
        assertThat(testProject.estimatedEndDate).isEqualTo(UPDATED_ESTIMATED_END_DATE)

        // Validate the Project in Elasticsearch
        verify(mockProjectSearchRepository, times(1)).save(testProject)
    }

    @Test
    @Transactional
    fun updateNonExistingProject() {
        val databaseSizeBeforeUpdate = projectRepository.findAll().size

        // Create the Project

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectMockMvc.perform(
            put("/api/projects")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(project))
        ).andExpect(status().isBadRequest)

        // Validate the Project in the database
        val projectList = projectRepository.findAll()
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate)

        // Validate the Project in Elasticsearch
        verify(mockProjectSearchRepository, times(0)).save(project)
    }

    @Test
    @Transactional
    fun deleteProject() {
        // Initialize the database
        projectRepository.saveAndFlush(project)

        val databaseSizeBeforeDelete = projectRepository.findAll().size

        val id = project.id
        assertNotNull(id)

        // Delete the project
        restProjectMockMvc.perform(
            delete("/api/projects/{id}", id)
                .accept(TestUtil.APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database is empty
        val projectList = projectRepository.findAll()
        assertThat(projectList).hasSize(databaseSizeBeforeDelete - 1)

        // Validate the Project in Elasticsearch
        verify(mockProjectSearchRepository, times(1)).deleteById(id)
    }

    @Test
    @Transactional
    fun searchProject() {
        // Initialize the database
        projectRepository.saveAndFlush(project)
        `when`(mockProjectSearchRepository.search(queryStringQuery("id:" + project.id)))
            .thenReturn(Collections.singletonList(project))
        // Search the project
        restProjectMockMvc.perform(get("/api/_search/projects?query=id:" + project.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(project.id?.toInt())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].estimatedEndDate").value(hasItem(DEFAULT_ESTIMATED_END_DATE.toString())))
    }

    @Test
    @Transactional
    fun equalsVerifier() {
        TestUtil.equalsVerifier(Project::class.java)
        val project1 = Project()
        project1.id = 1L
        val project2 = Project()
        project2.id = project1.id
        assertThat(project1).isEqualTo(project2)
        project2.id = 2L
        assertThat(project1).isNotEqualTo(project2)
        project1.id = null
        assertThat(project1).isNotEqualTo(project2)
    }

    companion object {

        private const val DEFAULT_NAME: String = "AAAAAAAAAA"
        private const val UPDATED_NAME = "BBBBBBBBBB"

        private const val DEFAULT_DESCRIPTION: String = "AAAAAAAAAA"
        private const val UPDATED_DESCRIPTION = "BBBBBBBBBB"

        private val DEFAULT_ESTIMATED_END_DATE: Instant = Instant.ofEpochMilli(0L)
        private val UPDATED_ESTIMATED_END_DATE: Instant = Instant.now().truncatedTo(ChronoUnit.MILLIS)
        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(em: EntityManager): Project {
            val project = Project()
            project.name = DEFAULT_NAME
            project.description = DEFAULT_DESCRIPTION
            project.estimatedEndDate = DEFAULT_ESTIMATED_END_DATE

        return project
        }
        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): Project {
            val project = Project()
            project.name = UPDATED_NAME
            project.description = UPDATED_DESCRIPTION
            project.estimatedEndDate = UPDATED_ESTIMATED_END_DATE

        return project
        }
    }
}
