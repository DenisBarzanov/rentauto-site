package com.rentautosofia.rentacar.controller.admin

import com.rentautosofia.rentacar.entity.BookedCar
import com.rentautosofia.rentacar.entity.Car
import com.rentautosofia.rentacar.entity.Customer
import com.rentautosofia.rentacar.repository.CarRepository
import com.rentautosofia.rentacar.repository.CustomerRepository
import com.rentautosofia.rentacar.repository.RentedCarRepository
import com.rentautosofia.rentacar.util.daysTill
import com.rentautosofia.rentacar.util.findOne
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

const val PATH_ADMIN_BOOKING = "admin/booking"

object BookedCarForView : BookedCar() {
    var car: Car? = null
    var customer: Customer? = null
}

@Controller
@RequestMapping("/$PATH_ADMIN_BOOKING")
class BookingController @Autowired
constructor(private val carRepository: CarRepository,
            private val customerRepository: CustomerRepository,
            private val rentedCarRepository: RentedCarRepository){

    fun BookedCar.toBookedCarForView(): BookedCarForView {
        val bookedCarView = this as BookedCarForView
        with(bookedCarView) {
            car = this@BookingController.carRepository.findOne(bookedCarView.carId)
            customer = this@BookingController.customerRepository.findOne(bookedCarView.customerId)
        }
        return bookedCarView
    }

    @GetMapping("/all")
    fun bookings(model: Model): String {
        model.addAttribute("view", "$PATH_ADMIN_BOOKING/all")

        val allBookings =
                this.rentedCarRepository.findAll()
        allBookings.sortWith(Comparator(fun (b1: BookedCar, b2: BookedCar): Int {
            return when {
                with(b1.startDate) { after(b2.startDate) and after(b2.endDate) }
                    or with(b1.endDate) { after(b2.startDate) and after(b2.endDate)}
                    -> 1
                else -> -1
            }
        }))


        val viewBookings = allBookings.map { it.toBookedCarForView() }


        model.addAttribute("bookings", viewBookings)
        return "base-layout"
    }


    @GetMapping("/{id}/edit")
    fun edit(model: Model, @PathVariable id: Int): String {
        val booking = this.rentedCarRepository.findOne(id) ?: return "redirect:/$PATH_ADMIN_BOOKING/all"
        with(model) {
            addAttribute("booking", booking)
            addAttribute("view", "$PATH_ADMIN_BOOKING/edit")
        }
        return "base-layout"
    }

    @PostMapping("/{id}/edit")
    fun editProcess(model: Model, @PathVariable id: Int, newBooking: BookedCar): String {
        val booking = this.rentedCarRepository.findOne(id) ?: return "redirect:/$PATH_ADMIN_BOOKING/all"
//        this.rentedCarRepository.saveAndFlush(oldBooking.copy(startDate = newBooking.startDate, endDate = newBooking.endDate, payedDeposit = newBooking.payedDeposit))
        with(booking) {
            startDate = newBooking.startDate
            endDate = newBooking.endDate
            payedDeposit = newBooking.payedDeposit
        }
        this.rentedCarRepository.saveAndFlush(booking)
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
