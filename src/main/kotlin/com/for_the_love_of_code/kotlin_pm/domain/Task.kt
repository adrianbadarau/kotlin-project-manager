package com.for_the_love_of_code.kotlin_pm.domain

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
 * A Task.
 */
@Entity
@Table(name = "task")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "task")
class Task(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    var id: Long? = null,

    @get: NotNull
    @Column(name = "name", nullable = false)
    var name: String? = null,

    @ManyToOne
    @JsonIgnoreProperties("tasks")
    var milestone: Milestone? = null,

    @ManyToMany
    @JoinTable(name = "task_user",
        joinColumns = [JoinColumn(name = "task_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")])
    var users: MutableSet<User> = mutableSetOf()

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {

    fun addUser(user: User): Task {
        this.users.add(user)
        return this
    }

    fun removeUser(user: User): Task {
        this.users.remove(user)
        return this
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Task) return false
        if (other.id == null || id == null) return false

        return Objects.equals(id, other.id)
    }

    override fun hashCode(): Int {
        return 31
    }

    override fun toString(): String {
        return "Task{" +
            "id=$id" +
            ", name='$name'" +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
