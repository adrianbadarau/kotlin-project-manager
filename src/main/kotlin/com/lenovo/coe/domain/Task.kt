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
 * A Task.
 */
@Document(collection = "task")
class Task(

    @Id
    var id: String? = null,

    @get: NotNull
    @Field("name")
    var name: String? = null,

    @get: NotNull
    @Field("description")
    var description: String? = null,

    @Field("estimated_time")
    var estimatedTime: Float? = null,

    @Field("spent_time")
    var spentTime: Float? = null,

    @DBRef
    @Field("milestone")
    @JsonIgnoreProperties("tasks")
    var milestone: Milestone? = null,

    @DBRef
    @Field("status")
    @JsonIgnoreProperties("tasks")
    var status: Status? = null,

    @DBRef
    @Field("taskType")
    @JsonIgnoreProperties("tasks")
    var taskType: TaskType? = null,

    @DBRef
    @Field("priority")
    @JsonIgnoreProperties("tasks")
    var priority: Priority? = null,

    @DBRef
    @Field("assignee")
    @JsonIgnoreProperties("tasks")
    var assignee: User? = null,

    @DBRef
    @Field("parent")
    @JsonIgnoreProperties("subTasks")
    var parent: Task? = null,

    @DBRef
    @Field("users")
    var users: MutableSet<User> = mutableSetOf(),

    @DBRef
    @Field("subTasks")
    var subTasks: MutableSet<Task> = mutableSetOf(),

    @DBRef
    @Field("attachments")
    @JsonIgnore
    var attachments: MutableSet<Attachment> = mutableSetOf(),

    @DBRef
    @Field("comments")
    @JsonIgnore
    var comments: MutableSet<Comment> = mutableSetOf(),

    @DBRef
    @Field("teams")
    @JsonIgnore
    var teams: MutableSet<Team> = mutableSetOf()

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {

    fun addUser(user: User): Task {
        this.users.add(user)
        return this
    }

    fun removeUser(user: User): Task {
        this.users.remove(user)
        return this
    }

    fun addSubTasks(task: Task): Task {
        this.subTasks.add(task)
        task.parent = this
        return this
    }

    fun removeSubTasks(task: Task): Task {
        this.subTasks.remove(task)
        task.parent = null
        return this
    }

    fun addAttachment(attachment: Attachment): Task {
        this.attachments.add(attachment)
        attachment.tasks.add(this)
        return this
    }

    fun removeAttachment(attachment: Attachment): Task {
        this.attachments.remove(attachment)
        attachment.tasks.remove(this)
        return this
    }

    fun addComment(comment: Comment): Task {
        this.comments.add(comment)
        comment.tasks.add(this)
        return this
    }

    fun removeComment(comment: Comment): Task {
        this.comments.remove(comment)
        comment.tasks.remove(this)
        return this
    }

    fun addTeam(team: Team): Task {
        this.teams.add(team)
        team.tasks.add(this)
        return this
    }

    fun removeTeam(team: Team): Task {
        this.teams.remove(team)
        team.tasks.remove(this)
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
        ", description='$description'" +
        ", estimatedTime=$estimatedTime" +
        ", spentTime=$spentTime" +
        "}"


    companion object {
        private const val serialVersionUID = 1L
    }
}
