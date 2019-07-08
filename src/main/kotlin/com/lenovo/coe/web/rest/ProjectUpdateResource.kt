package com.lenovo.coe.web.rest

import com.lenovo.coe.domain.ProjectUpdate
import com.lenovo.coe.service.ProjectUpdateService
import com.lenovo.coe.web.rest.errors.BadRequestAlertException

import io.github.jhipster.web.util.HeaderUtil
import io.github.jhipster.web.util.ResponseUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import java.net.URI
import java.net.URISyntaxException

/**
 * REST controller for managing [com.lenovo.coe.domain.ProjectUpdate].
 */
@RestController
@RequestMapping("/api")
class ProjectUpdateResource(
    private val projectUpdateService: ProjectUpdateService
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /project-updates` : Create a new projectUpdate.
     *
     * @param projectUpdate the projectUpdate to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new projectUpdate, or with status `400 (Bad Request)` if the projectUpdate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/project-updates")
    fun createProjectUpdate(@RequestBody projectUpdate: ProjectUpdate): ResponseEntity<ProjectUpdate> {
        log.debug("REST request to save ProjectUpdate : {}", projectUpdate)
        if (projectUpdate.id != null) {
            throw BadRequestAlertException("A new projectUpdate cannot already have an ID", ENTITY_NAME, "idexists")
        }
        val result = projectUpdateService.save(projectUpdate)
        return ResponseEntity.created(URI("/api/project-updates/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /project-updates` : Updates an existing projectUpdate.
     *
     * @param projectUpdate the projectUpdate to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated projectUpdate,
     * or with status `400 (Bad Request)` if the projectUpdate is not valid,
     * or with status `500 (Internal Server Error)` if the projectUpdate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/project-updates")
    fun updateProjectUpdate(@RequestBody projectUpdate: ProjectUpdate): ResponseEntity<ProjectUpdate> {
        log.debug("REST request to update ProjectUpdate : {}", projectUpdate)
        if (projectUpdate.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = projectUpdateService.save(projectUpdate)
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, projectUpdate.id.toString()))
            .body(result)
    }

    /**
     * `GET  /project-updates` : get all the projectUpdates.
     *
     * @return the [ResponseEntity] with status `200 (OK)` and the list of projectUpdates in body.
     */
    @GetMapping("/project-updates")    
    fun getAllProjectUpdates(): MutableList<ProjectUpdate> {
        log.debug("REST request to get all ProjectUpdates")
        return projectUpdateService.findAll()
    }

    /**
     * `GET  /project-updates/:id` : get the "id" projectUpdate.
     *
     * @param id the id of the projectUpdate to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the projectUpdate, or with status `404 (Not Found)`.
     */
    @GetMapping("/project-updates/{id}")
    fun getProjectUpdate(@PathVariable id: String): ResponseEntity<ProjectUpdate> {
        log.debug("REST request to get ProjectUpdate : {}", id)
        val projectUpdate = projectUpdateService.findOne(id)
        return ResponseUtil.wrapOrNotFound(projectUpdate)
    }

    /**
     * `DELETE  /project-updates/:id` : delete the "id" projectUpdate.
     *
     * @param id the id of the projectUpdate to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/project-updates/{id}")
    fun deleteProjectUpdate(@PathVariable id: String): ResponseEntity<Void> {
        log.debug("REST request to delete ProjectUpdate : {}", id)
        projectUpdateService.delete(id)
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build()
    }

    companion object {
        private const val ENTITY_NAME = "projectUpdate"
    }
}
