package com.lenovo.coe.web.rest

import com.lenovo.coe.domain.Team
import com.lenovo.coe.service.TeamService
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
 * REST controller for managing [com.lenovo.coe.domain.Team].
 */
@RestController
@RequestMapping("/api")
class TeamResource(
    private val teamService: TeamService
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /teams` : Create a new team.
     *
     * @param team the team to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new team, or with status `400 (Bad Request)` if the team has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/teams")
    fun createTeam(@Valid @RequestBody team: Team): ResponseEntity<Team> {
        log.debug("REST request to save Team : {}", team)
        if (team.id != null) {
            throw BadRequestAlertException("A new team cannot already have an ID", ENTITY_NAME, "idexists")
        }
        val result = teamService.save(team)
        return ResponseEntity.created(URI("/api/teams/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /teams` : Updates an existing team.
     *
     * @param team the team to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated team,
     * or with status `400 (Bad Request)` if the team is not valid,
     * or with status `500 (Internal Server Error)` if the team couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/teams")
    fun updateTeam(@Valid @RequestBody team: Team): ResponseEntity<Team> {
        log.debug("REST request to update Team : {}", team)
        if (team.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = teamService.save(team)
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, team.id.toString()))
            .body(result)
    }

    /**
     * `GET  /teams` : get all the teams.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the [ResponseEntity] with status `200 (OK)` and the list of teams in body.
     */
    @GetMapping("/teams")    
    fun getAllTeams(@RequestParam(required = false, defaultValue = "false") eagerload: Boolean): MutableList<Team> {
        log.debug("REST request to get all Teams")
        return teamService.findAll()
    }

    /**
     * `GET  /teams/:id` : get the "id" team.
     *
     * @param id the id of the team to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the team, or with status `404 (Not Found)`.
     */
    @GetMapping("/teams/{id}")
    fun getTeam(@PathVariable id: String): ResponseEntity<Team> {
        log.debug("REST request to get Team : {}", id)
        val team = teamService.findOne(id)
        return ResponseUtil.wrapOrNotFound(team)
    }

    /**
     * `DELETE  /teams/:id` : delete the "id" team.
     *
     * @param id the id of the team to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/teams/{id}")
    fun deleteTeam(@PathVariable id: String): ResponseEntity<Void> {
        log.debug("REST request to delete Team : {}", id)
        teamService.delete(id)
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build()
    }

    companion object {
        private const val ENTITY_NAME = "team"
    }
}
