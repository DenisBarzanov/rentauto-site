package com.rentautosofia.rentacar.controller

import com.rentautosofia.rentacar.bindingModel.CustomerBindingModel
import com.rentautosofia.rentacar.entity.BookedCar
import com.rentautosofia.rentacar.entity.Car
import com.rentautosofia.rentacar.entity.Customer
import com.rentautosofia.rentacar.repository.BookedCarRepository
import com.rentautosofia.rentacar.repository.CarRepository
import com.rentautosofia.rentacar.repository.CustomerRepository
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
            private val bookedCarRepository: BookedCarRepository,
            private val customerRepository: CustomerRepository) {

    @Autowired
    private lateinit var managerInformer: InformManager

    @GetMapping("/")
    fun searchCars(model: Model): String {
        model.addAttribute("view", "index")
        return "base-layout"
    }

    @GetMapping("/car/available")
    fun getAvailableCars(model: Model, @RequestParam("startDate", required = true) startDateString: String,
                         @RequestParam("endDate", required = true) endDateString: String): String {
        val startDate = getDateFromString(startDateString)
        val endDate = getDateFromString(endDateString)
        model.addAttribute("view", "car/available")
        val allCars = this.carRepository.findAll()

        val days = startDate.getDifferenceInDaysTill(endDate)

        val availableCars : MutableList<Car> = allCars.stream().filter {
            it.id !in this.bookedCarRepository.findAllIdsOfBookedCarsBetween(startDate, endDate)
        }.collect(toList())

        for(car in availableCars) {
            car.price = car.getPriceForPeriodPerDay(days)
        }

        model.addAttribute("availableCars", availableCars)
        return "base-layout"
    }

    @GetMapping("/car/book/{id}")
    fun order(model: Model, @PathVariable id: Int): String {
        val car = this.carRepository.findOne(id) ?: return "redirect:/"
        model.addAttribute("car", car)
        model.addAttribute("customer", CustomerBindingModel())
        model.addAttribute("view", "car/book")
        model.addAttribute("startDate", "")
        return "base-layout"
    }
    @PostMapping("/car/book/{id}")
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

        val args = requestParams.split("&")
        val phoneNumber = args[0].split("=")[1]
        val startDateString = args[1].split("=")[1]
        val endDateString = args[2].split("=")[1]
        val price = args[3].split("=")[1].toInt()

        val customer = this.customerRepository.findOneByPhoneNumber(phoneNumber)
                ?: Customer(phoneNumber, "")
        val startDate = getDateFromString(startDateString)
        val endDate = getDateFromString(endDateString)
        val car = this.carRepository.findOne(id)
        val bookedCar = BookedCar(car.id,customer.id, startDate, endDate)
        this.managerInformer.informManagerWith(bookedCar, price, phoneNumber)
//        this.bookedCarRepository.saveAndFlush(bookedCar)
        return "redirect:/thank_you"
    }

    @GetMapping("/thank_you")
    fun thankYou(model: Model): String {
        model.addAttribute("view", "thankYou")
        return "base-layout"
    }

    @GetMapping("/car/inclusions")
    fun showCarInclusion(model: Model) : String {
        model.addAttribute("view", "/car/inclusions")
        return "base-layout"
    }
    @GetMapping("/car/priceDependency")
    fun showPriceDependency(model: Model): String {
        model.addAttribute("view", "/car/priceDependency")
        return "base-layout"
    }
}
