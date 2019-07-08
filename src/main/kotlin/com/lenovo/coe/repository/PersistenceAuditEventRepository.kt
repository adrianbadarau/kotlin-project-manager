package com.lenovo.coe.repository

import com.lenovo.coe.domain.PersistentAuditEvent
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

import java.time.Instant

/**
 * Spring Data MongoDB repository for the [PersistentAuditEvent] entity.
 */
interface PersistenceAuditEventRepository : MongoRepository<PersistentAuditEvent, String> {

    fun findByPrincipal(principal: String): List<PersistentAuditEvent>

    fun findByAuditEventDateAfter(after: Instant): List<PersistentAuditEvent>

    fun findByPrincipalAndAuditEventDateAfter(principal: String, after: Instant): List<PersistentAuditEvent>

    fun findByPrincipalAndAuditEventDateAfterAndAuditEventType(principal: String, after: Instant, type: String): List<PersistentAuditEvent>

    fun findAllByAuditEventDateBetween(fromDate: Instant, toDate: Instant, pageable: Pageable): Page<PersistentAuditEvent>
}
