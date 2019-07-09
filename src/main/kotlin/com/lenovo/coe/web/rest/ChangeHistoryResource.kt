package com.lenovo.coe.web.rest

import com.lenovo.coe.domain.ChangeHistory
import com.lenovo.coe.repository.ChangeHistoryRepository
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
 * REST controller for managing [com.lenovo.coe.domain.ChangeHistory].
 */
@RestController
@RequestMapping("/api")
class ChangeHistoryResource(
    private val changeHistoryRepository: ChangeHistoryRepository
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /change-histories` : Create a new changeHistory.
     *
     * @param changeHistory the changeHistory to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new changeHistory, or with status `400 (Bad Request)` if the changeHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/change-histories")
    fun createChangeHistory(@Valid @RequestBody changeHistory: ChangeHistory): ResponseEntity<ChangeHistory> {
        log.debug("REST request to save ChangeHistory : {}", changeHistory)
        if (changeHistory.id != null) {
            throw BadRequestAlertException("A new changeHistory cannot already have an ID", ENTITY_NAME, "idexists")
        }
        val result = changeHistoryRepository.save(changeHistory)
        return ResponseEntity.created(URI("/api/change-histories/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /change-histories` : Updates an existing changeHistory.
     *
     * @param changeHistory the changeHistory to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated changeHistory,
     * or with status `400 (Bad Request)` if the changeHistory is not valid,
     * or with status `500 (Internal Server Error)` if the changeHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/change-histories")
    fun updateChangeHistory(@Valid @RequestBody changeHistory: ChangeHistory): ResponseEntity<ChangeHistory> {
        log.debug("REST request to update ChangeHistory : {}", changeHistory)
        if (changeHistory.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = changeHistoryRepository.save(changeHistory)
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, changeHistory.id.toString()))
            .body(result)
    }

    /**
     * `GET  /change-histories` : get all the changeHistories.
     *
     * @return the [ResponseEntity] with status `200 (OK)` and the list of changeHistories in body.
     */
    @GetMapping("/change-histories")    
    fun getAllChangeHistories(): MutableList<ChangeHistory> {
        log.debug("REST request to get all ChangeHistories")
        return changeHistoryRepository.findAll()
    }

    /**
     * `GET  /change-histories/:id` : get the "id" changeHistory.
     *
     * @param id the id of the changeHistory to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the changeHistory, or with status `404 (Not Found)`.
     */
    @GetMapping("/change-histories/{id}")
    fun getChangeHistory(@PathVariable id: String): ResponseEntity<ChangeHistory> {
        log.debug("REST request to get ChangeHistory : {}", id)
        val changeHistory = changeHistoryRepository.findById(id)
        return ResponseUtil.wrapOrNotFound(changeHistory)
    }

    /**
     * `DELETE  /change-histories/:id` : delete the "id" changeHistory.
     *
     * @param id the id of the changeHistory to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/change-histories/{id}")
    fun deleteChangeHistory(@PathVariable id: String): ResponseEntity<Void> {
        log.debug("REST request to delete ChangeHistory : {}", id)

        changeHistoryRepository.deleteById(id)
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build()
    }

    companion object {
        private const val ENTITY_NAME = "changeHistory"
    }
}
