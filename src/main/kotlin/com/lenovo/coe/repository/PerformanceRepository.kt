package com.lenovo.coe.repository

import com.lenovo.coe.domain.Performance
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data MongoDB repository for the [Performance] entity.
 */
@Suppress("unused")
@Repository
interface PerformanceRepository : MongoRepository<Performance, String> {
}
