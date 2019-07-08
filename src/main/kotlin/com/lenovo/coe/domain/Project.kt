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
import java.time.Instant

/**
 * A Project.
 */
@Document(collection = "project")
class Project(

    @Id
    var id: String? = null,

    @get: NotNull
    @Field("name")
    var name: String? = null,

    @get: NotNull
    @get: Size(min = 10)
    @Field("objective")
    var objective: String? = null,

    @get: NotNull
    @Field("target")
    var target: Instant? = null,

    @Field("budget")
    var budget: Double? = null,

    @get: NotNull
    @Field("risk")
    var risk: String? = null,

    @Field("benefit_mesurement")
    var benefitMesurement: String? = null,

    @DBRef
    @Field("businessCase")
    var businessCase: BusinessCase? = null,

    @DBRef
    @Field("team")
    var teams: MutableSet<Team> = mutableSetOf(),

    @DBRef
    @Field("delivrable")
    var delivrables: MutableSet<Delivrable> = mutableSetOf(),

    @DBRef
    @Field("performances")
    var performances: MutableSet<Performance> = mutableSetOf()

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {

    fun addTeam(team: Team): Project {
        this.teams.add(team)
        team.project = this
        return this
    }

    fun removeTeam(team: Team): Project {
        this.teams.remove(team)
        team.project = null
        return this
    }

    fun addDelivrable(delivrable: Delivrable): Project {
        this.delivrables.add(delivrable)
        delivrable.project = this
        return this
    }

    fun removeDelivrable(delivrable: Delivrable): Project {
        this.delivrables.remove(delivrable)
        delivrable.project = null
        return this
    }

    fun addPerformance(performance: Performance): Project {
        this.performances.add(performance)
        return this
    }

    fun removePerformance(performance: Performance): Project {
        this.performances.remove(performance)
        return this
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Project) return false
        if (other.id == null || id == null) return false

        return id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "Project{" +
        "id=$id" +
        ", name='$name'" +
        ", objective='$objective'" +
        ", target='$target'" +
        ", budget=$budget" +
        ", risk='$risk'" +
        ", benefitMesurement='$benefitMesurement'" +
        "}"


    companion object {
        private const val serialVersionUID = 1L
    }
}
