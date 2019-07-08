package com.lenovo.coe.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DBRef

import java.io.Serializable

/**
 * A BusinessCase.
 */
@Document(collection = "business_case")
class BusinessCase(

    @Id
    var id: String? = null,

    @Field("summary")
    var summary: String? = null,

    @DBRef
    @Field("benefit")
    var benefits: MutableSet<Benefit> = mutableSetOf(),

    @DBRef
    @Field("project")
    @com.fasterxml.jackson.annotation.JsonBackReference
    var project: Project? = null

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {

    fun addBenefit(benefit: Benefit): BusinessCase {
        this.benefits.add(benefit)
        benefit.businessCase = this
        return this
    }

    fun removeBenefit(benefit: Benefit): BusinessCase {
        this.benefits.remove(benefit)
        benefit.businessCase = null
        return this
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BusinessCase) return false
        if (other.id == null || id == null) return false

        return id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "BusinessCase{" +
        "id=$id" +
        ", summary='$summary'" +
        "}"


    companion object {
        private const val serialVersionUID = 1L
    }
}
