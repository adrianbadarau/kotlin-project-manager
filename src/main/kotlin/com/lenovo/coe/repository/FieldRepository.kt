package com.lenovo.coe.repository

import com.lenovo.coe.domain.Field
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data MongoDB repository for the [Field] entity.
 */
@Suppress("unused")
@Repository
interface FieldRepository : MongoRepository<Field, String> {
}
