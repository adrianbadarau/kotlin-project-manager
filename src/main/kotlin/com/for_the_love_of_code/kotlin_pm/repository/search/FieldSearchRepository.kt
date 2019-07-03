package com.for_the_love_of_code.kotlin_pm.repository.search

import com.for_the_love_of_code.kotlin_pm.domain.Field
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

/**
 * Spring Data Elasticsearch repository for the [Field] entity.
 */
interface FieldSearchRepository : ElasticsearchRepository<Field, Long>
