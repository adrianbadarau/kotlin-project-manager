package com.lenovo.coe.web.rest

import com.lenovo.coe.PmAppApp
import com.lenovo.coe.domain.Project
import com.lenovo.coe.repository.ProjectRepository
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

import java.time.Instant
import java.time.temporal.ChronoUnit

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
 * Test class for the ProjectResource REST controller.
 *
 * @see ProjectResource
 */
@SpringBootTest(classes = [PmAppApp::class])
class ProjectResourceIT {

    @Autowired
    private lateinit var projectRepository: ProjectRepository

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var validator: Validator

    private lateinit var restProjectMockMvc: MockMvc

    private lateinit var project: Project

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val projectResource = ProjectResource(projectRepository)
        this.restProjectMockMvc = MockMvcBuilders.standaloneSetup(projectResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        projectRepository.deleteAll()
        project = createEntity()
    }

    @Test
    fun createProject() {
        val databaseSizeBeforeCreate = projectRepository.findAll().size

        // Create the Project
        restProjectMockMvc.perform(
            post("/api/projects")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(project))
        ).andExpect(status().isCreated)

        // Validate the Project in the database
        val projectList = projectRepository.findAll()
        assertThat(projectList).hasSize(databaseSizeBeforeCreate + 1)
        val testProject = projectList[projectList.size - 1]
        assertThat(testProject.name).isEqualTo(DEFAULT_NAME)
        assertThat(testProject.code).isEqualTo(DEFAULT_CODE)
        assertThat(testProject.description).isEqualTo(DEFAULT_DESCRIPTION)
        assertThat(testProject.estimatedEndDate).isEqualTo(DEFAULT_ESTIMATED_END_DATE)
    }

    @Test
    fun createProjectWithExistingId() {
        val databaseSizeBeforeCreate = projectRepository.findAll().size

        // Create the Project with an existing ID
        project.id = "existing_id"

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectMockMvc.perform(
            post("/api/projects")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(project))
        ).andExpect(status().isBadRequest)

        // Validate the Project in the database
        val projectList = projectRepository.findAll()
        assertThat(projectList).hasSize(databaseSizeBeforeCreate)
    }


    @Test
    fun checkNameIsRequired() {
        val databaseSizeBeforeTest = projectRepository.findAll().size
        // set the field null
        project.name = null

        // Create the Project, which fails.

        restProjectMockMvc.perform(
            post("/api/projects")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(project))
        ).andExpect(status().isBadRequest)

        val projectList = projectRepository.findAll()
        assertThat(projectList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    fun checkCodeIsRequired() {
        val databaseSizeBeforeTest = projectRepository.findAll().size
        // set the field null
        project.code = null

        // Create the Project, which fails.

        restProjectMockMvc.perform(
            post("/api/projects")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(project))
        ).andExpect(status().isBadRequest)

        val projectList = projectRepository.findAll()
        assertThat(projectList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    fun getAllProjects() {
        // Initialize the database
        projectRepository.save(project)

        // Get all the projectList
        restProjectMockMvc.perform(get("/api/projects?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(project.id)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].estimatedEndDate").value(hasItem(DEFAULT_ESTIMATED_END_DATE.toString())))
    }
    
    @Test
    fun getProject() {
        // Initialize the database
        projectRepository.save(project)

        val id = project.id
        assertNotNull(id)

        // Get the project
        restProjectMockMvc.perform(get("/api/projects/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.estimatedEndDate").value(DEFAULT_ESTIMATED_END_DATE.toString()))
    }

    @Test
    fun getNonExistingProject() {
        // Get the project
        restProjectMockMvc.perform(get("/api/projects/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    fun updateProject() {
        // Initialize the database
        projectRepository.save(project)

        val databaseSizeBeforeUpdate = projectRepository.findAll().size

        // Update the project
        val id = project.id
        assertNotNull(id)
        val updatedProject = projectRepository.findById(id).get()
        updatedProject.name = UPDATED_NAME
        updatedProject.code = UPDATED_CODE
        updatedProject.description = UPDATED_DESCRIPTION
        updatedProject.estimatedEndDate = UPDATED_ESTIMATED_END_DATE

        restProjectMockMvc.perform(
            put("/api/projects")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(updatedProject))
        ).andExpect(status().isOk)

        // Validate the Project in the database
        val projectList = projectRepository.findAll()
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate)
        val testProject = projectList[projectList.size - 1]
        assertThat(testProject.name).isEqualTo(UPDATED_NAME)
        assertThat(testProject.code).isEqualTo(UPDATED_CODE)
        assertThat(testProject.description).isEqualTo(UPDATED_DESCRIPTION)
        assertThat(testProject.estimatedEndDate).isEqualTo(UPDATED_ESTIMATED_END_DATE)
    }

    @Test
    fun updateNonExistingProject() {
        val databaseSizeBeforeUpdate = projectRepository.findAll().size

        // Create the Project

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectMockMvc.perform(
            put("/api/projects")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(project))
        ).andExpect(status().isBadRequest)

        // Validate the Project in the database
        val projectList = projectRepository.findAll()
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    fun deleteProject() {
        // Initialize the database
        projectRepository.save(project)

        val databaseSizeBeforeDelete = projectRepository.findAll().size

        val id = project.id
        assertNotNull(id)

        // Delete the project
        restProjectMockMvc.perform(
            delete("/api/projects/{id}", id)
                .accept(APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val projectList = projectRepository.findAll()
        assertThat(projectList).hasSize(databaseSizeBeforeDelete - 1)
    }

    @Test
    fun equalsVerifier() {
        equalsVerifier(Project::class)
        val project1 = Project()
        project1.id = "id1"
        val project2 = Project()
        project2.id = project1.id
        assertThat(project1).isEqualTo(project2)
        project2.id = "id2"
        assertThat(project1).isNotEqualTo(project2)
        project1.id = null
        assertThat(project1).isNotEqualTo(project2)
    }

    companion object {

        private const val DEFAULT_NAME: String = "AAAAAAAAAA"
        private const val UPDATED_NAME = "BBBBBBBBBB"

        private const val DEFAULT_CODE: String = "AAAAAAAAAA"
        private const val UPDATED_CODE = "BBBBBBBBBB"

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
        fun createEntity(): Project {
            val project = Project(
                name = DEFAULT_NAME,
                code = DEFAULT_CODE,
                description = DEFAULT_DESCRIPTION,
                estimatedEndDate = DEFAULT_ESTIMATED_END_DATE
            )

            return project
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(): Project {
            val project = Project(
                name = UPDATED_NAME,
                code = UPDATED_CODE,
                description = UPDATED_DESCRIPTION,
                estimatedEndDate = UPDATED_ESTIMATED_END_DATE
            )

            return project
        }
    }
}
