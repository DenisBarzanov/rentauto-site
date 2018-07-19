package com.rentautosofia.rentacar.controller.admin

import com.rentautosofia.rentacar.entity.BookedCar
import com.rentautosofia.rentacar.entity.Car
import com.rentautosofia.rentacar.entity.Customer
import com.rentautosofia.rentacar.repository.CarRepository
import com.rentautosofia.rentacar.repository.CustomerRepository
import com.rentautosofia.rentacar.repository.RentedCarRepository
import com.rentautosofia.rentacar.util.findOne
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.util.Date

const val PATH_ADMIN_BOOKING = "admin/booking"

data class BookedCarForView(
        var car: Car? = null,
        var customer: Customer? = null,
        var startDate: Date = Date(),
        var endDate: Date = Date(),
        var earnest: Int? = 0,
        var id: Int = 0)

enum class Action { // Correct order
    TAKE_EARNEST,
    GIVE,
    TAKE_BACK
}

data class ScheduledBooking(
        var car: Car = Car(),
        var customer: Customer = Customer(),
        var date: Date = Date(),
        var action: Action = Action.GIVE,
        var id: Int = 0
)

@Controller
@RequestMapping("/$PATH_ADMIN_BOOKING")
class BookingController @Autowired
constructor(private val carRepository: CarRepository,
            private val customerRepository: CustomerRepository,
            private val rentedCarRepository: RentedCarRepository) {

    fun BookedCar.toBookedCarForView(): BookedCarForView =
            BookedCarForView(
                    car = this@BookingController.carRepository.findOne(this.carId),
                    customer = this@BookingController.customerRepository.findOne(this.customerId),
                    startDate = this.startDate,
                    endDate = this.endDate,
                    earnest = this.earnest,
                    id = this.id)

    fun BookedCar.toScheduledBookingsList(): List<ScheduledBooking?> {
        val car = this@BookingController.carRepository.findOne(this.carId)!!
        val customer = this@BookingController.customerRepository.findOne(this.customerId)!!

        return listOf(
                    ScheduledBooking(
                            car = car,
                            customer = customer,
                            date = this.startDate,
                            action = Action.GIVE,
                            id = this.id),
                    ScheduledBooking(
                            car = car,
                            customer = customer,
                            date = this.endDate,
                            action = Action.TAKE_BACK,
                            id = this.id)
                    )

    }
                    
    @GetMapping("/all")
    fun bookings(model: Model): String {
        model.addAttribute("view", "$PATH_ADMIN_BOOKING/all")

        val allBookings =
                this.rentedCarRepository.findAll()
//        allBookings.sortWith(Comparator(fun (b1: BookedCar, b2: BookedCar): Int {
//            return when {
//                with(b1.startDate) { before(b2.startDate) and before(b2.endDate) }
//                    or with(b1.endDate) { before(b2.startDate) and before(b2.endDate)}
//                    -> -1
//                else -> 1
//            }
//        })) FOR NEW BOOKINGS MAYBE
        allBookings.sortWith(Comparator(fun(b1: BookedCar, b2: BookedCar): Int {
            return when {
                b1.startDate.before(b2.startDate) -> -1
                else -> 1
            }
        }))


        val viewBookings = allBookings.map { it.toBookedCarForView() }

        val bookedCarIds = viewBookings.map { it.car!!.id }.distinct()

        val bookedCarsSorted = mutableListOf<List<BookedCarForView>>()

        bookedCarIds.forEach(fun(carId: Int) {
            val oneCarList = viewBookings.filter { it.car!!.id == carId }
            bookedCarsSorted.add(oneCarList)
        })

        model.addAttribute("bookings", bookedCarsSorted.flatten())
        return "base-layout"
    }

    @GetMapping("/toDoNext")
    fun toDoNext(model: Model): String {
        model.addAttribute("view", "$PATH_ADMIN_BOOKING/toDoNext")
        val allBookings = this.rentedCarRepository.findAll()
        var scheduledBookingsList = mutableListOf<ScheduledBooking?>()
        allBookings.forEach { booking ->
            booking.toScheduledBookingsList().forEach { 
                scheduledBookingsList.add(it)
            }
        }
        scheduledBookingsList = scheduledBookingsList.filter { 
            it!!.date.after(Date())
        }.toMutableList()

        scheduledBookingsList.sortWith(Comparator(fun(b1: ScheduledBooking?, b2: ScheduledBooking?): Int {
            return when {
                b1!!.date.before(b2!!.date)-> -1
                b1.action.ordinal < b2.action.ordinal -> -1 // the "smallest" booking
                else -> 1
            }
        }))
        model.addAttribute("scheduledBookings", scheduledBookingsList.toList())
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
        this.rentedCarRepository.saveAndFlush(oldBooking.copy(startDate = newBooking.startDate, endDate = newBooking.endDate, earnest = newBooking.earnest))
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

        with(model) {
            addAttribute("booking", booking)
            addAttribute("car", car)
            addAttribute("customer", customer)
            addAttribute("pricePerDay", booking.pricePerDay)
            addAttribute("totalPrice", booking.totalPrice)
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
