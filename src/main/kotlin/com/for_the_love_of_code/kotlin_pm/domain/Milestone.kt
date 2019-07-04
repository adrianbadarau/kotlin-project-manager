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
 * A Milestone.
 */
@Entity
@Table(name = "milestone")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "milestone")
class Milestone(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    var id: Long? = null,

    @get: NotNull
    @Column(name = "name", nullable = false)
    var name: String? = null,

    @Column(name = "estimated_end_date")
    var estimatedEndDate: Instant? = null,

    @Column(name = "end_date")
    var endDate: Instant? = null,

    @ManyToOne
    @JsonIgnoreProperties("milestones")
    var project: Project? = null,

    @ManyToOne
    @JsonIgnoreProperties("milestones")
    var status: Status? = null,

    @ManyToMany
    @JoinTable(name = "milestone_field",
        joinColumns = [JoinColumn(name = "milestone_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "field_id", referencedColumnName = "id")])
    var fields: MutableSet<Field> = mutableSetOf(),

    @ManyToMany
    @JoinTable(name = "milestone_team",
        joinColumns = [JoinColumn(name = "milestone_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "team_id", referencedColumnName = "id")])
    var teams: MutableSet<Team> = mutableSetOf(),

    @ManyToMany
    @JoinTable(name = "milestone_user",
        joinColumns = [JoinColumn(name = "milestone_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")])
    var users: MutableSet<User> = mutableSetOf(),

    @OneToMany(mappedBy = "milestone")
    var tasks: MutableSet<Task> = mutableSetOf(),

    @ManyToMany(mappedBy = "milestones")
    @JsonIgnore
    var attachments: MutableSet<Attachment> = mutableSetOf(),

    @ManyToMany(mappedBy = "milestones")
    @JsonIgnore
    var comments: MutableSet<Comment> = mutableSetOf()

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {

    fun addField(field: Field): Milestone {
        this.fields.add(field)
        return this
    }

    fun removeField(field: Field): Milestone {
        this.fields.remove(field)
        return this
    }

    fun addTeam(team: Team): Milestone {
        this.teams.add(team)
        return this
    }

    fun removeTeam(team: Team): Milestone {
        this.teams.remove(team)
        return this
    }

    fun addUser(user: User): Milestone {
        this.users.add(user)
        return this
    }

    fun removeUser(user: User): Milestone {
        this.users.remove(user)
        return this
    }

    fun addTask(task: Task): Milestone {
        this.tasks.add(task)
        task.milestone = this
        return this
    }

    fun removeTask(task: Task): Milestone {
        this.tasks.remove(task)
        task.milestone = null
        return this
    }

    fun addAttachment(attachment: Attachment): Milestone {
        this.attachments.add(attachment)
        attachment.milestones.add(this)
        return this
    }

    fun removeAttachment(attachment: Attachment): Milestone {
        this.attachments.remove(attachment)
        attachment.milestones.remove(this)
        return this
    }

    fun addComment(comment: Comment): Milestone {
        this.comments.add(comment)
        comment.milestones.add(this)
        return this
    }

    fun removeComment(comment: Comment): Milestone {
        this.comments.remove(comment)
        comment.milestones.remove(this)
        return this
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Milestone) return false
        if (other.id == null || id == null) return false

        return Objects.equals(id, other.id)
    }

    override fun hashCode(): Int {
        return 31
    }

    override fun toString(): String {
        return "Milestone{" +
            "id=$id" +
            ", name='$name'" +
            ", estimatedEndDate='$estimatedEndDate'" +
            ", endDate='$endDate'" +
            "}"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
