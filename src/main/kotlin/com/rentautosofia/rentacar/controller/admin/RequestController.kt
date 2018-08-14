package com.rentautosofia.rentacar.controller.admin

import com.rentautosofia.rentacar.entity.bookedCar
import com.rentautosofia.rentacar.repository.BookedCarRepository
import com.rentautosofia.rentacar.repository.CarRepository
import com.rentautosofia.rentacar.repository.CustomerRepository
import com.rentautosofia.rentacar.repository.BookingRequestRepository
import com.rentautosofia.rentacar.util.getIds
import com.rentautosofia.rentacar.util.findOne
import org.springframework.stereotype.Controller
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

const val PATH_ADMIN_REQUEST = "admin/bookRequest"

@Controller
@RequestMapping("/$PATH_ADMIN_REQUEST")
class CustomerRequestController @Autowired
constructor(private val carRepository: CarRepository,
            private val customerRepository: CustomerRepository,
            private val bookedCarRepository: BookedCarRepository,
            private val bookingRequestRepository: BookingRequestRepository) {

    @GetMapping("/all")
    fun requests(model: Model): String {
        with(model) {
            addAttribute("view", "$PATH_ADMIN_REQUEST/all")
            addAttribute("requestIds", this@CustomerRequestController.bookingRequestRepository.findAll().getIds())
        }
        return "base-layout"
    }

    @GetMapping("/{id}/accept")
    fun accept(model: Model, @PathVariable id: Int): String {
        model.addAttribute("view", "$PATH_ADMIN_REQUEST/accept")
        val requested =
                this.bookingRequestRepository.findOne(id) ?: return "redirect:/$PATH_ADMIN_REQUEST/all"
        val car =
                this.carRepository.findOne(requested.carId)
        val customer =
                this.customerRepository.findOne(requested.customerId)
        with(model) {
            addAttribute("car", car)
            addAttribute("customer", customer)
            addAttribute("requested", requested)
        }
        return "base-layout"
    }

    @PostMapping("/{id}/accept")
    fun acceptProcess(model: Model, @PathVariable id: Int, @RequestParam isAccepted: Boolean): String {
        val bookingRequest =
                this.bookingRequestRepository.findOne(id) ?: return "redirect:/$PATH_ADMIN_REQUEST/all"
        if (isAccepted) {
            val nowRentedCar = bookedCar {
                carId = bookingRequest.carId
                customerId = bookingRequest.customerId
                startDate = bookingRequest.startDate
                endDate = bookingRequest.endDate
                pricePerDay = bookingRequest.pricePerDay // saving with overriding option later
            }
            this.bookedCarRepository.saveAndFlush(nowRentedCar)
        }
        this.bookingRequestRepository.delete(bookingRequest)
        this.bookingRequestRepository.flush()

        return "redirect:/$PATH_ADMIN_REQUEST/all"
    }
}
