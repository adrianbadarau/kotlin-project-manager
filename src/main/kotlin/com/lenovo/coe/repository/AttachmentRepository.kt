package com.lenovo.coe.repository

import com.lenovo.coe.domain.Attachment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

import java.util.Optional

/**
 * Spring Data MongoDB repository for the [Attachment] entity.
 */
@Repository
interface AttachmentRepository : MongoRepository<Attachment, String> {

    @Query("{}")
    fun findAllWithEagerRelationships(pageable: Pageable): Page<Attachment>

    @Query("{}")
    fun findAllWithEagerRelationships(): MutableList<Attachment>

    @Query("{'id': ?0}")
    fun findOneWithEagerRelationships(id: String): Optional<Attachment>
}
