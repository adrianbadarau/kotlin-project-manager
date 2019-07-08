package com.lenovo.coe.repository

import com.lenovo.coe.domain.Team
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

import java.util.Optional

/**
 * Spring Data MongoDB repository for the [Team] entity.
 */
@Repository
interface TeamRepository : MongoRepository<Team, String> {

    @Query("{}")
    fun findAllWithEagerRelationships(pageable: Pageable): Page<Team>

    @Query("{}")
    fun findAllWithEagerRelationships(): MutableList<Team>

    @Query("{'id': ?0}")
    fun findOneWithEagerRelationships(id: String): Optional<Team>
}
