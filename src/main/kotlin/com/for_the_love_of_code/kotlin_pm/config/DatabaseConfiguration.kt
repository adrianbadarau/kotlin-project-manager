package com.for_the_love_of_code.kotlin_pm.config

import org.springframework.context.annotation.Configuration

import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories

import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableJpaRepositories("com.for_the_love_of_code.kotlin_pm.repository")
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
@EnableTransactionManagement
@EnableElasticsearchRepositories("com.for_the_love_of_code.kotlin_pm.repository.search")
class DatabaseConfiguration
