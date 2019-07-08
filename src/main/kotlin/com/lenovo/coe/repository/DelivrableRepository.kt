package com.lenovo.coe.repository

import com.lenovo.coe.domain.Delivrable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

import java.util.Optional

/**
 * Spring Data MongoDB repository for the [Delivrable] entity.
 */
@Repository
interface DelivrableRepository : MongoRepository<Delivrable, String> {

    @Query("{}")
    fun findAllWithEagerRelationships(pageable: Pageable): Page<Delivrable>

    @Query("{}")
    fun findAllWithEagerRelationships(): MutableList<Delivrable>

    @Query("{'id': ?0}")
    fun findOneWithEagerRelationships(id: String): Optional<Delivrable>
}
