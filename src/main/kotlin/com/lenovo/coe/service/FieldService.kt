package com.lenovo.coe.service

import com.lenovo.coe.domain.Field
import com.lenovo.coe.repository.FieldRepository
import org.slf4j.LoggerFactory

import org.springframework.stereotype.Service

import java.util.Optional

/**
 * Service Implementation for managing [Field].
 */
@Service
class FieldService(
    private val fieldRepository: FieldRepository
) {

    private val log = LoggerFactory.getLogger(FieldService::class.java)

    /**
     * Save a field.
     *
     * @param field the entity to save.
     * @return the persisted entity.
     */
    fun save(field: Field): Field {
        log.debug("Request to save Field : {}", field)
        return fieldRepository.save(field)
    }

    /**
     * Get all the fields.
     *
     * @return the list of entities.
     */
    fun findAll(): MutableList<Field> {
        log.debug("Request to get all Fields")
        return fieldRepository.findAll()
    }

    /**
     * Get one field by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: String): Optional<Field> {
        log.debug("Request to get Field : {}", id)
        return fieldRepository.findById(id)
    }

    /**
     * Delete the field by id.
     *
     * @param id the id of the entity.
     */
    fun delete(id: String) {
        log.debug("Request to delete Field : {}", id)

        fieldRepository.deleteById(id)
    }
}
