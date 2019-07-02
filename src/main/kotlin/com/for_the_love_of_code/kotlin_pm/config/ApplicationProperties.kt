package com.for_the_love_of_code.kotlin_pm.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Properties specific to Kotlin Pm.
 *
 * Properties are configured in the `application.yml` file.
 * See [io.github.jhipster.config.JHipsterProperties] for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
class ApplicationProperties
