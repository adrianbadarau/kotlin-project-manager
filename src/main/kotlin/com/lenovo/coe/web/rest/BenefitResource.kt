package com.lenovo.coe.web.rest

import com.lenovo.coe.domain.Benefit
import com.lenovo.coe.service.BenefitService
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
 * REST controller for managing [com.lenovo.coe.domain.Benefit].
 */
@RestController
@RequestMapping("/api")
class BenefitResource(
    private val benefitService: BenefitService
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /benefits` : Create a new benefit.
     *
     * @param benefit the benefit to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new benefit, or with status `400 (Bad Request)` if the benefit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/benefits")
    fun createBenefit(@Valid @RequestBody benefit: Benefit): ResponseEntity<Benefit> {
        log.debug("REST request to save Benefit : {}", benefit)
        if (benefit.id != null) {
            throw BadRequestAlertException("A new benefit cannot already have an ID", ENTITY_NAME, "idexists")
        }
        val result = benefitService.save(benefit)
        return ResponseEntity.created(URI("/api/benefits/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /benefits` : Updates an existing benefit.
     *
     * @param benefit the benefit to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated benefit,
     * or with status `400 (Bad Request)` if the benefit is not valid,
     * or with status `500 (Internal Server Error)` if the benefit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/benefits")
    fun updateBenefit(@Valid @RequestBody benefit: Benefit): ResponseEntity<Benefit> {
        log.debug("REST request to update Benefit : {}", benefit)
        if (benefit.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = benefitService.save(benefit)
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, benefit.id.toString()))
            .body(result)
    }

    /**
     * `GET  /benefits` : get all the benefits.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the [ResponseEntity] with status `200 (OK)` and the list of benefits in body.
     */
    @GetMapping("/benefits")    
    fun getAllBenefits(@RequestParam(required = false, defaultValue = "false") eagerload: Boolean): MutableList<Benefit> {
        log.debug("REST request to get all Benefits")
        return benefitService.findAll()
    }

    /**
     * `GET  /benefits/:id` : get the "id" benefit.
     *
     * @param id the id of the benefit to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the benefit, or with status `404 (Not Found)`.
     */
    @GetMapping("/benefits/{id}")
    fun getBenefit(@PathVariable id: String): ResponseEntity<Benefit> {
        log.debug("REST request to get Benefit : {}", id)
        val benefit = benefitService.findOne(id)
        return ResponseUtil.wrapOrNotFound(benefit)
    }

    /**
     * `DELETE  /benefits/:id` : delete the "id" benefit.
     *
     * @param id the id of the benefit to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/benefits/{id}")
    fun deleteBenefit(@PathVariable id: String): ResponseEntity<Void> {
        log.debug("REST request to delete Benefit : {}", id)
        benefitService.delete(id)
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build()
    }

    companion object {
        private const val ENTITY_NAME = "benefit"
    }
}
