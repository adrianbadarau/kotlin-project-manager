package com.lenovo.coe.service

import com.lenovo.coe.domain.BusinessCase
import com.lenovo.coe.repository.BusinessCaseRepository
import org.slf4j.LoggerFactory

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

import java.util.Optional

/**
 * Service Implementation for managing [BusinessCase].
 */
@Service
class BusinessCaseService(
    private val businessCaseRepository: BusinessCaseRepository
) {

    private val log = LoggerFactory.getLogger(BusinessCaseService::class.java)

    /**
     * Save a businessCase.
     *
     * @param businessCase the entity to save.
     * @return the persisted entity.
     */
    fun save(businessCase: BusinessCase): BusinessCase {
        log.debug("Request to save BusinessCase : {}", businessCase)
        return businessCaseRepository.save(businessCase)
    }

    /**
     * Get all the businessCases.
     *
     * @return the list of entities.
     */
    fun findAll(): MutableList<BusinessCase> {
        log.debug("Request to get all BusinessCases")
        return businessCaseRepository.findAllWithEagerRelationships()
    }

    /**
     * Get all the businessCases with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    fun findAllWithEagerRelationships(pageable: Pageable) =
        businessCaseRepository.findAllWithEagerRelationships(pageable)



    /**
     *  Get all the businessCases where Project is `null`.
     *  @return the list of entities.
     */
    fun findAllWhereProjectIsNull(): MutableList<BusinessCase>  {
        log.debug("Request to get all businessCases where Project is null")
        return businessCaseRepository.findAll()
            .filterTo(mutableListOf()) { it.project == null }
    }

    /**
     * Get one businessCase by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: String): Optional<BusinessCase> {
        log.debug("Request to get BusinessCase : {}", id)
        return businessCaseRepository.findOneWithEagerRelationships(id)
    }

    /**
     * Delete the businessCase by id.
     *
     * @param id the id of the entity.
     */
    fun delete(id: String) {
        log.debug("Request to delete BusinessCase : {}", id)

        businessCaseRepository.deleteById(id)
    }
}
