package com.for_the_love_of_code.kotlin_pm.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
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
import java.time.Instant
import java.util.Objects

/**
 * A Project.
 */
@Entity
@Table(name = "project")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "project")
class Project(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    var id: Long? = null,

    @get: NotNull
    @Column(name = "name", nullable = false)
    var name: String? = null,

    @Column(name = "description")
    var description: String? = null,

    @Column(name = "estimated_end_date")
    var estimatedEndDate: Instant? = null,

    @ManyToOne
    @JsonIgnoreProperties("projects")
    var owner: User? = null,

    @ManyToMany
    @JoinTable(name = "project_field",
        joinColumns = [JoinColumn(name = "project_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "field_id", referencedColumnName = "id")])
    var fields: MutableSet<Field> = mutableSetOf(),

    @OneToMany(mappedBy = "project")
    var milestones: MutableSet<Milestone> = mutableSetOf()

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {

    fun addField(field: Field): Project {
        this.fields.add(field)
        return this
    }

    fun removeField(field: Field): Project {
        this.fields.remove(field)
        return this
    }

    fun addMilestone(milestone: Milestone): Project {
        this.milestones.add(milestone)
        milestone.project = this
        return this
    }

    fun removeMilestone(milestone: Milestone): Project {
        this.milestones.remove(milestone)
        milestone.project = null
        return this
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Project) return false
        if (other.id == null || id == null) return false

        return Objects.equals(id, other.id)
    }

    override fun hashCode(): Int {
        return 31
    }

    override fun toString(): String {
        return "Project{" +
            "id=$id" +
            ", name='$name'" +
            ", description='$description'" +
            ", estimatedEndDate='$estimatedEndDate'" +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
