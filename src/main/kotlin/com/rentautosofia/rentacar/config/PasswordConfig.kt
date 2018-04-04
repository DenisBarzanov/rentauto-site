package com.rentautosofia.rentacar.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder


@Configuration
class PasswordConfig {
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        // Create an encoder with strength 31
        // values from 4 .. 31 are valid; the higher the value, the more work has to be done to calculate the hash
        return BCryptPasswordEncoder(12)
    }
}