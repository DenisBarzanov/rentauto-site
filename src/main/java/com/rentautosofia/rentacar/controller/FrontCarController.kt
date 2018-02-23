package com.rentautosofia.rentacar.controller

import com.rentautosofia.rentacar.bindingModel.CustomerBindingModel
import com.rentautosofia.rentacar.entity.Car
import com.rentautosofia.rentacar.entity.Customer
import com.rentautosofia.rentacar.entity.RequestedCar
import com.rentautosofia.rentacar.repository.RentedCarRepository
import com.rentautosofia.rentacar.repository.CarRepository
import com.rentautosofia.rentacar.repository.CustomerRepository
import com.rentautosofia.rentacar.repository.RequestedCarRepository
import com.rentautosofia.rentacar.util.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.validation.BindingResult
import java.util.stream.Collectors.toList
import javax.validation.Valid


@Controller
class FrontCarController @Autowired
constructor(private val carRepository: CarRepository,
            private val rentedCarRepository: RentedCarRepository,
            private val customerRepository: CustomerRepository,
            private val requestedCarRepository: RequestedCarRepository) {

    @Autowired
    private lateinit var managerInformer: InformManager

    @GetMapping("/")
    fun searchCars(model: Model): String {
        model.addAttribute("view", "index")
        val cars = this.carRepository.findAll()
        model.addAttribute("cars", cars)
        return "base-layout"
    }

    @GetMapping("/car/search")
    fun search(model: Model): String {
        model.addAttribute("view", "car/search")
        return "base-layout"
    }

    @GetMapping("/car/available")
    fun getAvailableCars(model: Model, @RequestParam("startDate", required = true) startDateString: String,
                         @RequestParam("endDate", required = true) endDateString: String): String {
        val startDate = getDateFrom(startDateString)
        val endDate = getDateFrom(endDateString)
        model.addAttribute("view", "car/available")
        val allCars = this.carRepository.findAll()

        val days = startDate daysTill endDate

        val rentedCarIdsInPeriod = this.rentedCarRepository.findAllIdsOfBookedCarsBetween(startDate, endDate)

        val availableCars : MutableList<Car> = allCars.stream().filter {
            it.id !in rentedCarIdsInPeriod
        }.collect(toList())

        for(car in availableCars) {
            car.price = car.getPricePerDayFor(days)
        }

        model.addAttribute("availableCars", availableCars)
        return "base-layout"
    }

    @GetMapping("/car/{id}/book")
    fun order(model: Model,
              @PathVariable id: Int,
              @RequestParam("startDate") startDateString: String,
              @RequestParam("endDate") endDateString: String,
              @RequestParam pricePerDay: Int): String {

        val car = this.carRepository.findOne(id) ?: return "redirect:/"
        model.addAttribute("car", car)
        model.addAttribute("customer", CustomerBindingModel())
        model.addAttribute("view", "car/book")

        val startDate = getDateFrom(startDateString)
        val endDate = getDateFrom(endDateString)
        val days = startDate daysTill endDate

        model.addAttribute("totalPrice", pricePerDay*days)

        return "base-layout"
    }
    @PostMapping("/car/{id}/book")
    fun orderProcess(model: Model,
                     @PathVariable id: Int,
                     @RequestBody requestParams: String,
                     @Valid customerBindingModel: CustomerBindingModel,
                     bindingResult: BindingResult) : String {

        if (bindingResult.hasErrors()) {
            model.addAttribute("view", "index")
            model.addAttribute("message", "Invalid data.")
            return "base-layout"
        }

        // todo govno code
        val args = requestParams.split("&")
        val phoneNumber = args[0].split("=")[1]
        val startDateString = args[1].split("=")[1]
        val endDateString = args[2].split("=")[1]
        val price = args[3].split("=")[1].toInt()



        val customer = this.customerRepository.findOneByPhoneNumber(phoneNumber)
                ?: Customer(phoneNumber, "")
        val startDate = getDateFrom(startDateString)
        val endDate = getDateFrom(endDateString)
        val car = this.carRepository.findOne(id)

        this.customerRepository.saveAndFlush(customer)

        val requestedCar = RequestedCar(car.id,customer.id, startDate, endDate)
        this.requestedCarRepository.saveAndFlush(requestedCar)

        this.managerInformer.informManagerWith(requestedCar)

        return "redirect:/thank_you"
    }

    @GetMapping("/thank_you")
    fun thankYou(model: Model): String {
        model.addAttribute("view", "thankYou")
        return "base-layout"
    }

    @GetMapping("/car/inclusions")
    fun showCarInclusion(model: Model) : String {
        model.addAttribute("view", "car/inclusions")
        return "base-layout"
    }

    @GetMapping("/car/priceDependency")
    fun showPriceDependency(model: Model): String {
        model.addAttribute("view", "car/priceDependency")
        return "base-layout"
    }
}
