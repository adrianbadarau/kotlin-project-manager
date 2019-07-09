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
 * A Priority.
 */
@Document(collection = "priority")
class Priority(

    @Id
    var id: String? = null,

    @get: NotNull
    @Field("name")
    var name: String? = null,

    @DBRef
    @Field("task")
    var tasks: MutableSet<Task> = mutableSetOf()

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {

    fun addTask(task: Task): Priority {
        this.tasks.add(task)
        task.priority = this
        return this
    }

    fun removeTask(task: Task): Priority {
        this.tasks.remove(task)
        task.priority = null
        return this
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Priority) return false
        if (other.id == null || id == null) return false

        return id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "Priority{" +
        "id=$id" +
        ", name='$name'" +
        "}"


    companion object {
        private const val serialVersionUID = 1L
    }
}
