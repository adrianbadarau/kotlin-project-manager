package com.lenovo.coe.service

import com.lenovo.coe.domain.Comment
import com.lenovo.coe.repository.CommentRepository
import org.slf4j.LoggerFactory

import org.springframework.stereotype.Service

import java.util.Optional

/**
 * Service Implementation for managing [Comment].
 */
@Service
class CommentService(
    private val commentRepository: CommentRepository
) {

    private val log = LoggerFactory.getLogger(CommentService::class.java)

    /**
     * Save a comment.
     *
     * @param comment the entity to save.
     * @return the persisted entity.
     */
    fun save(comment: Comment): Comment {
        log.debug("Request to save Comment : {}", comment)
        return commentRepository.save(comment)
    }

    /**
     * Get all the comments.
     *
     * @return the list of entities.
     */
    fun findAll(): MutableList<Comment> {
        log.debug("Request to get all Comments")
        return commentRepository.findAll()
    }

    /**
     * Get one comment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: String): Optional<Comment> {
        log.debug("Request to get Comment : {}", id)
        return commentRepository.findById(id)
    }

    /**
     * Delete the comment by id.
     *
     * @param id the id of the entity.
     */
    fun delete(id: String) {
        log.debug("Request to delete Comment : {}", id)

        commentRepository.deleteById(id)
    }
}
