package com.lenovo.coe.service

import com.lenovo.coe.domain.Project
import com.lenovo.coe.repository.ProjectRepository
import org.slf4j.LoggerFactory

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

import java.util.Optional

/**
 * Service Implementation for managing [Project].
 */
@Service
class ProjectService(
    private val projectRepository: ProjectRepository
) {

    private val log = LoggerFactory.getLogger(ProjectService::class.java)

    /**
     * Save a project.
     *
     * @param project the entity to save.
     * @return the persisted entity.
     */
    fun save(project: Project): Project {
        log.debug("Request to save Project : {}", project)
        return projectRepository.save(project)
    }

    /**
     * Get all the projects.
     *
     * @return the list of entities.
     */
    fun findAll(): MutableList<Project> {
        log.debug("Request to get all Projects")
        return projectRepository.findAllWithEagerRelationships()
    }

    /**
     * Get all the projects with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    fun findAllWithEagerRelationships(pageable: Pageable) =
        projectRepository.findAllWithEagerRelationships(pageable)


    /**
     * Get one project by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: String): Optional<Project> {
        log.debug("Request to get Project : {}", id)
        return projectRepository.findOneWithEagerRelationships(id)
    }

    /**
     * Delete the project by id.
     *
     * @param id the id of the entity.
     */
    fun delete(id: String) {
        log.debug("Request to delete Project : {}", id)

        projectRepository.deleteById(id)
    }
}
