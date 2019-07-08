package com.lenovo.coe.service

import com.lenovo.coe.domain.Performance
import com.lenovo.coe.repository.PerformanceRepository
import org.slf4j.LoggerFactory

import org.springframework.stereotype.Service

import java.util.Optional

/**
 * Service Implementation for managing [Performance].
 */
@Service
class PerformanceService(
    private val performanceRepository: PerformanceRepository
) {

    private val log = LoggerFactory.getLogger(PerformanceService::class.java)

    /**
     * Save a performance.
     *
     * @param performance the entity to save.
     * @return the persisted entity.
     */
    fun save(performance: Performance): Performance {
        log.debug("Request to save Performance : {}", performance)
        return performanceRepository.save(performance)
    }

    /**
     * Get all the performances.
     *
     * @return the list of entities.
     */
    fun findAll(): MutableList<Performance> {
        log.debug("Request to get all Performances")
        return performanceRepository.findAll()
    }

    /**
     * Get one performance by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: String): Optional<Performance> {
        log.debug("Request to get Performance : {}", id)
        return performanceRepository.findById(id)
    }

    /**
     * Delete the performance by id.
     *
     * @param id the id of the entity.
     */
    fun delete(id: String) {
        log.debug("Request to delete Performance : {}", id)

        performanceRepository.deleteById(id)
    }
}
