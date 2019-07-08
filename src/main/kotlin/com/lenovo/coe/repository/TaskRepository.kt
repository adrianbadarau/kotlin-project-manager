package com.lenovo.coe.repository

import com.lenovo.coe.domain.Task
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

import java.util.Optional

/**
 * Spring Data MongoDB repository for the [Task] entity.
 */
@Repository
interface TaskRepository : MongoRepository<Task, String> {

    @Query("{}")
    fun findAllWithEagerRelationships(pageable: Pageable): Page<Task>

    @Query("{}")
    fun findAllWithEagerRelationships(): MutableList<Task>

    @Query("{'id': ?0}")
    fun findOneWithEagerRelationships(id: String): Optional<Task>
}
