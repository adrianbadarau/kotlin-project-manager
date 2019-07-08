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
 * A Delivrable.
 */
@Document(collection = "delivrable")
class Delivrable(

    @Id
    var id: String? = null,

    @get: NotNull
    @Field("name")
    var name: String? = null,

    @get: NotNull
    @Field("description")
    var description: String? = null,

    @get: NotNull
    @Field("target")
    var target: Instant? = null,

    @DBRef
    @Field("milestone")
    var milestones: MutableSet<Milestone> = mutableSetOf(),

    @DBRef
    @Field("fields")
    var fields: MutableSet<Field> = mutableSetOf(),

    @DBRef
    @Field("project")
    @JsonIgnoreProperties("delivrables")
    var project: Project? = null

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {

    fun addMilestone(milestone: Milestone): Delivrable {
        this.milestones.add(milestone)
        milestone.delivrable = this
        return this
    }

    fun removeMilestone(milestone: Milestone): Delivrable {
        this.milestones.remove(milestone)
        milestone.delivrable = null
        return this
    }

    fun addField(field: Field): Delivrable {
        this.fields.add(field)
        return this
    }

    fun removeField(field: Field): Delivrable {
        this.fields.remove(field)
        return this
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Delivrable) return false
        if (other.id == null || id == null) return false

        return id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "Delivrable{" +
        "id=$id" +
        ", name='$name'" +
        ", description='$description'" +
        ", target='$target'" +
        "}"


    companion object {
        private const val serialVersionUID = 1L
    }
}
