package com.for_the_love_of_code.kotlin_pm.repository

import com.for_the_love_of_code.kotlin_pm.domain.Task
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

import java.util.Optional

/**
 * Spring Data  repository for the [Task] entity.
 */
@Repository
interface TaskRepository : JpaRepository<Task, Long> {

    @Query(value = "select distinct task from Task task left join fetch task.users",
        countQuery = "select count(distinct task) from Task task")
    fun findAllWithEagerRelationships(pageable: Pageable): Page<Task>

    @Query(value = "select distinct task from Task task left join fetch task.users")
    fun findAllWithEagerRelationships(): MutableList<Task>

    @Query("select task from Task task left join fetch task.users where task.id =:id")
    fun findOneWithEagerRelationships(@Param("id") id: Long): Optional<Task>
}
