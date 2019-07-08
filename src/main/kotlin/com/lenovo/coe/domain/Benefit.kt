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

/**
 * A Benefit.
 */
@Document(collection = "benefit")
class Benefit(

    @Id
    var id: String? = null,

    @get: NotNull
    @Field("description")
    var description: String? = null,

    @DBRef
    @Field("type")
    @JsonIgnoreProperties("benefits")
    var type: BenefitType? = null,

    @DBRef
    @Field("fields")
    var fields: MutableSet<Field> = mutableSetOf(),

    @DBRef
    @Field("businessCase")
    @JsonIgnoreProperties("benefits")
    var businessCase: BusinessCase? = null

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {

    fun addField(field: Field): Benefit {
        this.fields.add(field)
        return this
    }

    fun removeField(field: Field): Benefit {
        this.fields.remove(field)
        return this
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Benefit) return false
        if (other.id == null || id == null) return false

        return id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "Benefit{" +
        "id=$id" +
        ", description='$description'" +
        "}"


    companion object {
        private const val serialVersionUID = 1L
    }
}
