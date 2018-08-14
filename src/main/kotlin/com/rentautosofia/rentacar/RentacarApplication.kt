package com.rentautosofia.rentacar

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class RentacarApplication

fun main(args: Array<String>) {
    runApplication<RentacarApplication>(*args)
}
