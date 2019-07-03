package com.for_the_love_of_code.kotlin_pm.repository.search

import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Configuration

/**
 * Configure a Mock version of FieldSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
class FieldSearchRepositoryMockConfiguration {

    @MockBean
    private lateinit var mockFieldSearchRepository: FieldSearchRepository
}
