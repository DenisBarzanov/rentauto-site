package com.rentautosofia.rentacar.controller.admin

import com.rentautosofia.rentacar.repository.RentedCarRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

const val ADMIN_BOOKING = "admin/booking"

@Controller
@RequestMapping("/$ADMIN_BOOKING")
class BookingController @Autowired
constructor(private val rentedCarRepository: RentedCarRepository){

    @GetMapping("/all")
    fun bookings(model: Model): String {
        model.addAttribute("view", "$ADMIN_BOOKING/all")
        val allBookings = this.rentedCarRepository.findAll()
        model.addAttribute("bookings", allBookings)
        return "base-layout"
    }
}