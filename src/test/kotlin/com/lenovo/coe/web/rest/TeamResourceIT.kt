package com.lenovo.coe.web.rest

import com.lenovo.coe.PmAppApp
import com.lenovo.coe.domain.Team
import com.lenovo.coe.repository.TeamRepository
import com.lenovo.coe.service.TeamService
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
 * Test class for the TeamResource REST controller.
 *
 * @see TeamResource
 */
@SpringBootTest(classes = [PmAppApp::class])
class TeamResourceIT {

    @Autowired
    private lateinit var teamRepository: TeamRepository

    @Mock
    private lateinit var teamRepositoryMock: TeamRepository

    @Mock
    private lateinit var teamServiceMock: TeamService

    @Autowired
    private lateinit var teamService: TeamService

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var validator: Validator

    private lateinit var restTeamMockMvc: MockMvc

    private lateinit var team: Team

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val teamResource = TeamResource(teamService)
        this.restTeamMockMvc = MockMvcBuilders.standaloneSetup(teamResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        teamRepository.deleteAll()
        team = createEntity()
    }

    @Test
    fun createTeam() {
        val databaseSizeBeforeCreate = teamRepository.findAll().size

        // Create the Team
        restTeamMockMvc.perform(
            post("/api/teams")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(team))
        ).andExpect(status().isCreated)

        // Validate the Team in the database
        val teamList = teamRepository.findAll()
        assertThat(teamList).hasSize(databaseSizeBeforeCreate + 1)
        val testTeam = teamList[teamList.size - 1]
        assertThat(testTeam.name).isEqualTo(DEFAULT_NAME)
    }

    @Test
    fun createTeamWithExistingId() {
        val databaseSizeBeforeCreate = teamRepository.findAll().size

        // Create the Team with an existing ID
        team.id = "existing_id"

        // An entity with an existing ID cannot be created, so this API call must fail
        restTeamMockMvc.perform(
            post("/api/teams")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(team))
        ).andExpect(status().isBadRequest)

        // Validate the Team in the database
        val teamList = teamRepository.findAll()
        assertThat(teamList).hasSize(databaseSizeBeforeCreate)
    }


    @Test
    fun checkNameIsRequired() {
        val databaseSizeBeforeTest = teamRepository.findAll().size
        // set the field null
        team.name = null

        // Create the Team, which fails.

        restTeamMockMvc.perform(
            post("/api/teams")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(team))
        ).andExpect(status().isBadRequest)

        val teamList = teamRepository.findAll()
        assertThat(teamList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    fun getAllTeams() {
        // Initialize the database
        teamRepository.save(team)

        // Get all the teamList
        restTeamMockMvc.perform(get("/api/teams?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(team.id)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
    }
    
    @Suppress("unchecked")
    fun getAllTeamsWithEagerRelationshipsIsEnabled() {
        val teamResource = TeamResource(teamServiceMock)
        `when`(teamServiceMock.findAllWithEagerRelationships(any())).thenReturn(PageImpl(mutableListOf()))

        val restTeamMockMvc = MockMvcBuilders.standaloneSetup(teamResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build()

        restTeamMockMvc.perform(get("/api/teams?eagerload=true"))
            .andExpect(status().isOk)

        verify(teamServiceMock, times(1)).findAllWithEagerRelationships(any())
    }

    @Suppress("unchecked")
    fun getAllTeamsWithEagerRelationshipsIsNotEnabled() {
        val teamResource = TeamResource(teamServiceMock)
            `when`(teamServiceMock.findAllWithEagerRelationships(any())).thenReturn( PageImpl( mutableListOf()))
        val restTeamMockMvc = MockMvcBuilders.standaloneSetup(teamResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build()

        restTeamMockMvc.perform(get("/api/teams?eagerload=true"))
            .andExpect(status().isOk)

        verify(teamServiceMock, times(1)).findAllWithEagerRelationships(any())
    }

    @Test
    fun getTeam() {
        // Initialize the database
        teamRepository.save(team)

        val id = team.id
        assertNotNull(id)

        // Get the team
        restTeamMockMvc.perform(get("/api/teams/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
    }

    @Test
    fun getNonExistingTeam() {
        // Get the team
        restTeamMockMvc.perform(get("/api/teams/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    fun updateTeam() {
        // Initialize the database
        teamService.save(team)

        val databaseSizeBeforeUpdate = teamRepository.findAll().size

        // Update the team
        val id = team.id
        assertNotNull(id)
        val updatedTeam = teamRepository.findById(id).get()
        updatedTeam.name = UPDATED_NAME

        restTeamMockMvc.perform(
            put("/api/teams")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(updatedTeam))
        ).andExpect(status().isOk)

        // Validate the Team in the database
        val teamList = teamRepository.findAll()
        assertThat(teamList).hasSize(databaseSizeBeforeUpdate)
        val testTeam = teamList[teamList.size - 1]
        assertThat(testTeam.name).isEqualTo(UPDATED_NAME)
    }

    @Test
    fun updateNonExistingTeam() {
        val databaseSizeBeforeUpdate = teamRepository.findAll().size

        // Create the Team

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeamMockMvc.perform(
            put("/api/teams")
                .contentType(APPLICATION_JSON_UTF8)
                .content(convertObjectToJsonBytes(team))
        ).andExpect(status().isBadRequest)

        // Validate the Team in the database
        val teamList = teamRepository.findAll()
        assertThat(teamList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    fun deleteTeam() {
        // Initialize the database
        teamService.save(team)

        val databaseSizeBeforeDelete = teamRepository.findAll().size

        val id = team.id
        assertNotNull(id)

        // Delete the team
        restTeamMockMvc.perform(
            delete("/api/teams/{id}", id)
                .accept(APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val teamList = teamRepository.findAll()
        assertThat(teamList).hasSize(databaseSizeBeforeDelete - 1)
    }

    @Test
    fun equalsVerifier() {
        equalsVerifier(Team::class)
        val team1 = Team()
        team1.id = "id1"
        val team2 = Team()
        team2.id = team1.id
        assertThat(team1).isEqualTo(team2)
        team2.id = "id2"
        assertThat(team1).isNotEqualTo(team2)
        team1.id = null
        assertThat(team1).isNotEqualTo(team2)
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
        fun createEntity(): Team {
            val team = Team(
                name = DEFAULT_NAME
            )

            return team
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(): Team {
            val team = Team(
                name = UPDATED_NAME
            )

            return team
        }
    }
}
