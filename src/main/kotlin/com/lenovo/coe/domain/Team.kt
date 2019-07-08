package com.lenovo.coe.domain

import com.fasterxml.jackson.annotation.JsonIgnore
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

/**
 * A Team.
 */
@Document(collection = "team")
class Team(

    @Id
    var id: String? = null,

    @get: NotNull
    @Field("name")
    var name: String? = null,


    @DBRef
    @Field("users")
    var users: MutableSet<User> = mutableSetOf(),

    @DBRef
    @Field("project")
    @JsonIgnoreProperties("teams")
    var project: Project? = null,

    @DBRef
    @Field("tasks")
    @JsonIgnore
    var tasks: MutableSet<Task> = mutableSetOf()

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {


    fun addUser(user: User): Team {
        this.users.add(user)
        return this
    }

    fun removeUser(user: User): Team {
        this.users.remove(user)
        return this
    }

    fun addTask(task: Task): Team {
        this.tasks.add(task)
        task.teams.add(this)
        return this
    }

    fun removeTask(task: Task): Team {
        this.tasks.remove(task)
        task.teams.remove(this)
        return this
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Team) return false
        if (other.id == null || id == null) return false

        return id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "Team{" +
        "id=$id" +
        ", name='$name'" +
        "}"


    companion object {
        private const val serialVersionUID = 1L
    }
}
