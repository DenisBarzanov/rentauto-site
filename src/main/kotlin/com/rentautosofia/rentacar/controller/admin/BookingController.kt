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
import java.util.Date

const val PATH_ADMIN_BOOKING = "admin/booking"

data class BookedCarForView (
    var car: Car? = null,
    var customer: Customer? = null,
    var startDate: Date = Date(),
    var endDate: Date = Date(),
    var payedDeposit: Boolean? = false,
    var id: Int = 0)

@Controller
@RequestMapping("/$PATH_ADMIN_BOOKING")
class BookingController @Autowired
constructor(private val carRepository: CarRepository,
            private val customerRepository: CustomerRepository,
            private val rentedCarRepository: RentedCarRepository){

    fun BookedCar.toBookedCarForView(): BookedCarForView =
         BookedCarForView(
            car = this@BookingController.carRepository.findOne(this.carId),
            customer = this@BookingController.customerRepository.findOne(this.customerId),
            startDate = this.startDate,
            endDate = this.endDate,
            payedDeposit = this.payedDeposit,
            id = this.id)

    @GetMapping("/all")
    fun bookings(model: Model): String {
        model.addAttribute("view", "$PATH_ADMIN_BOOKING/all")

        val allBookings =
                this.rentedCarRepository.findAll()
        allBookings.sortWith(Comparator(fun (b1: BookedCar, b2: BookedCar): Int {
            return when {
                with(b1.startDate) { before(b2.startDate) and before(b2.endDate) }
                    or with(b1.endDate) { before(b2.startDate) and before(b2.endDate)}
                    -> -1
                else -> 1
            }
        }))


        val viewBookings = allBookings.map { it.toBookedCarForView() }

        val bookedCarIds = viewBookings.map { it.car!!.id }.distinct()

        val bookedCarsSorted = mutableListOf<List<BookedCarForView>>()

        bookedCarIds.forEach(fun (carId: Int) {
            val oneCarList = viewBookings.filter { it.car!!.id == carId }
            bookedCarsSorted.add(oneCarList)
        })

        model.addAttribute("bookings", bookedCarsSorted.flatten())
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
