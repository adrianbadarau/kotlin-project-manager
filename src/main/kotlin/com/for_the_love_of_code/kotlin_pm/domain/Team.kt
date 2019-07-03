package com.for_the_love_of_code.kotlin_pm.domain

import com.fasterxml.jackson.annotation.JsonIgnore

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
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
 * A Team.
 */
@Entity
@Table(name = "team")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "team")
class Team(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    var id: Long? = null,

    @get: NotNull
    @Column(name = "name", nullable = false)
    var name: String? = null,

    @ManyToMany
    @JoinTable(name = "team_user",
        joinColumns = [JoinColumn(name = "team_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")])
    var users: MutableSet<User> = mutableSetOf(),

    @ManyToMany(mappedBy = "teams")
    @JsonIgnore
    var milestones: MutableSet<Milestone> = mutableSetOf()

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {

    fun addUser(user: User): Team {
        this.users.add(user)
        return this
    }

    fun removeUser(user: User): Team {
        this.users.remove(user)
        return this
    }

    fun addMilestone(milestone: Milestone): Team {
        this.milestones.add(milestone)
        milestone.teams.add(this)
        return this
    }

    fun removeMilestone(milestone: Milestone): Team {
        this.milestones.remove(milestone)
        milestone.teams.remove(this)
        return this
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Team) return false
        if (other.id == null || id == null) return false

        return Objects.equals(id, other.id)
    }

    override fun hashCode(): Int {
        return 31
    }

    override fun toString(): String {
        return "Team{" +
            "id=$id" +
            ", name='$name'" +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
