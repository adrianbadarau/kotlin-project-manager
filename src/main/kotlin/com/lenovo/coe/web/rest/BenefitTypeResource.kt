package com.lenovo.coe.web.rest

import com.lenovo.coe.domain.BenefitType
import com.lenovo.coe.service.BenefitTypeService
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
 * REST controller for managing [com.lenovo.coe.domain.BenefitType].
 */
@RestController
@RequestMapping("/api")
class BenefitTypeResource(
    private val benefitTypeService: BenefitTypeService
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /benefit-types` : Create a new benefitType.
     *
     * @param benefitType the benefitType to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new benefitType, or with status `400 (Bad Request)` if the benefitType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/benefit-types")
    fun createBenefitType(@Valid @RequestBody benefitType: BenefitType): ResponseEntity<BenefitType> {
        log.debug("REST request to save BenefitType : {}", benefitType)
        if (benefitType.id != null) {
            throw BadRequestAlertException("A new benefitType cannot already have an ID", ENTITY_NAME, "idexists")
        }
        val result = benefitTypeService.save(benefitType)
        return ResponseEntity.created(URI("/api/benefit-types/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /benefit-types` : Updates an existing benefitType.
     *
     * @param benefitType the benefitType to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated benefitType,
     * or with status `400 (Bad Request)` if the benefitType is not valid,
     * or with status `500 (Internal Server Error)` if the benefitType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/benefit-types")
    fun updateBenefitType(@Valid @RequestBody benefitType: BenefitType): ResponseEntity<BenefitType> {
        log.debug("REST request to update BenefitType : {}", benefitType)
        if (benefitType.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = benefitTypeService.save(benefitType)
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, benefitType.id.toString()))
            .body(result)
    }

    /**
     * `GET  /benefit-types` : get all the benefitTypes.
     *
     * @return the [ResponseEntity] with status `200 (OK)` and the list of benefitTypes in body.
     */
    @GetMapping("/benefit-types")    
    fun getAllBenefitTypes(): MutableList<BenefitType> {
        log.debug("REST request to get all BenefitTypes")
        return benefitTypeService.findAll()
    }

    /**
     * `GET  /benefit-types/:id` : get the "id" benefitType.
     *
     * @param id the id of the benefitType to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the benefitType, or with status `404 (Not Found)`.
     */
    @GetMapping("/benefit-types/{id}")
    fun getBenefitType(@PathVariable id: String): ResponseEntity<BenefitType> {
        log.debug("REST request to get BenefitType : {}", id)
        val benefitType = benefitTypeService.findOne(id)
        return ResponseUtil.wrapOrNotFound(benefitType)
    }

    /**
     * `DELETE  /benefit-types/:id` : delete the "id" benefitType.
     *
     * @param id the id of the benefitType to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/benefit-types/{id}")
    fun deleteBenefitType(@PathVariable id: String): ResponseEntity<Void> {
        log.debug("REST request to delete BenefitType : {}", id)
        benefitTypeService.delete(id)
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build()
    }

    companion object {
        private const val ENTITY_NAME = "benefitType"
    }
}
