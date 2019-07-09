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
import java.time.Instant

/**
 * A Milestone.
 */
@Document(collection = "milestone")
class Milestone(

    @Id
    var id: String? = null,

    @get: NotNull
    @Field("name")
    var name: String? = null,

    @Field("estimated_end_date")
    var estimatedEndDate: Instant? = null,

    @Field("end_date")
    var endDate: Instant? = null,

    @DBRef
    @Field("project")
    @JsonIgnoreProperties("milestones")
    var project: Project? = null,

    @DBRef
    @Field("status")
    @JsonIgnoreProperties("milestones")
    var status: Status? = null,

    @DBRef
    @Field("teams")
    var teams: MutableSet<Team> = mutableSetOf(),

    @DBRef
    @Field("users")
    var users: MutableSet<User> = mutableSetOf(),

    @DBRef
    @Field("task")
    var tasks: MutableSet<Task> = mutableSetOf(),

    @DBRef
    @Field("attachments")
    @JsonIgnore
    var attachments: MutableSet<Attachment> = mutableSetOf(),

    @DBRef
    @Field("comments")
    @JsonIgnore
    var comments: MutableSet<Comment> = mutableSetOf()

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {

    fun addTeam(team: Team): Milestone {
        this.teams.add(team)
        return this
    }

    fun removeTeam(team: Team): Milestone {
        this.teams.remove(team)
        return this
    }

    fun addUser(user: User): Milestone {
        this.users.add(user)
        return this
    }

    fun removeUser(user: User): Milestone {
        this.users.remove(user)
        return this
    }

    fun addTask(task: Task): Milestone {
        this.tasks.add(task)
        task.milestone = this
        return this
    }

    fun removeTask(task: Task): Milestone {
        this.tasks.remove(task)
        task.milestone = null
        return this
    }

    fun addAttachment(attachment: Attachment): Milestone {
        this.attachments.add(attachment)
        attachment.milestones.add(this)
        return this
    }

    fun removeAttachment(attachment: Attachment): Milestone {
        this.attachments.remove(attachment)
        attachment.milestones.remove(this)
        return this
    }

    fun addComment(comment: Comment): Milestone {
        this.comments.add(comment)
        comment.milestones.add(this)
        return this
    }

    fun removeComment(comment: Comment): Milestone {
        this.comments.remove(comment)
        comment.milestones.remove(this)
        return this
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Milestone) return false
        if (other.id == null || id == null) return false

        return id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "Milestone{" +
        "id=$id" +
        ", name='$name'" +
        ", estimatedEndDate='$estimatedEndDate'" +
        ", endDate='$endDate'" +
        "}"


    companion object {
        private const val serialVersionUID = 1L
    }
}
