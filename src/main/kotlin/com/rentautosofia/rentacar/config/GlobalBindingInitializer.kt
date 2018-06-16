package com.rentautosofia.rentacar.config

import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.annotation.ControllerAdvice
import java.util.*


@ControllerAdvice
class GlobalBindingInitializer {

    /* Initialize a global InitBinder for dates instead of cloning its code in every Controller */

    @InitBinder
    fun binder(binder: WebDataBinder) {
        binder.registerCustomEditor(Date::class.java, DateEditor())
    }
}