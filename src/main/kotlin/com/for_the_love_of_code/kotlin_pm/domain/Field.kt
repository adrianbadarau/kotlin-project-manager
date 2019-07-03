package com.for_the_love_of_code.kotlin_pm.domain

import com.fasterxml.jackson.annotation.JsonIgnore

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.Table
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

import org.springframework.data.elasticsearch.annotations.FieldType
import java.io.Serializable
import java.util.Objects

/**
 * A Field.
 */
@Entity
@Table(name = "field")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "field")
class Field(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    var id: Long? = null,

    @get: NotNull
    @Column(name = "name", nullable = false)
    var name: String? = null,

    @Column(name = "data")
    var data: String? = null,

    @ManyToMany(mappedBy = "fields")
    @JsonIgnore
    var projects: MutableSet<Project> = mutableSetOf(),

    @ManyToMany(mappedBy = "fields")
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

        return Objects.equals(id, other.id)
    }

    override fun hashCode(): Int {
        return 31
    }

    override fun toString(): String {
        return "Field{" +
            "id=$id" +
            ", name='$name'" +
            ", data='$data'" +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
