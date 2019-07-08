package com.lenovo.coe.web.rest

import com.lenovo.coe.domain.Milestone
import com.lenovo.coe.service.MilestoneService
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
 * REST controller for managing [com.lenovo.coe.domain.Milestone].
 */
@RestController
@RequestMapping("/api")
class MilestoneResource(
    private val milestoneService: MilestoneService
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /milestones` : Create a new milestone.
     *
     * @param milestone the milestone to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new milestone, or with status `400 (Bad Request)` if the milestone has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/milestones")
    fun createMilestone(@Valid @RequestBody milestone: Milestone): ResponseEntity<Milestone> {
        log.debug("REST request to save Milestone : {}", milestone)
        if (milestone.id != null) {
            throw BadRequestAlertException("A new milestone cannot already have an ID", ENTITY_NAME, "idexists")
        }
        val result = milestoneService.save(milestone)
        return ResponseEntity.created(URI("/api/milestones/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /milestones` : Updates an existing milestone.
     *
     * @param milestone the milestone to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated milestone,
     * or with status `400 (Bad Request)` if the milestone is not valid,
     * or with status `500 (Internal Server Error)` if the milestone couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/milestones")
    fun updateMilestone(@Valid @RequestBody milestone: Milestone): ResponseEntity<Milestone> {
        log.debug("REST request to update Milestone : {}", milestone)
        if (milestone.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = milestoneService.save(milestone)
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, milestone.id.toString()))
            .body(result)
    }

    /**
     * `GET  /milestones` : get all the milestones.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the [ResponseEntity] with status `200 (OK)` and the list of milestones in body.
     */
    @GetMapping("/milestones")    
    fun getAllMilestones(@RequestParam(required = false, defaultValue = "false") eagerload: Boolean): MutableList<Milestone> {
        log.debug("REST request to get all Milestones")
        return milestoneService.findAll()
    }

    /**
     * `GET  /milestones/:id` : get the "id" milestone.
     *
     * @param id the id of the milestone to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the milestone, or with status `404 (Not Found)`.
     */
    @GetMapping("/milestones/{id}")
    fun getMilestone(@PathVariable id: String): ResponseEntity<Milestone> {
        log.debug("REST request to get Milestone : {}", id)
        val milestone = milestoneService.findOne(id)
        return ResponseUtil.wrapOrNotFound(milestone)
    }

    /**
     * `DELETE  /milestones/:id` : delete the "id" milestone.
     *
     * @param id the id of the milestone to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/milestones/{id}")
    fun deleteMilestone(@PathVariable id: String): ResponseEntity<Void> {
        log.debug("REST request to delete Milestone : {}", id)
        milestoneService.delete(id)
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build()
    }

    companion object {
        private const val ENTITY_NAME = "milestone"
    }
}
