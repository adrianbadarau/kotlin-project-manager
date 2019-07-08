package com.lenovo.coe.web.rest

import com.lenovo.coe.PmAppApp
import com.lenovo.coe.domain.Project
import com.lenovo.coe.repository.ProjectRepository
import com.lenovo.coe.service.ProjectService
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
 * Test class for the ProjectResource REST controller.
 *
 * @see ProjectResource
 */
@SpringBootTest(classes = [PmAppApp::class])
class ProjectResourceIT {

    @Autowired
    private lateinit var projectRepository: ProjectRepository

    @Mock
    private lateinit var projectRepositoryMock: ProjectRepository

    @Mock
    private lateinit var projectServiceMock: ProjectService

    @Autowired
    private lateinit var projectService: ProjectService

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
        val projectResource = ProjectResource(projectService)
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
        assertThat(testProject.objective).isEqualTo(DEFAULT_OBJECTIVE)
        assertThat(testProject.target).isEqualTo(DEFAULT_TARGET)
        assertThat(testProject.budget).isEqualTo(DEFAULT_BUDGET)
        assertThat(testProject.risk).isEqualTo(DEFAULT_RISK)
        assertThat(testProject.benefitMesurement).isEqualTo(DEFAULT_BENEFIT_MESUREMENT)
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
    fun checkObjectiveIsRequired() {
        val databaseSizeBeforeTest = projectRepository.findAll().size
        // set the field null
        project.objective = null

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
    fun checkTargetIsRequired() {
        val databaseSizeBeforeTest = projectRepository.findAll().size
        // set the field null
        project.target = null

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
    fun checkRiskIsRequired() {
        val databaseSizeBeforeTest = projectRepository.findAll().size
        // set the field null
        project.risk = null

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
            .andExpect(jsonPath("$.[*].objective").value(hasItem(DEFAULT_OBJECTIVE)))
            .andExpect(jsonPath("$.[*].target").value(hasItem(DEFAULT_TARGET.toString())))
            .andExpect(jsonPath("$.[*].budget").value(hasItem(DEFAULT_BUDGET)))
            .andExpect(jsonPath("$.[*].risk").value(hasItem(DEFAULT_RISK)))
            .andExpect(jsonPath("$.[*].benefitMesurement").value(hasItem(DEFAULT_BENEFIT_MESUREMENT)))
    }
    
    @Suppress("unchecked")
    fun getAllProjectsWithEagerRelationshipsIsEnabled() {
        val projectResource = ProjectResource(projectServiceMock)
        `when`(projectServiceMock.findAllWithEagerRelationships(any())).thenReturn(PageImpl(mutableListOf()))

        val restProjectMockMvc = MockMvcBuilders.standaloneSetup(projectResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build()

        restProjectMockMvc.perform(get("/api/projects?eagerload=true"))
            .andExpect(status().isOk)

        verify(projectServiceMock, times(1)).findAllWithEagerRelationships(any())
    }

    @Suppress("unchecked")
    fun getAllProjectsWithEagerRelationshipsIsNotEnabled() {
        val projectResource = ProjectResource(projectServiceMock)
            `when`(projectServiceMock.findAllWithEagerRelationships(any())).thenReturn( PageImpl( mutableListOf()))
        val restProjectMockMvc = MockMvcBuilders.standaloneSetup(projectResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build()

        restProjectMockMvc.perform(get("/api/projects?eagerload=true"))
            .andExpect(status().isOk)

        verify(projectServiceMock, times(1)).findAllWithEagerRelationships(any())
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
            .andExpect(jsonPath("$.objective").value(DEFAULT_OBJECTIVE))
            .andExpect(jsonPath("$.target").value(DEFAULT_TARGET.toString()))
            .andExpect(jsonPath("$.budget").value(DEFAULT_BUDGET))
            .andExpect(jsonPath("$.risk").value(DEFAULT_RISK))
            .andExpect(jsonPath("$.benefitMesurement").value(DEFAULT_BENEFIT_MESUREMENT))
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
        projectService.save(project)

        val databaseSizeBeforeUpdate = projectRepository.findAll().size

        // Update the project
        val id = project.id
        assertNotNull(id)
        val updatedProject = projectRepository.findById(id).get()
        updatedProject.name = UPDATED_NAME
        updatedProject.objective = UPDATED_OBJECTIVE
        updatedProject.target = UPDATED_TARGET
        updatedProject.budget = UPDATED_BUDGET
        updatedProject.risk = UPDATED_RISK
        updatedProject.benefitMesurement = UPDATED_BENEFIT_MESUREMENT

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
        assertThat(testProject.objective).isEqualTo(UPDATED_OBJECTIVE)
        assertThat(testProject.target).isEqualTo(UPDATED_TARGET)
        assertThat(testProject.budget).isEqualTo(UPDATED_BUDGET)
        assertThat(testProject.risk).isEqualTo(UPDATED_RISK)
        assertThat(testProject.benefitMesurement).isEqualTo(UPDATED_BENEFIT_MESUREMENT)
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
        projectService.save(project)

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

        private const val DEFAULT_OBJECTIVE: String = "AAAAAAAAAA"
        private const val UPDATED_OBJECTIVE = "BBBBBBBBBB"

        private val DEFAULT_TARGET: Instant = Instant.ofEpochMilli(0L)
        private val UPDATED_TARGET: Instant = Instant.now().truncatedTo(ChronoUnit.MILLIS)

        private const val DEFAULT_BUDGET: Double = 1.0
        private const val UPDATED_BUDGET: Double = 2.0

        private const val DEFAULT_RISK: String = "AAAAAAAAAA"
        private const val UPDATED_RISK = "BBBBBBBBBB"

        private const val DEFAULT_BENEFIT_MESUREMENT: String = "AAAAAAAAAA"
        private const val UPDATED_BENEFIT_MESUREMENT = "BBBBBBBBBB"

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
                objective = DEFAULT_OBJECTIVE,
                target = DEFAULT_TARGET,
                budget = DEFAULT_BUDGET,
                risk = DEFAULT_RISK,
                benefitMesurement = DEFAULT_BENEFIT_MESUREMENT
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
                objective = UPDATED_OBJECTIVE,
                target = UPDATED_TARGET,
                budget = UPDATED_BUDGET,
                risk = UPDATED_RISK,
                benefitMesurement = UPDATED_BENEFIT_MESUREMENT
            )

            return project
        }
    }
}
