package com.lenovo.coe.repository

import com.lenovo.coe.domain.ChangeHistory
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data MongoDB repository for the [ChangeHistory] entity.
 */
@Suppress("unused")
@Repository
interface ChangeHistoryRepository : MongoRepository<ChangeHistory, String> {
}
