package com.lenovo.coe.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DBRef

import java.io.Serializable

/**
 * A Performance.
 */
@Document(collection = "performance")
class Performance(

    @Id
    var id: String? = null,

    @Field("timeline_performance")
    var timelinePerformance: Float? = null,

    @Field("risk_register")
    var riskRegister: String? = null,

    @Field("mitigation_plan")
    var mitigationPlan: String? = null,

    @DBRef
    @Field("milestones")
    @JsonIgnore
    var milestones: MutableSet<Milestone> = mutableSetOf(),

    @DBRef
    @Field("projects")
    @JsonIgnore
    var projects: MutableSet<Project> = mutableSetOf()

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {

    fun addMilestone(milestone: Milestone): Performance {
        this.milestones.add(milestone)
        milestone.performances.add(this)
        return this
    }

    fun removeMilestone(milestone: Milestone): Performance {
        this.milestones.remove(milestone)
        milestone.performances.remove(this)
        return this
    }

    fun addProject(project: Project): Performance {
        this.projects.add(project)
        project.performances.add(this)
        return this
    }

    fun removeProject(project: Project): Performance {
        this.projects.remove(project)
        project.performances.remove(this)
        return this
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Performance) return false
        if (other.id == null || id == null) return false

        return id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "Performance{" +
        "id=$id" +
        ", timelinePerformance=$timelinePerformance" +
        ", riskRegister='$riskRegister'" +
        ", mitigationPlan='$mitigationPlan'" +
        "}"


    companion object {
        private const val serialVersionUID = 1L
    }
}
