package com.lenovo.coe.web.rest

import com.lenovo.coe.domain.TaskType
import com.lenovo.coe.repository.TaskTypeRepository
import com.lenovo.coe.web.rest.errors.BadRequestAlertException

import io.github.jhipster.web.util.HeaderUtil
import io.github.jhipster.web.util.ResponseUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import javax.validation.Valid
import java.net.URI
import java.net.URISyntaxException

/**
 * REST controller for managing [com.lenovo.coe.domain.TaskType].
 */
@RestController
@RequestMapping("/api")
class TaskTypeResource(
    private val taskTypeRepository: TaskTypeRepository
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /task-types` : Create a new taskType.
     *
     * @param taskType the taskType to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new taskType, or with status `400 (Bad Request)` if the taskType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/task-types")
    fun createTaskType(@Valid @RequestBody taskType: TaskType): ResponseEntity<TaskType> {
        log.debug("REST request to save TaskType : {}", taskType)
        if (taskType.id != null) {
            throw BadRequestAlertException("A new taskType cannot already have an ID", ENTITY_NAME, "idexists")
        }
        val result = taskTypeRepository.save(taskType)
        return ResponseEntity.created(URI("/api/task-types/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /task-types` : Updates an existing taskType.
     *
     * @param taskType the taskType to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated taskType,
     * or with status `400 (Bad Request)` if the taskType is not valid,
     * or with status `500 (Internal Server Error)` if the taskType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/task-types")
    fun updateTaskType(@Valid @RequestBody taskType: TaskType): ResponseEntity<TaskType> {
        log.debug("REST request to update TaskType : {}", taskType)
        if (taskType.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = taskTypeRepository.save(taskType)
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, taskType.id.toString()))
            .body(result)
    }

    /**
     * `GET  /task-types` : get all the taskTypes.
     *
     * @return the [ResponseEntity] with status `200 (OK)` and the list of taskTypes in body.
     */
    @GetMapping("/task-types")    
    fun getAllTaskTypes(): MutableList<TaskType> {
        log.debug("REST request to get all TaskTypes")
        return taskTypeRepository.findAll()
    }

    /**
     * `GET  /task-types/:id` : get the "id" taskType.
     *
     * @param id the id of the taskType to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the taskType, or with status `404 (Not Found)`.
     */
    @GetMapping("/task-types/{id}")
    fun getTaskType(@PathVariable id: String): ResponseEntity<TaskType> {
        log.debug("REST request to get TaskType : {}", id)
        val taskType = taskTypeRepository.findById(id)
        return ResponseUtil.wrapOrNotFound(taskType)
    }

    /**
     * `DELETE  /task-types/:id` : delete the "id" taskType.
     *
     * @param id the id of the taskType to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/task-types/{id}")
    fun deleteTaskType(@PathVariable id: String): ResponseEntity<Void> {
        log.debug("REST request to delete TaskType : {}", id)

        taskTypeRepository.deleteById(id)
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build()
    }

    companion object {
        private const val ENTITY_NAME = "taskType"
    }
}
