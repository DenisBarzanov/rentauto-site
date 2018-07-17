package com.rentautosofia.rentacar.controller

import com.rentautosofia.rentacar.entity.*
import com.rentautosofia.rentacar.repository.RentedCarRepository
import com.rentautosofia.rentacar.repository.CarRepository
import com.rentautosofia.rentacar.repository.CustomerRepository
import com.rentautosofia.rentacar.repository.RequestedCarRepository
import com.rentautosofia.rentacar.util.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import org.springframework.util.MultiValueMap

@Controller
class FrontCarController @Autowired
constructor(private val carRepository: CarRepository,
            private val rentedCarRepository: RentedCarRepository,
            private val customerRepository: CustomerRepository,
            private val requestedCarRepository: RequestedCarRepository) {

    @Autowired
    private lateinit var managerInformer: ManagerInformer

    @GetMapping("/")
    fun searchCars(model: Model): String {
        model.addAttribute("view", "index")
        return "client-base-layout"
    }

    @PostMapping("/car/search")
    fun searchProcess(@RequestBody input: String) = "redirect:/car/available?$input"

    @GetMapping("/car/available")
    fun getAvailableCars(model: Model,
                         @RequestParam("datetime_pick", required = true) startDateString: String,
                         @RequestParam("datetime_off", required = true) endDateString: String): String {
        val startDate = getDateFrom(startDateString)
        val endDate = getDateFrom(endDateString)
        if (startDate.after(endDate)) {
            return "redirect:/"
        }
        model.addAttribute("view", "car/available")
        val allCars = this.carRepository.findAll()

        val days = startDate daysTill endDate

        val rentedCarIdsInPeriod = this.rentedCarRepository.findAllIdsOfBookedCarsBetween(startDate, endDate)

        val availableCars : MutableList<Car> = allCars.asSequence().filter {
            it.id !in rentedCarIdsInPeriod
        }.toMutableList()

        for(car in availableCars) {
            car.price = car.getPricePerDayFor(days)
        }
        model.addAttribute("availableCars", availableCars)

        return "client-base-layout"
    }

    @GetMapping("/car/{id}/book")
    fun order(model: Model,
              @PathVariable id: Int,
              @RequestParam("datetime_pick") startDateString: String,
              @RequestParam("datetime_off") endDateString: String): String {

        val car = this.carRepository.findOne(id) ?: return "redirect:/"
        model.addAttribute("car", car)
        model.addAttribute("view", "car/book")
        model.addAttribute("customer", Customer())

        val booking = BaseBooking(startDate = getDateFrom(startDateString),
                endDate = getDateFrom(endDateString),
                carId = car.id)

        model.addAttribute("totalPrice", booking.totalPrice)

        return "client-base-layout"
    }
    @PostMapping("/car/{id}/book", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun orderProcess(model: Model,
                     @PathVariable id: Int,
                     newCustomer: Customer,
                     request: HttpServletRequest,
                     @RequestBody multiParams: MultiValueMap<String, String>) : String {

        val params = multiParams.toSingleValueMap()

        val startDateString = params["startDate"]!!
        val endDateString = params["endDate"]!!

        val customer: Customer
        val existingCustomer = this.customerRepository.findOneByPhoneNumber(newCustomer.phoneNumber)

        if (existingCustomer == null) {
            customer = newCustomer
            customer.id = this.customerRepository.saveAndFlush(customer).id // save returns saved object
        } else {
            customer = existingCustomer
        }	

        val startDate = getDateFrom(startDateString)
        val endDate = getDateFrom(endDateString)
        val car = this.carRepository.findOne(id) ?: return "redirect:/"

        val requestedCar = RequestedCar(car.id, customer.id, startDate, endDate)

        if (!this.requestedCarRepository.hasBooking(requestedCar)) {
            // No such booking yet
            this.requestedCarRepository.saveAndFlush(requestedCar)
            this.managerInformer.informManagerWith(requestedCar)
            this.managerInformer.informClientWith(requestedCar)
        }

        val willPayDepositNow = params["payDepositNow"].toString() == "on"

        return if (willPayDepositNow)
            "forward:/pay"
        else "redirect:/cancel"
    }

    @GetMapping("/success")
    fun success(model: Model): String {
        model.addAttribute("view", "success")
        return "empty-client-base-layout"
    }

    @GetMapping("/cancel")
    fun cancel(model: Model): String {
        model.addAttribute("view", "cancel")
        return "empty-client-base-layout"
    }
}
