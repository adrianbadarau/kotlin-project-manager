package com.lenovo.coe.web.rest

import com.lenovo.coe.domain.BusinessCase
import com.lenovo.coe.service.BusinessCaseService
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
 * REST controller for managing [com.lenovo.coe.domain.BusinessCase].
 */
@RestController
@RequestMapping("/api")
class BusinessCaseResource(
    private val businessCaseService: BusinessCaseService
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /business-cases` : Create a new businessCase.
     *
     * @param businessCase the businessCase to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new businessCase, or with status `400 (Bad Request)` if the businessCase has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/business-cases")
    fun createBusinessCase(@RequestBody businessCase: BusinessCase): ResponseEntity<BusinessCase> {
        log.debug("REST request to save BusinessCase : {}", businessCase)
        if (businessCase.id != null) {
            throw BadRequestAlertException("A new businessCase cannot already have an ID", ENTITY_NAME, "idexists")
        }
        val result = businessCaseService.save(businessCase)
        return ResponseEntity.created(URI("/api/business-cases/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /business-cases` : Updates an existing businessCase.
     *
     * @param businessCase the businessCase to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated businessCase,
     * or with status `400 (Bad Request)` if the businessCase is not valid,
     * or with status `500 (Internal Server Error)` if the businessCase couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/business-cases")
    fun updateBusinessCase(@RequestBody businessCase: BusinessCase): ResponseEntity<BusinessCase> {
        log.debug("REST request to update BusinessCase : {}", businessCase)
        if (businessCase.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = businessCaseService.save(businessCase)
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, businessCase.id.toString()))
            .body(result)
    }

    /**
     * `GET  /business-cases` : get all the businessCases.
     *
     * @param filter the filter of the request.
     * @return the [ResponseEntity] with status `200 (OK)` and the list of businessCases in body.
     */
    @GetMapping("/business-cases")    
    fun getAllBusinessCases(@RequestParam(required = false) filter: String?): MutableList<BusinessCase> {
        if ("project-is-null".equals(filter)) {
            log.debug("REST request to get all BusinessCases where project is null")
            return businessCaseService.findAllWhereProjectIsNull()
        }
        log.debug("REST request to get all BusinessCases")
        return businessCaseService.findAll()
    }

    /**
     * `GET  /business-cases/:id` : get the "id" businessCase.
     *
     * @param id the id of the businessCase to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the businessCase, or with status `404 (Not Found)`.
     */
    @GetMapping("/business-cases/{id}")
    fun getBusinessCase(@PathVariable id: String): ResponseEntity<BusinessCase> {
        log.debug("REST request to get BusinessCase : {}", id)
        val businessCase = businessCaseService.findOne(id)
        return ResponseUtil.wrapOrNotFound(businessCase)
    }

    /**
     * `DELETE  /business-cases/:id` : delete the "id" businessCase.
     *
     * @param id the id of the businessCase to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/business-cases/{id}")
    fun deleteBusinessCase(@PathVariable id: String): ResponseEntity<Void> {
        log.debug("REST request to delete BusinessCase : {}", id)
        businessCaseService.delete(id)
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build()
    }

    companion object {
        private const val ENTITY_NAME = "businessCase"
    }
}
