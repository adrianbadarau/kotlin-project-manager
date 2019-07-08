package com.lenovo.coe.repository

import com.lenovo.coe.domain.BusinessCase
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

import java.util.Optional

/**
 * Spring Data MongoDB repository for the [BusinessCase] entity.
 */
@Repository
interface BusinessCaseRepository : MongoRepository<BusinessCase, String> {

    @Query("{}")
    fun findAllWithEagerRelationships(pageable: Pageable): Page<BusinessCase>

    @Query("{}")
    fun findAllWithEagerRelationships(): MutableList<BusinessCase>

    @Query("{'id': ?0}")
    fun findOneWithEagerRelationships(id: String): Optional<BusinessCase>
}
