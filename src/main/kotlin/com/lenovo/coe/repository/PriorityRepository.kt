package com.lenovo.coe.repository

import com.lenovo.coe.domain.Priority
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data MongoDB repository for the [Priority] entity.
 */
@Suppress("unused")
@Repository
interface PriorityRepository : MongoRepository<Priority, String> {
}
