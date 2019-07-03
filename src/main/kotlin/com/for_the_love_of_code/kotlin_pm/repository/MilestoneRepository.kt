package com.for_the_love_of_code.kotlin_pm.repository

import com.for_the_love_of_code.kotlin_pm.domain.Milestone
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

import java.util.Optional

/**
 * Spring Data  repository for the [Milestone] entity.
 */
@Repository
interface MilestoneRepository : JpaRepository<Milestone, Long> {

    @Query(value = "select distinct milestone from Milestone milestone left join fetch milestone.fields left join fetch milestone.teams left join fetch milestone.users",
        countQuery = "select count(distinct milestone) from Milestone milestone")
    fun findAllWithEagerRelationships(pageable: Pageable): Page<Milestone>

    @Query(value = "select distinct milestone from Milestone milestone left join fetch milestone.fields left join fetch milestone.teams left join fetch milestone.users")
    fun findAllWithEagerRelationships(): MutableList<Milestone>

    @Query("select milestone from Milestone milestone left join fetch milestone.fields left join fetch milestone.teams left join fetch milestone.users where milestone.id =:id")
    fun findOneWithEagerRelationships(@Param("id") id: Long): Optional<Milestone>
}
