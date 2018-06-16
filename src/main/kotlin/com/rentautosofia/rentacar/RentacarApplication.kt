package com.rentautosofia.rentacar

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

//@ComponentScan("com.rentautosofia.rentacar.config")
@SpringBootApplication
class RentacarApplication

fun main(args: Array<String>) {
    runApplication<RentacarApplication>(*args)
}
