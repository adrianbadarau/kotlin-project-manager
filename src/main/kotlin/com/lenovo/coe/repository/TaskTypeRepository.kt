package com.lenovo.coe.repository

import com.lenovo.coe.domain.TaskType
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data MongoDB repository for the [TaskType] entity.
 */
@Suppress("unused")
@Repository
interface TaskTypeRepository : MongoRepository<TaskType, String> {
}
