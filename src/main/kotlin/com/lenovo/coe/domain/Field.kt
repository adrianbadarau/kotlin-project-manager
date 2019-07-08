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
 * A Field.
 */
@Document(collection = "field")
class Field(

    @Id
    var id: String? = null,

    @get: NotNull
    @Field("name")
    var name: String? = null,

    @Field("data")
    var data: String? = null,

    @DBRef
    @Field("projects")
    @JsonIgnore
    var projects: MutableSet<Project> = mutableSetOf(),

    @DBRef
    @Field("businessCases")
    @JsonIgnore
    var businessCases: MutableSet<BusinessCase> = mutableSetOf(),

    @DBRef
    @Field("benefits")
    @JsonIgnore
    var benefits: MutableSet<Benefit> = mutableSetOf(),

    @DBRef
    @Field("delivrables")
    @JsonIgnore
    var delivrables: MutableSet<Delivrable> = mutableSetOf(),

    @DBRef
    @Field("milestones")
    @JsonIgnore
    var milestones: MutableSet<Milestone> = mutableSetOf()

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {

    fun addProject(project: Project): Field {
        this.projects.add(project)
        project.fields.add(this)
        return this
    }

    fun removeProject(project: Project): Field {
        this.projects.remove(project)
        project.fields.remove(this)
        return this
    }

    fun addBusinessCase(businessCase: BusinessCase): Field {
        this.businessCases.add(businessCase)
        businessCase.fields.add(this)
        return this
    }

    fun removeBusinessCase(businessCase: BusinessCase): Field {
        this.businessCases.remove(businessCase)
        businessCase.fields.remove(this)
        return this
    }

    fun addBenefit(benefit: Benefit): Field {
        this.benefits.add(benefit)
        benefit.fields.add(this)
        return this
    }

    fun removeBenefit(benefit: Benefit): Field {
        this.benefits.remove(benefit)
        benefit.fields.remove(this)
        return this
    }

    fun addDelivrable(delivrable: Delivrable): Field {
        this.delivrables.add(delivrable)
        delivrable.fields.add(this)
        return this
    }

    fun removeDelivrable(delivrable: Delivrable): Field {
        this.delivrables.remove(delivrable)
        delivrable.fields.remove(this)
        return this
    }

    fun addMilestone(milestone: Milestone): Field {
        this.milestones.add(milestone)
        milestone.fields.add(this)
        return this
    }

    fun removeMilestone(milestone: Milestone): Field {
        this.milestones.remove(milestone)
        milestone.fields.remove(this)
        return this
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Field) return false
        if (other.id == null || id == null) return false

        return id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "Field{" +
        "id=$id" +
        ", name='$name'" +
        ", data='$data'" +
        "}"


    companion object {
        private const val serialVersionUID = 1L
    }
}
