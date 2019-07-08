package com.lenovo.coe.service

import com.lenovo.coe.domain.Delivrable
import com.lenovo.coe.repository.DelivrableRepository
import org.slf4j.LoggerFactory

import org.springframework.stereotype.Service

import java.util.Optional

/**
 * Service Implementation for managing [Delivrable].
 */
@Service
class DelivrableService(
    private val delivrableRepository: DelivrableRepository
) {

    private val log = LoggerFactory.getLogger(DelivrableService::class.java)

    /**
     * Save a delivrable.
     *
     * @param delivrable the entity to save.
     * @return the persisted entity.
     */
    fun save(delivrable: Delivrable): Delivrable {
        log.debug("Request to save Delivrable : {}", delivrable)
        return delivrableRepository.save(delivrable)
    }

    /**
     * Get all the delivrables.
     *
     * @return the list of entities.
     */
    fun findAll(): MutableList<Delivrable> {
        log.debug("Request to get all Delivrables")
        return delivrableRepository.findAll()
    }

    /**
     * Get one delivrable by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: String): Optional<Delivrable> {
        log.debug("Request to get Delivrable : {}", id)
        return delivrableRepository.findById(id)
    }

    /**
     * Delete the delivrable by id.
     *
     * @param id the id of the entity.
     */
    fun delete(id: String) {
        log.debug("Request to delete Delivrable : {}", id)

        delivrableRepository.deleteById(id)
    }
}
