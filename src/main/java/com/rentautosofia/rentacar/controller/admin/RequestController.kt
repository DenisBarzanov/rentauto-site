package com.rentautosofia.rentacar.controller.admin

import com.rentautosofia.rentacar.entity.BookedCar
import com.rentautosofia.rentacar.repository.RentedCarRepository
import com.rentautosofia.rentacar.repository.CarRepository
import com.rentautosofia.rentacar.repository.CustomerRepository
import com.rentautosofia.rentacar.repository.RequestedCarRepository
import com.rentautosofia.rentacar.util.getProperFormat
import com.rentautosofia.rentacar.util.getIds
import org.springframework.stereotype.Controller
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

const val PATH_ADMIN_REQUEST = "admin/bookRequest"

@Controller
@RequestMapping("/" + PATH_ADMIN_REQUEST)
class CustomerRequestController @Autowired
constructor(private val carRepository: CarRepository,
            private val customerRepository: CustomerRepository,
            private val rentedCarRepository: RentedCarRepository,
            private val requestedCarRepository: RequestedCarRepository) {

    @GetMapping("/all")
    fun requests(model: Model): String {
        model.addAttribute("view", "$PATH_ADMIN_REQUEST/all")
        model.addAttribute("requestIds",
                this.requestedCarRepository.findAll().getIds())
        return "base-layout"
    }

    @GetMapping("/{id}/accept")
    fun accept(model: Model, @PathVariable id: Int): String {
        model.addAttribute("view", "$PATH_ADMIN_REQUEST/accept")
        val requested =
                this.requestedCarRepository.findOne(id) ?: return "redirect:/$PATH_ADMIN_REQUEST/all"
        val car =
                this.carRepository.findOne(requested.carId)
        val customer =
                this.customerRepository.findOne(requested.customerId)
        model.addAttribute("car", car)
        model.addAttribute("customer", customer)
        model.addAttribute("startDate", requested.startDate.getProperFormat())
        model.addAttribute("endDate", requested.endDate.getProperFormat())
        return "base-layout"
    }

    @PostMapping("/{id}/accept")
    fun acceptProcess(model: Model, @PathVariable id: Int, @RequestParam(name = "isAccepted") isAccepted: Boolean): String {
        val requestedCar =
                this.requestedCarRepository.findOne(id) ?: return "redirect:/$PATH_ADMIN_REQUEST/all"
        if (isAccepted) {
            val nowRentedCar = BookedCar(requestedCar.carId, requestedCar.customerId, requestedCar.startDate, requestedCar.endDate)
            this.rentedCarRepository.saveAndFlush(nowRentedCar)
        }
        this.requestedCarRepository.delete(requestedCar)
        this.requestedCarRepository.flush()

        return "redirect:/$PATH_ADMIN_REQUEST/all"
    }
}