package com.lenovo.coe.service

import com.lenovo.coe.domain.Milestone
import com.lenovo.coe.repository.MilestoneRepository
import org.slf4j.LoggerFactory

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

import java.util.Optional

/**
 * Service Implementation for managing [Milestone].
 */
@Service
class MilestoneService(
    private val milestoneRepository: MilestoneRepository
) {

    private val log = LoggerFactory.getLogger(MilestoneService::class.java)

    /**
     * Save a milestone.
     *
     * @param milestone the entity to save.
     * @return the persisted entity.
     */
    fun save(milestone: Milestone): Milestone {
        log.debug("Request to save Milestone : {}", milestone)
        return milestoneRepository.save(milestone)
    }

    /**
     * Get all the milestones.
     *
     * @return the list of entities.
     */
    fun findAll(): MutableList<Milestone> {
        log.debug("Request to get all Milestones")
        return milestoneRepository.findAllWithEagerRelationships()
    }

    /**
     * Get all the milestones with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    fun findAllWithEagerRelationships(pageable: Pageable) =
        milestoneRepository.findAllWithEagerRelationships(pageable)


    /**
     * Get one milestone by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: String): Optional<Milestone> {
        log.debug("Request to get Milestone : {}", id)
        return milestoneRepository.findOneWithEagerRelationships(id)
    }

    /**
     * Delete the milestone by id.
     *
     * @param id the id of the entity.
     */
    fun delete(id: String) {
        log.debug("Request to delete Milestone : {}", id)

        milestoneRepository.deleteById(id)
    }
}
