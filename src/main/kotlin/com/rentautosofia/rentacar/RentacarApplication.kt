/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.rentautosofia.rentacar.RentacarApplication
 *  org.springframework.boot.SpringApplication
 *  org.springframework.boot.autoconfigure.SpringBootApplication
 */
package com.rentautosofia.rentacar

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RentacarApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<RentacarApplication>(*args)
        }
    }
}
