package com.rentautosofia.rentacar.controller.admin

import com.rentautosofia.rentacar.entity.BookedCar
import com.rentautosofia.rentacar.repository.CarRepository
import com.rentautosofia.rentacar.repository.CustomerRepository
import com.rentautosofia.rentacar.repository.RentedCarRepository
import com.rentautosofia.rentacar.util.daysTill
import com.rentautosofia.rentacar.util.findOne
import com.rentautosofia.rentacar.util.getProperFormat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

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


    @GetMapping("/{id}/edit")
    fun edit(model: Model, @PathVariable id: Int): String {
        val booking = this.rentedCarRepository.findOne(id) ?: return "redirect:/$PATH_ADMIN_BOOKING/all"
        model.addAttribute("booking", booking)
        model.addAttribute("view", "$PATH_ADMIN_BOOKING/edit")
        return "base-layout"
    }

    @PostMapping("/{id}/edit")
    fun editProcess(model: Model, @PathVariable id: Int, newBooking: BookedCar): String {
        val oldBooking = this.rentedCarRepository.findOne(id) ?: return "redirect:/$PATH_ADMIN_BOOKING/all"
        this.rentedCarRepository.saveAndFlush(oldBooking.copy(startDate = newBooking.startDate, endDate = newBooking.endDate, payedDeposit = newBooking.payedDeposit))
        return "redirect:/$PATH_ADMIN_BOOKING/all"
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

        val days = booking.startDate daysTill booking.endDate
        val pricePerDay = car?.getPricePerDayFor(days)!!
        val totalPrice = pricePerDay * days

        with(model) {
            addAttribute("booking", booking)
            addAttribute("car", car)
            addAttribute("customer", customer)
            addAttribute("pricePerDay", pricePerDay)
            addAttribute("totalPrice", totalPrice)
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
