package com.lenovo.coe.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.lenovo.coe.domain.pojo.CustomField
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
    @Field("code")
    var code: String? = null,

    @Field("description")
    var description: String? = null,

    @Field("estimated_end_date")
    var estimatedEndDate: Instant? = null,

    @Field("custom_fields")
    var customFields: MutableList<CustomField> = mutableListOf(),

    @Field("custom_tables")
    var customTables: MutableList<MutableList<HashMap<String, Any>>> = mutableListOf(),

    @DBRef
    @Field("owner")
    @JsonIgnoreProperties("projects")
    var owner: User? = null,

    @DBRef
    @Field("status")
    @JsonIgnoreProperties("projects")
    var status: Status? = null,

    @DBRef
    @Field("milestone")
    var milestones: MutableSet<Milestone> = mutableSetOf(),

    @DBRef
    @Field("attachments")
    @JsonIgnore
    var attachments: MutableSet<Attachment> = mutableSetOf(),

    @DBRef
    @Field("comments")
    @JsonIgnore
    var comments: MutableSet<Comment> = mutableSetOf()

// jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : Serializable {

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

    fun addAttachment(attachment: Attachment): Project {
        this.attachments.add(attachment)
        attachment.projects.add(this)
        return this
    }

    fun removeAttachment(attachment: Attachment): Project {
        this.attachments.remove(attachment)
        attachment.projects.remove(this)
        return this
    }

    fun addComment(comment: Comment): Project {
        this.comments.add(comment)
        comment.projects.add(this)
        return this
    }

    fun removeComment(comment: Comment): Project {
        this.comments.remove(comment)
        comment.projects.remove(this)
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
        ", code='$code'" +
        ", description='$description'" +
        ", estimatedEndDate='$estimatedEndDate'" +
        "}"


    companion object {
        private const val serialVersionUID = 1L
    }
}
