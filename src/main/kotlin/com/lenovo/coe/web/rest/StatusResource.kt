package com.lenovo.coe.web.rest

import com.lenovo.coe.domain.Status
import com.lenovo.coe.service.StatusService
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
 * REST controller for managing [com.lenovo.coe.domain.Status].
 */
@RestController
@RequestMapping("/api")
class StatusResource(
    private val statusService: StatusService
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /statuses` : Create a new status.
     *
     * @param status the status to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new status, or with status `400 (Bad Request)` if the status has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/statuses")
    fun createStatus(@Valid @RequestBody status: Status): ResponseEntity<Status> {
        log.debug("REST request to save Status : {}", status)
        if (status.id != null) {
            throw BadRequestAlertException("A new status cannot already have an ID", ENTITY_NAME, "idexists")
        }
        val result = statusService.save(status)
        return ResponseEntity.created(URI("/api/statuses/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /statuses` : Updates an existing status.
     *
     * @param status the status to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated status,
     * or with status `400 (Bad Request)` if the status is not valid,
     * or with status `500 (Internal Server Error)` if the status couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/statuses")
    fun updateStatus(@Valid @RequestBody status: Status): ResponseEntity<Status> {
        log.debug("REST request to update Status : {}", status)
        if (status.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = statusService.save(status)
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, status.id.toString()))
            .body(result)
    }

    /**
     * `GET  /statuses` : get all the statuses.
     *
     * @return the [ResponseEntity] with status `200 (OK)` and the list of statuses in body.
     */
    @GetMapping("/statuses")    
    fun getAllStatuses(): MutableList<Status> {
        log.debug("REST request to get all Statuses")
        return statusService.findAll()
    }

    /**
     * `GET  /statuses/:id` : get the "id" status.
     *
     * @param id the id of the status to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the status, or with status `404 (Not Found)`.
     */
    @GetMapping("/statuses/{id}")
    fun getStatus(@PathVariable id: String): ResponseEntity<Status> {
        log.debug("REST request to get Status : {}", id)
        val status = statusService.findOne(id)
        return ResponseUtil.wrapOrNotFound(status)
    }

    /**
     * `DELETE  /statuses/:id` : delete the "id" status.
     *
     * @param id the id of the status to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/statuses/{id}")
    fun deleteStatus(@PathVariable id: String): ResponseEntity<Void> {
        log.debug("REST request to delete Status : {}", id)
        statusService.delete(id)
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build()
    }

    companion object {
        private const val ENTITY_NAME = "status"
    }
}
