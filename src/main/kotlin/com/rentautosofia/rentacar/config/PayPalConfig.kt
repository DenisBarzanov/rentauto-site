package com.rentautosofia.rentacar.config

import java.util.HashMap

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import com.paypal.base.rest.APIContext
import com.paypal.base.rest.OAuthTokenCredential
import com.paypal.base.rest.PayPalRESTException

@Configuration
class PaypalConfig {

    @Value("\${paypal.client.app}")
    private lateinit var clientId: String
    @Value("\${paypal.client.secret}")
    private lateinit var clientSecret: String
    @Value("\${paypal.mode}")
    private lateinit var mode: String

    @Bean
    fun paypalSdkConfig(): Map<String, String> {
        val sdkConfig = HashMap<String, String>()
        sdkConfig["mode"] = mode
        return sdkConfig
    }

    @Bean
    fun authTokenCredential(): OAuthTokenCredential {
        return OAuthTokenCredential(clientId, clientSecret, paypalSdkConfig())
    }

    @Bean
    @Throws(PayPalRESTException::class)
    fun apiContext(): APIContext {
        val apiContext = APIContext(authTokenCredential().accessToken)
        apiContext.configurationMap = paypalSdkConfig()
        return apiContext
    }
}