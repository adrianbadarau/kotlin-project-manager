package com.lenovo.coe.web.rest

import com.lenovo.coe.domain.Comment
import com.lenovo.coe.repository.CommentRepository
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
 * REST controller for managing [com.lenovo.coe.domain.Comment].
 */
@RestController
@RequestMapping("/api")
class CommentResource(
    private val commentRepository: CommentRepository
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /comments` : Create a new comment.
     *
     * @param comment the comment to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new comment, or with status `400 (Bad Request)` if the comment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/comments")
    fun createComment(@Valid @RequestBody comment: Comment): ResponseEntity<Comment> {
        log.debug("REST request to save Comment : {}", comment)
        if (comment.id != null) {
            throw BadRequestAlertException("A new comment cannot already have an ID", ENTITY_NAME, "idexists")
        }
        val result = commentRepository.save(comment)
        return ResponseEntity.created(URI("/api/comments/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /comments` : Updates an existing comment.
     *
     * @param comment the comment to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated comment,
     * or with status `400 (Bad Request)` if the comment is not valid,
     * or with status `500 (Internal Server Error)` if the comment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/comments")
    fun updateComment(@Valid @RequestBody comment: Comment): ResponseEntity<Comment> {
        log.debug("REST request to update Comment : {}", comment)
        if (comment.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = commentRepository.save(comment)
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, comment.id.toString()))
            .body(result)
    }

    /**
     * `GET  /comments` : get all the comments.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the [ResponseEntity] with status `200 (OK)` and the list of comments in body.
     */
    @GetMapping("/comments")    
    fun getAllComments(@RequestParam(required = false, defaultValue = "false") eagerload: Boolean): MutableList<Comment> {
        log.debug("REST request to get all Comments")
        return commentRepository.findAllWithEagerRelationships()
    }

    /**
     * `GET  /comments/:id` : get the "id" comment.
     *
     * @param id the id of the comment to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the comment, or with status `404 (Not Found)`.
     */
    @GetMapping("/comments/{id}")
    fun getComment(@PathVariable id: String): ResponseEntity<Comment> {
        log.debug("REST request to get Comment : {}", id)
        val comment = commentRepository.findOneWithEagerRelationships(id)
        return ResponseUtil.wrapOrNotFound(comment)
    }

    /**
     * `DELETE  /comments/:id` : delete the "id" comment.
     *
     * @param id the id of the comment to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/comments/{id}")
    fun deleteComment(@PathVariable id: String): ResponseEntity<Void> {
        log.debug("REST request to delete Comment : {}", id)

        commentRepository.deleteById(id)
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build()
    }

    companion object {
        private const val ENTITY_NAME = "comment"
    }
}
