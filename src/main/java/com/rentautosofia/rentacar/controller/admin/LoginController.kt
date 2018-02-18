package com.rentautosofia.rentacar.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class LoginController {

    @GetMapping("/login")
    fun login(model: Model): String {
        model.addAttribute("view", "login")
        return "base-layout"
    }
}