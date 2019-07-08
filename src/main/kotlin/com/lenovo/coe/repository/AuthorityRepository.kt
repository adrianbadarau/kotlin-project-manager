package com.lenovo.coe.repository

import com.lenovo.coe.domain.Authority
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * Spring Data MongoDB repository for the [Authority] entity.
 */

interface AuthorityRepository : MongoRepository<Authority, String>
