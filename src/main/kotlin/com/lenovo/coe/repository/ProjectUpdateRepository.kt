package com.lenovo.coe.repository

import com.lenovo.coe.domain.ProjectUpdate
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data MongoDB repository for the [ProjectUpdate] entity.
 */
@Suppress("unused")
@Repository
interface ProjectUpdateRepository : MongoRepository<ProjectUpdate, String> {
}
