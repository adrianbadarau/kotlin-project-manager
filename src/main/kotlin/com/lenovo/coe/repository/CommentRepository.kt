package com.lenovo.coe.repository

import com.lenovo.coe.domain.Comment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

import java.util.Optional

/**
 * Spring Data MongoDB repository for the [Comment] entity.
 */
@Repository
interface CommentRepository : MongoRepository<Comment, String> {

    @Query("{}")
    fun findAllWithEagerRelationships(pageable: Pageable): Page<Comment>

    @Query("{}")
    fun findAllWithEagerRelationships(): MutableList<Comment>

    @Query("{'id': ?0}")
    fun findOneWithEagerRelationships(id: String): Optional<Comment>
}
