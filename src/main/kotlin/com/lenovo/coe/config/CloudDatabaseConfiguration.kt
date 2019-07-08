package com.lenovo.coe.config

import com.github.mongobee.Mongobee

import io.github.jhipster.config.JHipsterConstants
import io.github.jhipster.domain.util.JSR310DateConverters.DateToZonedDateTimeConverter
import io.github.jhipster.domain.util.JSR310DateConverters.DurationToLongConverter
import io.github.jhipster.domain.util.JSR310DateConverters.ZonedDateTimeToDateConverter

import org.slf4j.LoggerFactory
import org.springframework.cloud.Cloud
import org.springframework.cloud.CloudException
import org.springframework.cloud.config.java.AbstractCloudConfig
import org.springframework.cloud.service.common.MongoServiceInfo
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.convert.converter.Converter
import org.springframework.data.mongodb.MongoDbFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.convert.MongoCustomConversions
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean

@Configuration
@EnableMongoRepositories("com.lenovo.coe.repository")
@Profile(JHipsterConstants.SPRING_PROFILE_CLOUD)
class CloudDatabaseConfiguration : AbstractCloudConfig() {

    private val log = LoggerFactory.getLogger(CloudDatabaseConfiguration::class.java)

    @Bean
    fun mongoFactory(): MongoDbFactory {
        return connectionFactory().mongoDbFactory()
    }

    @Bean
    fun validator(): LocalValidatorFactoryBean {
        return LocalValidatorFactoryBean()
    }

    @Bean
    fun validatingMongoEventListener(): ValidatingMongoEventListener {
        return ValidatingMongoEventListener(validator())
    }

    @Bean
    fun customConversions(): MongoCustomConversions {
        val converterList = mutableListOf<Converter<*, *>>(
            DateToZonedDateTimeConverter.INSTANCE,
            ZonedDateTimeToDateConverter.INSTANCE,
            DurationToLongConverter.INSTANCE
        )
        return MongoCustomConversions(converterList)
    }

    @Bean
    fun mongobee(mongoDbFactory: MongoDbFactory, mongoTemplate: MongoTemplate, cloud: Cloud): Mongobee {
        log.debug("Configuring Cloud Mongobee")
        val matchingServiceInfos = cloud.getServiceInfos(MongoDbFactory::class.java)

        if (matchingServiceInfos.size != 1) {
            throw CloudException("No unique service matching MongoDbFactory found. Expected 1, found " +
                matchingServiceInfos.size)
        }
        val info = matchingServiceInfos[0] as MongoServiceInfo
        val mongobee = Mongobee(info.uri)
        mongobee.setDbName(mongoDbFactory.db.name)
        mongobee.setMongoTemplate(mongoTemplate)
        // package to scan for migrations
        mongobee.setChangeLogsScanPackage("com.lenovo.coe.config.dbmigrations")
        mongobee.isEnabled = true
        return mongobee
    }
}
