package com.for_the_love_of_code.kotlin_pm.repository.search

import com.for_the_love_of_code.kotlin_pm.domain.User
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

/**
 * Spring Data Elasticsearch repository for the User entity.
 */
interface UserSearchRepository : ElasticsearchRepository<User, Long>
