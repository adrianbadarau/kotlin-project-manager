package com.lenovo.coe.web.rest

import com.lenovo.coe.domain.Attachment
import com.lenovo.coe.repository.AttachmentRepository
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
 * REST controller for managing [com.lenovo.coe.domain.Attachment].
 */
@RestController
@RequestMapping("/api")
class AttachmentResource(
    private val attachmentRepository: AttachmentRepository
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /attachments` : Create a new attachment.
     *
     * @param attachment the attachment to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new attachment, or with status `400 (Bad Request)` if the attachment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/attachments")
    fun createAttachment(@Valid @RequestBody attachment: Attachment): ResponseEntity<Attachment> {
        log.debug("REST request to save Attachment : {}", attachment)
        if (attachment.id != null) {
            throw BadRequestAlertException("A new attachment cannot already have an ID", ENTITY_NAME, "idexists")
        }
        val result = attachmentRepository.save(attachment)
        return ResponseEntity.created(URI("/api/attachments/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /attachments` : Updates an existing attachment.
     *
     * @param attachment the attachment to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated attachment,
     * or with status `400 (Bad Request)` if the attachment is not valid,
     * or with status `500 (Internal Server Error)` if the attachment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/attachments")
    fun updateAttachment(@Valid @RequestBody attachment: Attachment): ResponseEntity<Attachment> {
        log.debug("REST request to update Attachment : {}", attachment)
        if (attachment.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = attachmentRepository.save(attachment)
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, attachment.id.toString()))
            .body(result)
    }

    /**
     * `GET  /attachments` : get all the attachments.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the [ResponseEntity] with status `200 (OK)` and the list of attachments in body.
     */
    @GetMapping("/attachments")    
    fun getAllAttachments(@RequestParam(required = false, defaultValue = "false") eagerload: Boolean): MutableList<Attachment> {
        log.debug("REST request to get all Attachments")
        return attachmentRepository.findAllWithEagerRelationships()
    }

    /**
     * `GET  /attachments/:id` : get the "id" attachment.
     *
     * @param id the id of the attachment to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the attachment, or with status `404 (Not Found)`.
     */
    @GetMapping("/attachments/{id}")
    fun getAttachment(@PathVariable id: String): ResponseEntity<Attachment> {
        log.debug("REST request to get Attachment : {}", id)
        val attachment = attachmentRepository.findOneWithEagerRelationships(id)
        return ResponseUtil.wrapOrNotFound(attachment)
    }

    /**
     * `DELETE  /attachments/:id` : delete the "id" attachment.
     *
     * @param id the id of the attachment to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/attachments/{id}")
    fun deleteAttachment(@PathVariable id: String): ResponseEntity<Void> {
        log.debug("REST request to delete Attachment : {}", id)

        attachmentRepository.deleteById(id)
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build()
    }

    companion object {
        private const val ENTITY_NAME = "attachment"
    }
}
