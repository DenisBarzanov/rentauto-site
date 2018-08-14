package com.rentautosofia.rentacar.controller.admin

import com.rentautosofia.rentacar.entity.Booking
import com.rentautosofia.rentacar.entity.Car
import com.rentautosofia.rentacar.entity.Customer
import com.rentautosofia.rentacar.repository.CarRepository
import com.rentautosofia.rentacar.repository.CustomerRepository
import com.rentautosofia.rentacar.repository.BookingRepository
import com.rentautosofia.rentacar.util.findOne
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.util.*
import com.rentautosofia.rentacar.util.*

const val PATH_ADMIN_BOOKING = "admin/booking"

data class BookingForView(
        var car: Car? = null,
        var customer: Customer? = null,
        var startDate: Date = Date(),
        var endDate: Date = Date(),
        var earnest: Int? = 0,
        var deposit: Int? = 0,
        var id: Int = 0)

enum class Action { // Correct order
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

data class AvailableForBooking(
        val car: Car = Car(),
        val startDate: Date = Date(),
        val endDate: Date? = Date() // May not have an endDate -> ... | Free FTW
)

@Controller
@RequestMapping("/$PATH_ADMIN_BOOKING")
class BookingController @Autowired
constructor(private val carRepository: CarRepository,
            private val customerRepository: CustomerRepository,
            private val bookingRepository: BookingRepository) {

    fun Booking.toBookingForView(): BookingForView =
            BookingForView(
                    car = this@BookingController.carRepository.findOne(this.carId),
                    customer = this@BookingController.customerRepository.findOne(this.customerId),
                    startDate = this.startDate,
                    endDate = this.endDate,
                    earnest = this.earnest,
                    deposit = this.deposit,
                    id = this.id)

    fun Booking.toScheduledBookingsList(): List<ScheduledBooking?> {
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
                this.bookingRepository.findAll()
        allBookings.sortWith(Comparator(fun(b1: Booking, b2: Booking): Int {
            return when {
                b1.startDate.before(b2.startDate) -> -1
                else -> 1
            }
        }))

        val viewBookings = allBookings.map { it.toBookingForView() }

        val carIds = viewBookings.map { it.car!!.id }.distinct()

        val bookingsSorted = mutableListOf<List<BookingForView>>()

        carIds.forEach { carId ->
            val oneCarList = viewBookings.filter { it.car!!.id == carId }
            bookingsSorted.add(oneCarList)
        }

        model.addAttribute("bookings", bookingsSorted.flatten())
        return "base-layout"
    }

    @GetMapping("/toDoNext")
    fun toDoNext(model: Model): String {
        model.addAttribute("view", "$PATH_ADMIN_BOOKING/toDoNext")
        val allBookings = this.bookingRepository.findAll()
        var scheduledBookingsList = mutableListOf<ScheduledBooking?>()

        allBookings.forEach { booking ->
            booking.toScheduledBookingsList().forEach {
                scheduledBookingsList.add(it)
            }
        }
        val today = Date().truncateDay()
        scheduledBookingsList = scheduledBookingsList.filter {
            !it!!.date.before(today)
        }.toMutableList()

        scheduledBookingsList.sortWith(Comparator(fun(b1: ScheduledBooking?, b2: ScheduledBooking?): Int {
            if (b1!!.date.before(b2!!.date)) return -1
            else if (b1.date.after(b2.date)) return 1
            // they are equal
            if (b1.car.id == b2.car.id) {
                return if (b1.action == Action.GIVE) 1 else -1
            }
            return 0
        }))
        model.addAttribute("scheduledBookings", scheduledBookingsList.toList())
        return "base-layout"
    }

    @GetMapping("/availableForBooking")
    fun availableForBooking(model: Model): String {
        model.addAttribute("view", "$PATH_ADMIN_BOOKING/availableForBooking")
        val allBookings = this.bookingRepository.findAll()
        allBookings.sortWith(Comparator(fun(b1: Booking, b2: Booking): Int {
            return when {
                b1.startDate.before(b2.startDate) -> -1
                else -> 1
            }
        }))

        val carIds = allBookings.map { it.carId }.distinct()

        val bookingsSorted = mutableListOf<List<Booking>>()

        carIds.forEach { carId ->
            val oneCarList = allBookings.filter { it.carId == carId }
            bookingsSorted.add(oneCarList)
        }

        val availableForBooking = mutableListOf<AvailableForBooking>()

        bookingsSorted.forEach { carBookings ->
            val car = this.carRepository.findOne(carBookings[0].carId)!! // All bookings here have the same carId
            val today = getDateFrom(Date().getProperFormat())
            if (today.before(carBookings[0].startDate)) {
                availableForBooking.add(AvailableForBooking(
                        car = car,
                        startDate = today,
                        endDate = carBookings[0].startDate // Guaranteed to have such booking with a date
                ))
            }
            carBookings.forEachIndexed { index, _ ->
                val startDate = carBookings[index].endDate // this booking's endDate
                val endDate = carBookings.getOrNull(index + 1)?.startDate // next one's startDate

                availableForBooking.add(AvailableForBooking(
                        car = car,
                        startDate = startDate,
                        endDate = endDate
                ))
            }
        }

        model.addAttribute("availableForBooking", availableForBooking)

        return "base-layout"
    }


    @GetMapping("/{id}/edit")
    fun edit(model: Model, @PathVariable id: Int): String {
        val booking = this.bookingRepository.findOne(id) ?: return "redirect:/$PATH_ADMIN_BOOKING/all"
        with(model) {
            addAttribute("booking", booking)
            addAttribute("view", "$PATH_ADMIN_BOOKING/edit")
        }
        return "base-layout"
    }

    @PostMapping("/{id}/edit")
    fun editProcess(model: Model, @PathVariable id: Int, newBooking: Booking): String {
        val oldBooking = this.bookingRepository.findOne(id) ?: return "redirect:/$PATH_ADMIN_BOOKING/all"

        this.bookingRepository.saveAndFlush(
                oldBooking.copy(startDate = newBooking.startDate,
                        endDate = newBooking.endDate,
                        earnest = newBooking.earnest,
                        deposit = newBooking.deposit,
                        pricePerDay = newBooking.pricePerDay,
                        notes = newBooking.notes)
        )

        return "redirect:/$PATH_ADMIN_BOOKING/all"
    }

    @GetMapping("/{id}/delete")
    fun delete(model: Model, @PathVariable id: Int): String {
        model.addAttribute("view", "$PATH_ADMIN_BOOKING/delete")
        val booking =
                this.bookingRepository.findOne(id) ?: return "redirect:/$PATH_ADMIN_BOOKING/all"
        val car =
                this.carRepository.findOne(booking.carId)
        val customer =
                this.customerRepository.findOne(booking.customerId)

        with(model) {
            addAttribute("booking", booking)
            addAttribute("car", car)
            addAttribute("customer", customer)
        }

        return "base-layout"
    }

    @PostMapping("/{id}/delete")
    fun deleteProcess(@PathVariable id: Int): String {
        val booking =
                this.bookingRepository.findOne(id) ?: return "redirect:/$PATH_ADMIN_BOOKING/all"
        this.bookingRepository.delete(booking)
        this.bookingRepository.flush()
        return "redirect:/$PATH_ADMIN_BOOKING/all"
    }
}
