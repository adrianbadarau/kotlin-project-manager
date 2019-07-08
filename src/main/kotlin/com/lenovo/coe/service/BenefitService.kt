package com.lenovo.coe.service

import com.lenovo.coe.domain.Benefit
import com.lenovo.coe.repository.BenefitRepository
import org.slf4j.LoggerFactory

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

import java.util.Optional

/**
 * Service Implementation for managing [Benefit].
 */
@Service
class BenefitService(
    private val benefitRepository: BenefitRepository
) {

    private val log = LoggerFactory.getLogger(BenefitService::class.java)

    /**
     * Save a benefit.
     *
     * @param benefit the entity to save.
     * @return the persisted entity.
     */
    fun save(benefit: Benefit): Benefit {
        log.debug("Request to save Benefit : {}", benefit)
        return benefitRepository.save(benefit)
    }

    /**
     * Get all the benefits.
     *
     * @return the list of entities.
     */
    fun findAll(): MutableList<Benefit> {
        log.debug("Request to get all Benefits")
        return benefitRepository.findAllWithEagerRelationships()
    }

    /**
     * Get all the benefits with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    fun findAllWithEagerRelationships(pageable: Pageable) =
        benefitRepository.findAllWithEagerRelationships(pageable)


    /**
     * Get one benefit by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: String): Optional<Benefit> {
        log.debug("Request to get Benefit : {}", id)
        return benefitRepository.findOneWithEagerRelationships(id)
    }

    /**
     * Delete the benefit by id.
     *
     * @param id the id of the entity.
     */
    fun delete(id: String) {
        log.debug("Request to delete Benefit : {}", id)

        benefitRepository.deleteById(id)
    }
}
