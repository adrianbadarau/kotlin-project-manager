package com.for_the_love_of_code.kotlin_pm.web.rest

import com.for_the_love_of_code.kotlin_pm.KotlinPmApp
import com.for_the_love_of_code.kotlin_pm.domain.Team
import com.for_the_love_of_code.kotlin_pm.repository.TeamRepository
import com.for_the_love_of_code.kotlin_pm.repository.search.TeamSearchRepository
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
 * Test class for the TeamResource REST controller.
 *
 * @see TeamResource
 */
@SpringBootTest(classes = [KotlinPmApp::class])
class TeamResourceIT {

    @Autowired
    private lateinit var teamRepository: TeamRepository

    @Mock
    private lateinit var teamRepositoryMock: TeamRepository

    /**
     * This repository is mocked in the com.for_the_love_of_code.kotlin_pm.repository.search test package.
     *
     * @see com.for_the_love_of_code.kotlin_pm.repository.search.TeamSearchRepositoryMockConfiguration
     */
    @Autowired
    private lateinit var mockTeamSearchRepository: TeamSearchRepository

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

    private lateinit var restTeamMockMvc: MockMvc

    private lateinit var team: Team

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val teamResource = TeamResource(teamRepository, mockTeamSearchRepository)
        this.restTeamMockMvc = MockMvcBuilders.standaloneSetup(teamResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        team = createEntity(em)
    }

    @Test
    @Transactional
    fun createTeam() {
        val databaseSizeBeforeCreate = teamRepository.findAll().size

        // Create the Team
        restTeamMockMvc.perform(
            post("/api/teams")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(team))
        ).andExpect(status().isCreated)

        // Validate the Team in the database
        val teamList = teamRepository.findAll()
        assertThat(teamList).hasSize(databaseSizeBeforeCreate + 1)
        val testTeam = teamList[teamList.size - 1]
        assertThat(testTeam.name).isEqualTo(DEFAULT_NAME)

        // Validate the Team in Elasticsearch
    }

    @Test
    @Transactional
    fun createTeamWithExistingId() {
        val databaseSizeBeforeCreate = teamRepository.findAll().size

        // Create the Team with an existing ID
        team.id = 1L

        // An entity with an existing ID cannot be created, so this API call must fail
        restTeamMockMvc.perform(
            post("/api/teams")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(team))
        ).andExpect(status().isBadRequest)

        // Validate the Team in the database
        val teamList = teamRepository.findAll()
        assertThat(teamList).hasSize(databaseSizeBeforeCreate)

        // Validate the Team in Elasticsearch
        verify(mockTeamSearchRepository, times(0)).save(team)
    }


    @Test
    @Transactional
    fun checkNameIsRequired() {
        val databaseSizeBeforeTest = teamRepository.findAll().size
        // set the field null
        team.name = null

        // Create the Team, which fails.

        restTeamMockMvc.perform(
            post("/api/teams")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(team))
        ).andExpect(status().isBadRequest)

        val teamList = teamRepository.findAll()
        assertThat(teamList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Transactional
    fun getAllTeams() {
        // Initialize the database
        teamRepository.saveAndFlush(team)

        // Get all the teamList
        restTeamMockMvc.perform(get("/api/teams?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(team.id?.toInt())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
    }
    
    @SuppressWarnings("unchecked")
    fun getAllTeamsWithEagerRelationshipsIsEnabled() {
        val teamResource = TeamResource(teamRepositoryMock, mockTeamSearchRepository)
        `when`(teamRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(PageImpl(mutableListOf()))

        val restTeamMockMvc = MockMvcBuilders.standaloneSetup(teamResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build()

        restTeamMockMvc.perform(get("/api/teams?eagerload=true"))
        .andExpect(status().isOk)

        verify(teamRepositoryMock, times(1)).findAllWithEagerRelationships(any())
    }

    @SuppressWarnings("unchecked")
    fun getAllTeamsWithEagerRelationshipsIsNotEnabled() {
        val teamResource = TeamResource(teamRepositoryMock, mockTeamSearchRepository)
            `when`(teamRepositoryMock.findAllWithEagerRelationships(any())).thenReturn( PageImpl( mutableListOf()))
            val restTeamMockMvc = MockMvcBuilders.standaloneSetup(teamResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build()

        restTeamMockMvc.perform(get("/api/teams?eagerload=true"))
        .andExpect(status().isOk)

            verify(teamRepositoryMock, times(1)).findAllWithEagerRelationships(any())
    }

    @Test
    @Transactional
    fun getTeam() {
        // Initialize the database
        teamRepository.saveAndFlush(team)

        val id = team.id
        assertNotNull(id)

        // Get the team
        restTeamMockMvc.perform(get("/api/teams/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(id.toInt()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
    }

    @Test
    @Transactional
    fun getNonExistingTeam() {
        // Get the team
        restTeamMockMvc.perform(get("/api/teams/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    @Transactional
    fun updateTeam() {
        // Initialize the database
        teamRepository.saveAndFlush(team)

        val databaseSizeBeforeUpdate = teamRepository.findAll().size

        // Update the team
        val id = team.id
        assertNotNull(id)
        val updatedTeam = teamRepository.findById(id).get()
        // Disconnect from session so that the updates on updatedTeam are not directly saved in db
        em.detach(updatedTeam)
        updatedTeam.name = UPDATED_NAME

        restTeamMockMvc.perform(
            put("/api/teams")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTeam))
        ).andExpect(status().isOk)

        // Validate the Team in the database
        val teamList = teamRepository.findAll()
        assertThat(teamList).hasSize(databaseSizeBeforeUpdate)
        val testTeam = teamList[teamList.size - 1]
        assertThat(testTeam.name).isEqualTo(UPDATED_NAME)

        // Validate the Team in Elasticsearch
        verify(mockTeamSearchRepository, times(1)).save(testTeam)
    }

    @Test
    @Transactional
    fun updateNonExistingTeam() {
        val databaseSizeBeforeUpdate = teamRepository.findAll().size

        // Create the Team

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeamMockMvc.perform(
            put("/api/teams")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(team))
        ).andExpect(status().isBadRequest)

        // Validate the Team in the database
        val teamList = teamRepository.findAll()
        assertThat(teamList).hasSize(databaseSizeBeforeUpdate)

        // Validate the Team in Elasticsearch
        verify(mockTeamSearchRepository, times(0)).save(team)
    }

    @Test
    @Transactional
    fun deleteTeam() {
        // Initialize the database
        teamRepository.saveAndFlush(team)

        val databaseSizeBeforeDelete = teamRepository.findAll().size

        val id = team.id
        assertNotNull(id)

        // Delete the team
        restTeamMockMvc.perform(
            delete("/api/teams/{id}", id)
                .accept(TestUtil.APPLICATION_JSON_UTF8)
        ).andExpect(status().isNoContent)

        // Validate the database is empty
        val teamList = teamRepository.findAll()
        assertThat(teamList).hasSize(databaseSizeBeforeDelete - 1)

        // Validate the Team in Elasticsearch
        verify(mockTeamSearchRepository, times(1)).deleteById(id)
    }

    @Test
    @Transactional
    fun searchTeam() {
        // Initialize the database
        teamRepository.saveAndFlush(team)
        `when`(mockTeamSearchRepository.search(queryStringQuery("id:" + team.id)))
            .thenReturn(Collections.singletonList(team))
        // Search the team
        restTeamMockMvc.perform(get("/api/_search/teams?query=id:" + team.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(team.id?.toInt())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
    }

    @Test
    @Transactional
    fun equalsVerifier() {
        TestUtil.equalsVerifier(Team::class.java)
        val team1 = Team()
        team1.id = 1L
        val team2 = Team()
        team2.id = team1.id
        assertThat(team1).isEqualTo(team2)
        team2.id = 2L
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
        fun createEntity(em: EntityManager): Team {
            val team = Team()
            team.name = DEFAULT_NAME

        return team
        }
        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(em: EntityManager): Team {
            val team = Team()
            team.name = UPDATED_NAME

        return team
        }
    }
}
