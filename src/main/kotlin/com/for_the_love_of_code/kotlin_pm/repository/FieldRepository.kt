package com.for_the_love_of_code.kotlin_pm.repository

import com.for_the_love_of_code.kotlin_pm.domain.Field
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * Spring Data  repository for the [Field] entity.
 */
@SuppressWarnings("unused")
@Repository
interface FieldRepository : JpaRepository<Field, Long> {
}
