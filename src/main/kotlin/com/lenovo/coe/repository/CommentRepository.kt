package com.lenovo.coe.repository

import com.lenovo.coe.domain.Comment
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data MongoDB repository for the [Comment] entity.
 */
@Suppress("unused")
@Repository
interface CommentRepository : MongoRepository<Comment, String> {
}
