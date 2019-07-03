package com.for_the_love_of_code.kotlin_pm.repository

import com.for_the_love_of_code.kotlin_pm.domain.Project
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

import java.util.Optional

/**
 * Spring Data  repository for the [Project] entity.
 */
@Repository
interface ProjectRepository : JpaRepository<Project, Long> {

    @Query("select project from Project project where project.owner.login = ?#{principal.username}")
    fun findByOwnerIsCurrentUser(): MutableList<Project>

    @Query(value = "select distinct project from Project project left join fetch project.fields",
        countQuery = "select count(distinct project) from Project project")
    fun findAllWithEagerRelationships(pageable: Pageable): Page<Project>

    @Query(value = "select distinct project from Project project left join fetch project.fields")
    fun findAllWithEagerRelationships(): MutableList<Project>

    @Query("select project from Project project left join fetch project.fields where project.id =:id")
    fun findOneWithEagerRelationships(@Param("id") id: Long): Optional<Project>
}
