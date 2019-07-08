package com.lenovo.coe.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DBRef
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

import java.io.Serializable
import java.time.Instant

/**
 * A Task.
 */
@Document(collection = "task")
class Task(

    @Id
    var id: String? = null,

    @get: NotNull
    @Field("name")
    var name: String? = null,

    @Field("estimated_date")
    var estimatedDate: Instant? = null,

    @Field("details")
    var details: String? = null,

    @DBRef
    @Field("status")
    @JsonIgnoreProperties("tasks")
    var status: Status? = null,

    @DBRef
    @Field("projectUpdates")
    var projectUpdates: MutableSet<ProjectUpdate> = mutableSetOf(),

    @DBRef
    @Field("comments")
    var comments: MutableSet<Comment> = mutableSetOf(),

    @DBRef
    @Field("users")
    var users: MutableSet<User> = mutableSetOf(),

    @DBRef
    @Field("teams")
    var teams: MutableSet<Team> = mutableSetOf(),

    @DBRef
    @Field("assignedTeam")
    @JsonIgnoreProperties("tasks")
    var assignedTeam: Team? = null,

    @DBRef
    @Field("milestone")
    @JsonIgnoreProperties("tasks")
    var milestone: Milestone? = null

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {

    fun addProjectUpdate(projectUpdate: ProjectUpdate): Task {
        this.projectUpdates.add(projectUpdate)
        return this
    }

    fun removeProjectUpdate(projectUpdate: ProjectUpdate): Task {
        this.projectUpdates.remove(projectUpdate)
        return this
    }

    fun addComment(comment: Comment): Task {
        this.comments.add(comment)
        return this
    }

    fun removeComment(comment: Comment): Task {
        this.comments.remove(comment)
        return this
    }

    fun addUser(user: User): Task {
        this.users.add(user)
        return this
    }

    fun removeUser(user: User): Task {
        this.users.remove(user)
        return this
    }

    fun addTeam(team: Team): Task {
        this.teams.add(team)
        return this
    }

    fun removeTeam(team: Team): Task {
        this.teams.remove(team)
        return this
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Task) return false
        if (other.id == null || id == null) return false

        return id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "Task{" +
        "id=$id" +
        ", name='$name'" +
        ", estimatedDate='$estimatedDate'" +
        ", details='$details'" +
        "}"


    companion object {
        private const val serialVersionUID = 1L
    }
}
