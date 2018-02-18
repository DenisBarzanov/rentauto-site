package com.rentautosofia.rentacar.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/admin")
@Controller
class AdminController {

    @GetMapping("/options")
    fun options(model: Model): String {
        model.addAttribute("view", "admin/options")
        return "base-layout"
    }
}