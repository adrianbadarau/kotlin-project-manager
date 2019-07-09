package com.lenovo.coe.domain

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
 * A ChangeHistory.
 */
@Document(collection = "change_history")
class ChangeHistory(

    @Id
    var id: String? = null,

    @get: NotNull
    @Field("subject")
    var subject: String? = null,

    @Field("from_value")
    var fromValue: String? = null,

    @get: NotNull
    @Field("to_value")
    var toValue: String? = null

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ChangeHistory) return false
        if (other.id == null || id == null) return false

        return id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "ChangeHistory{" +
        "id=$id" +
        ", subject='$subject'" +
        ", fromValue='$fromValue'" +
        ", toValue='$toValue'" +
        "}"


    companion object {
        private const val serialVersionUID = 1L
    }
}
