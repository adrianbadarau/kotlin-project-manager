package com.lenovo.coe.repository

import com.lenovo.coe.domain.Milestone
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

import java.util.Optional

/**
 * Spring Data MongoDB repository for the [Milestone] entity.
 */
@Repository
interface MilestoneRepository : MongoRepository<Milestone, String> {

    @Query("{}")
    fun findAllWithEagerRelationships(pageable: Pageable): Page<Milestone>

    @Query("{}")
    fun findAllWithEagerRelationships(): MutableList<Milestone>

    @Query("{'id': ?0}")
    fun findOneWithEagerRelationships(id: String): Optional<Milestone>
}
