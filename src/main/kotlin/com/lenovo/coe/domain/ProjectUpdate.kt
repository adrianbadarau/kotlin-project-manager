package com.lenovo.coe.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DBRef

import java.io.Serializable

/**
 * A ProjectUpdate.
 */
@Document(collection = "project_update")
class ProjectUpdate(

    @Id
    var id: String? = null,

    @Field("key_milestone_update")
    var keyMilestoneUpdate: String? = null,

    @Field("comments")
    var comments: String? = null,

    @Field("task_plan")
    var taskPlan: String? = null,

    @Field("risk")
    var risk: String? = null,

    @Field("support_neaded")
    var supportNeaded: String? = null,

    @DBRef
    @Field("milestones")
    @JsonIgnore
    var milestones: MutableSet<Milestone> = mutableSetOf(),

    @DBRef
    @Field("tasks")
    @JsonIgnore
    var tasks: MutableSet<Task> = mutableSetOf()

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {

    fun addMilestone(milestone: Milestone): ProjectUpdate {
        this.milestones.add(milestone)
        milestone.projectUpdates.add(this)
        return this
    }

    fun removeMilestone(milestone: Milestone): ProjectUpdate {
        this.milestones.remove(milestone)
        milestone.projectUpdates.remove(this)
        return this
    }

    fun addTask(task: Task): ProjectUpdate {
        this.tasks.add(task)
        task.projectUpdates.add(this)
        return this
    }

    fun removeTask(task: Task): ProjectUpdate {
        this.tasks.remove(task)
        task.projectUpdates.remove(this)
        return this
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ProjectUpdate) return false
        if (other.id == null || id == null) return false

        return id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "ProjectUpdate{" +
        "id=$id" +
        ", keyMilestoneUpdate='$keyMilestoneUpdate'" +
        ", comments='$comments'" +
        ", taskPlan='$taskPlan'" +
        ", risk='$risk'" +
        ", supportNeaded='$supportNeaded'" +
        "}"


    companion object {
        private const val serialVersionUID = 1L
    }
}
