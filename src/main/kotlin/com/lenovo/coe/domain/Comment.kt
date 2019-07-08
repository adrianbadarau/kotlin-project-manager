package com.lenovo.coe.domain

import com.fasterxml.jackson.annotation.JsonIgnore
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
 * A Comment.
 */
@Document(collection = "comment")
class Comment(

    @Id
    var id: String? = null,

    @get: NotNull
    @Field("body")
    var body: String? = null,

    @get: NotNull
    @Field("created_at")
    var createdAt: Int? = null,

    @DBRef
    @Field("tasks")
    @JsonIgnore
    var tasks: MutableSet<Task> = mutableSetOf(),

    @DBRef
    @Field("milestones")
    @JsonIgnore
    var milestones: MutableSet<Milestone> = mutableSetOf()

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {

    fun addTask(task: Task): Comment {
        this.tasks.add(task)
        task.comments.add(this)
        return this
    }

    fun removeTask(task: Task): Comment {
        this.tasks.remove(task)
        task.comments.remove(this)
        return this
    }

    fun addMilestone(milestone: Milestone): Comment {
        this.milestones.add(milestone)
        milestone.comments.add(this)
        return this
    }

    fun removeMilestone(milestone: Milestone): Comment {
        this.milestones.remove(milestone)
        milestone.comments.remove(this)
        return this
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Comment) return false
        if (other.id == null || id == null) return false

        return id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "Comment{" +
        "id=$id" +
        ", body='$body'" +
        ", createdAt=$createdAt" +
        "}"


    companion object {
        private const val serialVersionUID = 1L
    }
}
