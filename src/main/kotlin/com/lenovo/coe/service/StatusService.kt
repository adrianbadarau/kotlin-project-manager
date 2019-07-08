package com.lenovo.coe.service

import com.lenovo.coe.domain.Status
import com.lenovo.coe.repository.StatusRepository
import org.slf4j.LoggerFactory

import org.springframework.stereotype.Service

import java.util.Optional

/**
 * Service Implementation for managing [Status].
 */
@Service
class StatusService(
    private val statusRepository: StatusRepository
) {

    private val log = LoggerFactory.getLogger(StatusService::class.java)

    /**
     * Save a status.
     *
     * @param status the entity to save.
     * @return the persisted entity.
     */
    fun save(status: Status): Status {
        log.debug("Request to save Status : {}", status)
        return statusRepository.save(status)
    }

    /**
     * Get all the statuses.
     *
     * @return the list of entities.
     */
    fun findAll(): MutableList<Status> {
        log.debug("Request to get all Statuses")
        return statusRepository.findAll()
    }

    /**
     * Get one status by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: String): Optional<Status> {
        log.debug("Request to get Status : {}", id)
        return statusRepository.findById(id)
    }

    /**
     * Delete the status by id.
     *
     * @param id the id of the entity.
     */
    fun delete(id: String) {
        log.debug("Request to delete Status : {}", id)

        statusRepository.deleteById(id)
    }
}
