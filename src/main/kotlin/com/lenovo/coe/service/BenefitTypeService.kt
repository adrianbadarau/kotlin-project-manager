package com.lenovo.coe.service

import com.lenovo.coe.domain.BenefitType
import com.lenovo.coe.repository.BenefitTypeRepository
import org.slf4j.LoggerFactory

import org.springframework.stereotype.Service

import java.util.Optional

/**
 * Service Implementation for managing [BenefitType].
 */
@Service
class BenefitTypeService(
    private val benefitTypeRepository: BenefitTypeRepository
) {

    private val log = LoggerFactory.getLogger(BenefitTypeService::class.java)

    /**
     * Save a benefitType.
     *
     * @param benefitType the entity to save.
     * @return the persisted entity.
     */
    fun save(benefitType: BenefitType): BenefitType {
        log.debug("Request to save BenefitType : {}", benefitType)
        return benefitTypeRepository.save(benefitType)
    }

    /**
     * Get all the benefitTypes.
     *
     * @return the list of entities.
     */
    fun findAll(): MutableList<BenefitType> {
        log.debug("Request to get all BenefitTypes")
        return benefitTypeRepository.findAll()
    }

    /**
     * Get one benefitType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: String): Optional<BenefitType> {
        log.debug("Request to get BenefitType : {}", id)
        return benefitTypeRepository.findById(id)
    }

    /**
     * Delete the benefitType by id.
     *
     * @param id the id of the entity.
     */
    fun delete(id: String) {
        log.debug("Request to delete BenefitType : {}", id)

        benefitTypeRepository.deleteById(id)
    }
}
