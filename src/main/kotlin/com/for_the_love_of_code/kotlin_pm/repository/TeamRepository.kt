package com.for_the_love_of_code.kotlin_pm.repository

import com.for_the_love_of_code.kotlin_pm.domain.Team
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

import java.util.Optional

/**
 * Spring Data  repository for the [Team] entity.
 */
@Repository
interface TeamRepository : JpaRepository<Team, Long> {

    @Query(value = "select distinct team from Team team left join fetch team.users left join fetch team.tasks",
        countQuery = "select count(distinct team) from Team team")
    fun findAllWithEagerRelationships(pageable: Pageable): Page<Team>

    @Query(value = "select distinct team from Team team left join fetch team.users left join fetch team.tasks")
    fun findAllWithEagerRelationships(): MutableList<Team>

    @Query("select team from Team team left join fetch team.users left join fetch team.tasks where team.id =:id")
    fun findOneWithEagerRelationships(@Param("id") id: Long): Optional<Team>
}
