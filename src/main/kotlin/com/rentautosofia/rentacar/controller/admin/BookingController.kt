package com.rentautosofia.rentacar.controller.admin

import com.rentautosofia.rentacar.repository.CarRepository
import com.rentautosofia.rentacar.repository.CustomerRepository
import com.rentautosofia.rentacar.repository.RentedCarRepository
import com.rentautosofia.rentacar.util.findOne
import com.rentautosofia.rentacar.util.getProperFormat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

const val PATH_ADMIN_BOOKING = "admin/booking"

@Controller
@RequestMapping("/$PATH_ADMIN_BOOKING")
class BookingController @Autowired
constructor(private val carRepository: CarRepository,
            private val customerRepository: CustomerRepository,
            private val rentedCarRepository: RentedCarRepository){

    @GetMapping("/all")
    fun bookings(model: Model): String {
        model.addAttribute("view", "$PATH_ADMIN_BOOKING/all")
        val allBookings =
                this.rentedCarRepository.findAll()
        model.addAttribute("bookings", allBookings)
        return "base-layout"
    }
    @GetMapping("/{id}/delete")
    fun delete(model: Model, @PathVariable id: Int): String {
        model.addAttribute("view", "$PATH_ADMIN_BOOKING/delete")
        val booking =
                this.rentedCarRepository.findOne(id) ?: return "redirect:/$PATH_ADMIN_BOOKING/all"
        val car =
                this.carRepository.findOne(booking.carId)
        val customer =
                this.customerRepository.findOne(booking.customerId)

        with(model) {
            addAttribute("car", car)
            addAttribute("customer", customer)
            addAttribute("startDate", booking.startDate.getProperFormat())
            addAttribute("endDate", booking.endDate.getProperFormat())
            addAttribute("bookingId", booking.id)
        }

        return "base-layout"
    }

    @PostMapping("/{id}/delete")
    fun deleteProcess(@PathVariable id: Int): String {
        val booking =
                this.rentedCarRepository.findOne(id) ?: return "redirect:/$PATH_ADMIN_BOOKING/all"
        this.rentedCarRepository.delete(booking)
        this.rentedCarRepository.flush()
        return "redirect:/$PATH_ADMIN_BOOKING/all"
    }
}
