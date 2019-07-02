package com.for_the_love_of_code.kotlin_pm.domain

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Column
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import java.io.Serializable
import java.util.Objects

/**
 * An authority (a security role) used by Spring Security.
 */
@Entity
@Table(name = "jhi_authority")
data class Authority(

    @field:NotNull
    @field:Size(max = 50)
    @Id
    @Column(length = 50)
    var name: String? = null

) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Authority) return false
        if (other.name == null || name == null) return false

        return Objects.equals(name, other.name)
    }

    override fun hashCode(): Int {
        return 31
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
