package com.lenovo.coe.web.rest

import com.lenovo.coe.domain.Performance
import com.lenovo.coe.service.PerformanceService
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
 * REST controller for managing [com.lenovo.coe.domain.Performance].
 */
@RestController
@RequestMapping("/api")
class PerformanceResource(
    private val performanceService: PerformanceService
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /performances` : Create a new performance.
     *
     * @param performance the performance to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new performance, or with status `400 (Bad Request)` if the performance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/performances")
    fun createPerformance(@RequestBody performance: Performance): ResponseEntity<Performance> {
        log.debug("REST request to save Performance : {}", performance)
        if (performance.id != null) {
            throw BadRequestAlertException("A new performance cannot already have an ID", ENTITY_NAME, "idexists")
        }
        val result = performanceService.save(performance)
        return ResponseEntity.created(URI("/api/performances/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /performances` : Updates an existing performance.
     *
     * @param performance the performance to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated performance,
     * or with status `400 (Bad Request)` if the performance is not valid,
     * or with status `500 (Internal Server Error)` if the performance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/performances")
    fun updatePerformance(@RequestBody performance: Performance): ResponseEntity<Performance> {
        log.debug("REST request to update Performance : {}", performance)
        if (performance.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = performanceService.save(performance)
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, performance.id.toString()))
            .body(result)
    }

    /**
     * `GET  /performances` : get all the performances.
     *
     * @return the [ResponseEntity] with status `200 (OK)` and the list of performances in body.
     */
    @GetMapping("/performances")    
    fun getAllPerformances(): MutableList<Performance> {
        log.debug("REST request to get all Performances")
        return performanceService.findAll()
    }

    /**
     * `GET  /performances/:id` : get the "id" performance.
     *
     * @param id the id of the performance to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the performance, or with status `404 (Not Found)`.
     */
    @GetMapping("/performances/{id}")
    fun getPerformance(@PathVariable id: String): ResponseEntity<Performance> {
        log.debug("REST request to get Performance : {}", id)
        val performance = performanceService.findOne(id)
        return ResponseUtil.wrapOrNotFound(performance)
    }

    /**
     * `DELETE  /performances/:id` : delete the "id" performance.
     *
     * @param id the id of the performance to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/performances/{id}")
    fun deletePerformance(@PathVariable id: String): ResponseEntity<Void> {
        log.debug("REST request to delete Performance : {}", id)
        performanceService.delete(id)
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build()
    }

    companion object {
        private const val ENTITY_NAME = "performance"
    }
}
