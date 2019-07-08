package com.lenovo.coe.repository

import com.lenovo.coe.domain.BusinessCase
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data MongoDB repository for the [BusinessCase] entity.
 */
@Suppress("unused")
@Repository
interface BusinessCaseRepository : MongoRepository<BusinessCase, String> {
}
