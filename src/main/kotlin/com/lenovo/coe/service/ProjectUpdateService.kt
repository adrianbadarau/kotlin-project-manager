package com.lenovo.coe.service

import com.lenovo.coe.domain.ProjectUpdate
import com.lenovo.coe.repository.ProjectUpdateRepository
import org.slf4j.LoggerFactory

import org.springframework.stereotype.Service

import java.util.Optional

/**
 * Service Implementation for managing [ProjectUpdate].
 */
@Service
class ProjectUpdateService(
    private val projectUpdateRepository: ProjectUpdateRepository
) {

    private val log = LoggerFactory.getLogger(ProjectUpdateService::class.java)

    /**
     * Save a projectUpdate.
     *
     * @param projectUpdate the entity to save.
     * @return the persisted entity.
     */
    fun save(projectUpdate: ProjectUpdate): ProjectUpdate {
        log.debug("Request to save ProjectUpdate : {}", projectUpdate)
        return projectUpdateRepository.save(projectUpdate)
    }

    /**
     * Get all the projectUpdates.
     *
     * @return the list of entities.
     */
    fun findAll(): MutableList<ProjectUpdate> {
        log.debug("Request to get all ProjectUpdates")
        return projectUpdateRepository.findAll()
    }

    /**
     * Get one projectUpdate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: String): Optional<ProjectUpdate> {
        log.debug("Request to get ProjectUpdate : {}", id)
        return projectUpdateRepository.findById(id)
    }

    /**
     * Delete the projectUpdate by id.
     *
     * @param id the id of the entity.
     */
    fun delete(id: String) {
        log.debug("Request to delete ProjectUpdate : {}", id)

        projectUpdateRepository.deleteById(id)
    }
}
