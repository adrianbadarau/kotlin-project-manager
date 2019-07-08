package com.lenovo.coe.service

import com.lenovo.coe.domain.Task
import com.lenovo.coe.repository.TaskRepository
import org.slf4j.LoggerFactory

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

import java.util.Optional

/**
 * Service Implementation for managing [Task].
 */
@Service
class TaskService(
    private val taskRepository: TaskRepository
) {

    private val log = LoggerFactory.getLogger(TaskService::class.java)

    /**
     * Save a task.
     *
     * @param task the entity to save.
     * @return the persisted entity.
     */
    fun save(task: Task): Task {
        log.debug("Request to save Task : {}", task)
        return taskRepository.save(task)
    }

    /**
     * Get all the tasks.
     *
     * @return the list of entities.
     */
    fun findAll(): MutableList<Task> {
        log.debug("Request to get all Tasks")
        return taskRepository.findAllWithEagerRelationships()
    }

    /**
     * Get all the tasks with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    fun findAllWithEagerRelationships(pageable: Pageable) =
        taskRepository.findAllWithEagerRelationships(pageable)


    /**
     * Get one task by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: String): Optional<Task> {
        log.debug("Request to get Task : {}", id)
        return taskRepository.findOneWithEagerRelationships(id)
    }

    /**
     * Delete the task by id.
     *
     * @param id the id of the entity.
     */
    fun delete(id: String) {
        log.debug("Request to delete Task : {}", id)

        taskRepository.deleteById(id)
    }
}
