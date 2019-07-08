package com.lenovo.coe.repository

import com.lenovo.coe.domain.User

import org.springframework.data.domain.Page

import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

import java.util.Optional
import java.time.Instant

/**
 * Spring Data MongoDB repository for the [User] entity.
 */
@Repository
interface UserRepository : MongoRepository<User, String> {

    fun findOneByActivationKey(activationKey: String): Optional<User>

    fun findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(dateTime: Instant): List<User>

    fun findOneByResetKey(resetKey: String): Optional<User>

    fun findOneByEmailIgnoreCase(email: String?): Optional<User>

    fun findOneByLogin(login: String): Optional<User>

    fun findAllByLoginNot(pageable: Pageable, login: String): Page<User>
}
