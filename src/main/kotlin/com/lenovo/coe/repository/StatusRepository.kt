package com.lenovo.coe.repository

import com.lenovo.coe.domain.Status
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data MongoDB repository for the [Status] entity.
 */
@Suppress("unused")
@Repository
interface StatusRepository : MongoRepository<Status, String> {
}
