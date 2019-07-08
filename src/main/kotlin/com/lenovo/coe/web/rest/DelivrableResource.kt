package com.lenovo.coe.web.rest

import com.lenovo.coe.domain.Delivrable
import com.lenovo.coe.service.DelivrableService
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
 * REST controller for managing [com.lenovo.coe.domain.Delivrable].
 */
@RestController
@RequestMapping("/api")
class DelivrableResource(
    private val delivrableService: DelivrableService
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /delivrables` : Create a new delivrable.
     *
     * @param delivrable the delivrable to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new delivrable, or with status `400 (Bad Request)` if the delivrable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/delivrables")
    fun createDelivrable(@Valid @RequestBody delivrable: Delivrable): ResponseEntity<Delivrable> {
        log.debug("REST request to save Delivrable : {}", delivrable)
        if (delivrable.id != null) {
            throw BadRequestAlertException("A new delivrable cannot already have an ID", ENTITY_NAME, "idexists")
        }
        val result = delivrableService.save(delivrable)
        return ResponseEntity.created(URI("/api/delivrables/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /delivrables` : Updates an existing delivrable.
     *
     * @param delivrable the delivrable to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated delivrable,
     * or with status `400 (Bad Request)` if the delivrable is not valid,
     * or with status `500 (Internal Server Error)` if the delivrable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/delivrables")
    fun updateDelivrable(@Valid @RequestBody delivrable: Delivrable): ResponseEntity<Delivrable> {
        log.debug("REST request to update Delivrable : {}", delivrable)
        if (delivrable.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = delivrableService.save(delivrable)
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, delivrable.id.toString()))
            .body(result)
    }

    /**
     * `GET  /delivrables` : get all the delivrables.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the [ResponseEntity] with status `200 (OK)` and the list of delivrables in body.
     */
    @GetMapping("/delivrables")    
    fun getAllDelivrables(@RequestParam(required = false, defaultValue = "false") eagerload: Boolean): MutableList<Delivrable> {
        log.debug("REST request to get all Delivrables")
        return delivrableService.findAll()
    }

    /**
     * `GET  /delivrables/:id` : get the "id" delivrable.
     *
     * @param id the id of the delivrable to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the delivrable, or with status `404 (Not Found)`.
     */
    @GetMapping("/delivrables/{id}")
    fun getDelivrable(@PathVariable id: String): ResponseEntity<Delivrable> {
        log.debug("REST request to get Delivrable : {}", id)
        val delivrable = delivrableService.findOne(id)
        return ResponseUtil.wrapOrNotFound(delivrable)
    }

    /**
     * `DELETE  /delivrables/:id` : delete the "id" delivrable.
     *
     * @param id the id of the delivrable to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/delivrables/{id}")
    fun deleteDelivrable(@PathVariable id: String): ResponseEntity<Void> {
        log.debug("REST request to delete Delivrable : {}", id)
        delivrableService.delete(id)
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build()
    }

    companion object {
        private const val ENTITY_NAME = "delivrable"
    }
}
