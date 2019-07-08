package com.lenovo.coe.security

import com.lenovo.coe.config.SYSTEM_ACCOUNT

import java.util.Optional

import org.springframework.data.domain.AuditorAware
import org.springframework.stereotype.Component

/**
 * Implementation of [AuditorAware] based on Spring Security.
 */
@Component
class SpringSecurityAuditorAware : AuditorAware<String> {

    override fun getCurrentAuditor(): Optional<String> {
        return Optional.of(getCurrentUserLogin().orElse(SYSTEM_ACCOUNT))
    }
}
