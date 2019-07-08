package com.lenovo.coe.repository

import com.lenovo.coe.domain.Benefit
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

import java.util.Optional

/**
 * Spring Data MongoDB repository for the [Benefit] entity.
 */
@Repository
interface BenefitRepository : MongoRepository<Benefit, String> {

    @Query("{}")
    fun findAllWithEagerRelationships(pageable: Pageable): Page<Benefit>

    @Query("{}")
    fun findAllWithEagerRelationships(): MutableList<Benefit>

    @Query("{'id': ?0}")
    fun findOneWithEagerRelationships(id: String): Optional<Benefit>
}
