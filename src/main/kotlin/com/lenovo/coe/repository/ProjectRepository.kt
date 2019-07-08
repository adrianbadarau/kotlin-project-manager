package com.lenovo.coe.repository

import com.lenovo.coe.domain.Project
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

import java.util.Optional

/**
 * Spring Data MongoDB repository for the [Project] entity.
 */
@Repository
interface ProjectRepository : MongoRepository<Project, String> {

    @Query("{}")
    fun findAllWithEagerRelationships(pageable: Pageable): Page<Project>

    @Query("{}")
    fun findAllWithEagerRelationships(): MutableList<Project>

    @Query("{'id': ?0}")
    fun findOneWithEagerRelationships(id: String): Optional<Project>
}
