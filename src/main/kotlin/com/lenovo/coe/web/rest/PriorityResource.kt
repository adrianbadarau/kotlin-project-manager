package com.lenovo.coe.web.rest

import com.lenovo.coe.domain.Priority
import com.lenovo.coe.repository.PriorityRepository
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

import javax.validation.Valid
import java.net.URI
import java.net.URISyntaxException

/**
 * REST controller for managing [com.lenovo.coe.domain.Priority].
 */
@RestController
@RequestMapping("/api")
class PriorityResource(
    private val priorityRepository: PriorityRepository
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /priorities` : Create a new priority.
     *
     * @param priority the priority to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new priority, or with status `400 (Bad Request)` if the priority has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/priorities")
    fun createPriority(@Valid @RequestBody priority: Priority): ResponseEntity<Priority> {
        log.debug("REST request to save Priority : {}", priority)
        if (priority.id != null) {
            throw BadRequestAlertException("A new priority cannot already have an ID", ENTITY_NAME, "idexists")
        }
        val result = priorityRepository.save(priority)
        return ResponseEntity.created(URI("/api/priorities/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /priorities` : Updates an existing priority.
     *
     * @param priority the priority to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated priority,
     * or with status `400 (Bad Request)` if the priority is not valid,
     * or with status `500 (Internal Server Error)` if the priority couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/priorities")
    fun updatePriority(@Valid @RequestBody priority: Priority): ResponseEntity<Priority> {
        log.debug("REST request to update Priority : {}", priority)
        if (priority.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = priorityRepository.save(priority)
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, priority.id.toString()))
            .body(result)
    }

    /**
     * `GET  /priorities` : get all the priorities.
     *
     * @return the [ResponseEntity] with status `200 (OK)` and the list of priorities in body.
     */
    @GetMapping("/priorities")    
    fun getAllPriorities(): MutableList<Priority> {
        log.debug("REST request to get all Priorities")
        return priorityRepository.findAll()
    }

    /**
     * `GET  /priorities/:id` : get the "id" priority.
     *
     * @param id the id of the priority to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the priority, or with status `404 (Not Found)`.
     */
    @GetMapping("/priorities/{id}")
    fun getPriority(@PathVariable id: String): ResponseEntity<Priority> {
        log.debug("REST request to get Priority : {}", id)
        val priority = priorityRepository.findById(id)
        return ResponseUtil.wrapOrNotFound(priority)
    }

    /**
     * `DELETE  /priorities/:id` : delete the "id" priority.
     *
     * @param id the id of the priority to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/priorities/{id}")
    fun deletePriority(@PathVariable id: String): ResponseEntity<Void> {
        log.debug("REST request to delete Priority : {}", id)

        priorityRepository.deleteById(id)
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build()
    }

    companion object {
        private const val ENTITY_NAME = "priority"
    }
}
