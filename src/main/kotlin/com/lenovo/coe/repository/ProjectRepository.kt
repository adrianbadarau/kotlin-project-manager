package com.lenovo.coe.repository

import com.lenovo.coe.domain.Project
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data MongoDB repository for the [Project] entity.
 */
@Suppress("unused")
@Repository
interface ProjectRepository : MongoRepository<Project, String> {
}
