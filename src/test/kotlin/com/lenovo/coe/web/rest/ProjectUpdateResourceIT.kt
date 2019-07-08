package com.lenovo.coe.web.rest

import com.lenovo.coe.PmAppApp
import com.lenovo.coe.domain.ProjectUpdate
import com.lenovo.coe.repository.ProjectUpdateRepository
import com.lenovo.coe.service.ProjectUpdateService
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
 * Test class for the ProjectUpdateResource REST controller.
 *
 * @see ProjectUpdateResource
 */
@SpringBootTest(classes = [PmAppApp::class])
class ProjectUpdateResourceIT {

    @Autowired
    private lateinit var projectUpdateRepository: ProjectUpdateRepository

    @Autowired
    private lateinit var projectUpdateService: ProjectUpdateService

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var validator: Validator

    private lateinit var restProjectUpdateMockMvc: MockMvc

    private lateinit var projectUpdate: ProjectUpdate

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val projectUpdateResource = ProjectUpdateResource(projectUpdateService)
        this.restProjectUpdateMockMvc = MockMvcBuilders.standaloneSetup(projectUpdateResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        projectUpdateRepository.deleteAll()
        projectUpdate = createEntity()
    }

    @Test
    fun createProjectUpdate() {
        val databaseSizeBeforeCreate = projectUpdateRepository.findAll().size

        // Create the ProjectUpdate
        restProjectUpdateMockMvc.perform(
            post("/api/project-updates")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(projectUpdate))
        ).andExpect(status().isCreated)

        // Validate the ProjectUpdate in the database
        val projectUpdateList = projectUpdateRepository.findAll()
        assertThat(projectUpdateList).hasSize(databaseSizeBeforeCreate + 1)
        val testProjectUpdate = projectUpdateList[projectUpdateList.size - 1]
        assertThat(testProjectUpdate.keyMilestoneUpdate).isEqualTo(DEFAULT_KEY_MILESTONE_UPDATE)
        assertThat(testProjectUpdate.comments).isEqualTo(DEFAULT_COMMENTS)
        assertThat(testProjectUpdate.taskPlan).isEqualTo(DEFAULT_TASK_PLAN)
        assertThat(testProjectUpdate.risk).isEqualTo(DEFAULT_RISK)
        assertThat(testProjectUpdate.supportNeaded).isEqualTo(DEFAULT_SUPPORT_NEADED)
    }

    @Test
    fun createProjectUpdateWithExistingId() {
        val databaseSizeBeforeCreate = projectUpdateRepository.findAll().size

        // Create the ProjectUpdate with an existing ID
        projectUpdate.id = "existing_id"

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectUpdateMockMvc.perform(
            post("/api/project-updates")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(projectUpdate))
        ).andExpect(status().isBadRequest)

        // Validate the ProjectUpdate in the database
        val projectUpdateList = projectUpdateRepository.findAll()
        assertThat(projectUpdateList).hasSize(databaseSizeBeforeCreate)
    }


    @Test
    fun getAllProjectUpdates() {
        // Initialize the database
        projectUpdateRepository.save(projectUpdate)

        // Get all the projectUpdateList
        restProjectUpdateMockMvc.perform(get("/api/project-updates?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectUpdate.id)))
            .andExpect(jsonPath("$.[*].keyMilestoneUpdate").value(hasItem(DEFAULT_KEY_MILESTONE_UPDATE)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)))
            .andExpect(jsonPath("$.[*].taskPlan").value(hasItem(DEFAULT_TASK_PLAN)))
            .andExpect(jsonPath("$.[*].risk").value(hasItem(DEFAULT_RISK)))
            .andExpect(jsonPath("$.[*].supportNeaded").value(hasItem(DEFAULT_SUPPORT_NEADED)))
    }
    
    @Test
    fun getProjectUpdate() {
        // Initialize the database
        projectUpdateRepository.save(projectUpdate)

        val id = projectUpdate.id
        assertNotNull(id)

        // Get the projectUpdate
        restProjectUpdateMockMvc.perform(get("/api/project-updates/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.keyMilestoneUpdate").value(DEFAULT_KEY_MILESTONE_UPDATE))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS))
            .andExpect(jsonPath("$.taskPlan").value(DEFAULT_TASK_PLAN))
            .andExpect(jsonPath("$.risk").value(DEFAULT_RISK))
            .andExpect(jsonPath("$.supportNeaded").value(DEFAULT_SUPPORT_NEADED))
    }

    @Test
    fun getNonExistingProjectUpdate() {
        // Get the projectUpdate
        restProjectUpdateMockMvc.perform(get("/api/project-updates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    fun updateProjectUpdate() {
        // Initialize the database
        projectUpdateService.save(projectUpdate)

        val databaseSizeBeforeUpdate = projectUpdateRepository.findAll().size

        // Update the projectUpdate
        val id = projectUpdate.id
        assertNotNull(id)
        val updatedProjectUpdate = projectUpdateRepository.findById(id).get()
        updatedProjectUpdate.keyMilestoneUpdate = UPDATED_KEY_MILESTONE_UPDATE
        updatedProjectUpdate.comments = UPDATED_COMMENTS
        updatedProjectUpdate.taskPlan = UPDATED_TASK_PLAN
        updatedProjectUpdate.risk = UPDATED_RISK
        updatedProjectUpdate.supportNeaded = UPDATED_SUPPORT_NEADED

        restProjectUpdateMockMvc.perform(
            put("/api/project-updates")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(updatedProjectUpdate))
        ).andExpect(status().isOk)

        // Validate the ProjectUpdate in the database
        val projectUpdateList = projectUpdateRepository.findAll()
        assertThat(projectUpdateList).hasSize(databaseSizeBeforeUpdate)
        val testProjectUpdate = projectUpdateList[projectUpdateList.size - 1]
        assertThat(testProjectUpdate.keyMilestoneUpdate).isEqualTo(UPDATED_KEY_MILESTONE_UPDATE)
        assertThat(testProjectUpdate.comments).isEqualTo(UPDATED_COMMENTS)
        assertThat(testProjectUpdate.taskPlan).isEqualTo(UPDATED_TASK_PLAN)
        assertThat(testProjectUpdate.risk).isEqualTo(UPDATED_RISK)
        assertThat(testProjectUpdate.supportNeaded).isEqualTo(UPDATED_SUPPORT_NEADED)
    }

    @Test
    fun updateNonExistingProjectUpdate() {
        val databaseSizeBeforeUpdate = projectUpdateRepository.findAll().size

        // Create the ProjectUpdate

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectUpdateMockMvc.perform(
            put("/api/project-updates")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(projectUpdate))
        ).andExpect(status().isBadRequest)

        // Validate the ProjectUpdate in the database
        val projectUpdateList = projectUpdateRepository.findAll()
        assertThat(projectUpdateList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    fun deleteProjectUpdate() {
        // Initialize the database
        projectUpdateService.save(projectUpdate)

        val databaseSizeBeforeDelete = projectUpdateRepository.findAll().size

        val id = projectUpdate.id
        assertNotNull(id)

        // Delete the projectUpdate
        restProjectUpdateMockMvc.perform(
            delete("/api/project-updates/{id}", id)
                .accept(APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val projectUpdateList = projectUpdateRepository.findAll()
        assertThat(projectUpdateList).hasSize(databaseSizeBeforeDelete - 1)
    }

    @Test
    fun equalsVerifier() {
        equalsVerifier(ProjectUpdate::class)
        val projectUpdate1 = ProjectUpdate()
        projectUpdate1.id = "id1"
        val projectUpdate2 = ProjectUpdate()
        projectUpdate2.id = projectUpdate1.id
        assertThat(projectUpdate1).isEqualTo(projectUpdate2)
        projectUpdate2.id = "id2"
        assertThat(projectUpdate1).isNotEqualTo(projectUpdate2)
        projectUpdate1.id = null
        assertThat(projectUpdate1).isNotEqualTo(projectUpdate2)
    }

    companion object {

        private const val DEFAULT_KEY_MILESTONE_UPDATE: String = "AAAAAAAAAA"
        private const val UPDATED_KEY_MILESTONE_UPDATE = "BBBBBBBBBB"

        private const val DEFAULT_COMMENTS: String = "AAAAAAAAAA"
        private const val UPDATED_COMMENTS = "BBBBBBBBBB"

        private const val DEFAULT_TASK_PLAN: String = "AAAAAAAAAA"
        private const val UPDATED_TASK_PLAN = "BBBBBBBBBB"

        private const val DEFAULT_RISK: String = "AAAAAAAAAA"
        private const val UPDATED_RISK = "BBBBBBBBBB"

        private const val DEFAULT_SUPPORT_NEADED: String = "AAAAAAAAAA"
        private const val UPDATED_SUPPORT_NEADED = "BBBBBBBBBB"

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(): ProjectUpdate {
            val projectUpdate = ProjectUpdate(
                keyMilestoneUpdate = DEFAULT_KEY_MILESTONE_UPDATE,
                comments = DEFAULT_COMMENTS,
                taskPlan = DEFAULT_TASK_PLAN,
                risk = DEFAULT_RISK,
                supportNeaded = DEFAULT_SUPPORT_NEADED
            )

            return projectUpdate
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(): ProjectUpdate {
            val projectUpdate = ProjectUpdate(
                keyMilestoneUpdate = UPDATED_KEY_MILESTONE_UPDATE,
                comments = UPDATED_COMMENTS,
                taskPlan = UPDATED_TASK_PLAN,
                risk = UPDATED_RISK,
                supportNeaded = UPDATED_SUPPORT_NEADED
            )

            return projectUpdate
        }
    }
}
