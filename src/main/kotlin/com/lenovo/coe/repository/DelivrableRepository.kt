package com.lenovo.coe.repository

import com.lenovo.coe.domain.Delivrable
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data MongoDB repository for the [Delivrable] entity.
 */
@Suppress("unused")
@Repository
interface DelivrableRepository : MongoRepository<Delivrable, String> {
}
