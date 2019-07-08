package com.lenovo.coe.service

import com.lenovo.coe.domain.Team
import com.lenovo.coe.repository.TeamRepository
import org.slf4j.LoggerFactory

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

import java.util.Optional

/**
 * Service Implementation for managing [Team].
 */
@Service
class TeamService(
    private val teamRepository: TeamRepository
) {

    private val log = LoggerFactory.getLogger(TeamService::class.java)

    /**
     * Save a team.
     *
     * @param team the entity to save.
     * @return the persisted entity.
     */
    fun save(team: Team): Team {
        log.debug("Request to save Team : {}", team)
        return teamRepository.save(team)
    }

    /**
     * Get all the teams.
     *
     * @return the list of entities.
     */
    fun findAll(): MutableList<Team> {
        log.debug("Request to get all Teams")
        return teamRepository.findAllWithEagerRelationships()
    }

    /**
     * Get all the teams with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    fun findAllWithEagerRelationships(pageable: Pageable) =
        teamRepository.findAllWithEagerRelationships(pageable)


    /**
     * Get one team by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: String): Optional<Team> {
        log.debug("Request to get Team : {}", id)
        return teamRepository.findOneWithEagerRelationships(id)
    }

    /**
     * Delete the team by id.
     *
     * @param id the id of the entity.
     */
    fun delete(id: String) {
        log.debug("Request to delete Team : {}", id)

        teamRepository.deleteById(id)
    }
}
