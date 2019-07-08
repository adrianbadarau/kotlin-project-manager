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
 * A Milestone.
 */
@Document(collection = "milestone")
class Milestone(

    @Id
    var id: String? = null,

    @get: NotNull
    @Field("name")
    var name: String? = null,

    @get: NotNull
    @Field("target")
    var target: Instant? = null,

    @Field("description")
    var description: String? = null,

    @Field("workstream")
    var workstream: String? = null,

    @Field("code")
    var code: String? = null,

    @Field("track")
    var track: String? = null,

    @Field("estimated_end_date")
    var estimatedEndDate: Instant? = null,

    @Field("actual_end_date")
    var actualEndDate: Instant? = null,

    @Field("result")
    var result: String? = null,

    @DBRef
    @Field("task")
    var tasks: MutableSet<Task> = mutableSetOf(),

    @DBRef
    @Field("status")
    @JsonIgnoreProperties("milestones")
    var status: Status? = null,

    @DBRef
    @Field("owner")
    @JsonIgnoreProperties("milestones")
    var owner: User? = null,

    @DBRef
    @Field("projectUpdates")
    var projectUpdates: MutableSet<ProjectUpdate> = mutableSetOf(),

    @DBRef
    @Field("performances")
    var performances: MutableSet<Performance> = mutableSetOf(),

    @DBRef
    @Field("comments")
    var comments: MutableSet<Comment> = mutableSetOf(),

    @DBRef
    @Field("delivrable")
    @JsonIgnoreProperties("milestones")
    var delivrable: Delivrable? = null

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {

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

    fun addProjectUpdate(projectUpdate: ProjectUpdate): Milestone {
        this.projectUpdates.add(projectUpdate)
        return this
    }

    fun removeProjectUpdate(projectUpdate: ProjectUpdate): Milestone {
        this.projectUpdates.remove(projectUpdate)
        return this
    }

    fun addPerformance(performance: Performance): Milestone {
        this.performances.add(performance)
        return this
    }

    fun removePerformance(performance: Performance): Milestone {
        this.performances.remove(performance)
        return this
    }

    fun addComment(comment: Comment): Milestone {
        this.comments.add(comment)
        return this
    }

    fun removeComment(comment: Comment): Milestone {
        this.comments.remove(comment)
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
        ", target='$target'" +
        ", description='$description'" +
        ", workstream='$workstream'" +
        ", code='$code'" +
        ", track='$track'" +
        ", estimatedEndDate='$estimatedEndDate'" +
        ", actualEndDate='$actualEndDate'" +
        ", result='$result'" +
        "}"


    companion object {
        private const val serialVersionUID = 1L
    }
}
