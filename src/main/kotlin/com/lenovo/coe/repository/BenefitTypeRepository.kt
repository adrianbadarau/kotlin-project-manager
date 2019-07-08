package com.lenovo.coe.repository

import com.lenovo.coe.domain.BenefitType
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data MongoDB repository for the [BenefitType] entity.
 */
@Suppress("unused")
@Repository
interface BenefitTypeRepository : MongoRepository<BenefitType, String> {
}
