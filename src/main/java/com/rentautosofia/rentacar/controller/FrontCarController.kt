package com.rentautosofia.rentacar.controller

import com.rentautosofia.rentacar.entity.Car
import com.rentautosofia.rentacar.repository.BookedCarRepository
import com.rentautosofia.rentacar.repository.CarRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.propertyeditors.CustomDateEditor
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.Locale
import java.text.DateFormat




@Controller
class FrontCarController @Autowired
constructor(private val carRepository: CarRepository,
            private val bookedCarRepository: BookedCarRepository) {

    private fun Date.getDifferenceInDaysTill(otherDate: Date): Int {
        val difference = if (this.time >= otherDate.time)
            otherDate.time - this.time else this.time - otherDate.time
        return TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS).toInt()
    }

    @GetMapping("/")
    fun searchCars(model: Model): String {
        model.addAttribute("view", "index")
        return "base-layout"
    }

    @GetMapping("/car/available")
    fun getAvailableCars(model: Model, @RequestParam("startDate", required = true) startDateString: String,
                         @RequestParam("endDate", required = true) endDateString: String): String {
        val format = SimpleDateFormat("dd-MM-yyyy")
        val startDate = format.parse(startDateString)
        val endDate = format.parse(endDateString)
        println(startDate)
        println(endDate)
        model.addAttribute("view", "car/available")
        val allCars = this.carRepository.findAll()
//        val availableCars : List<Car> = allCars.stream().filter {
//            it.id in this.bookedCarRepository.findAllIds().stream().filter {
//                this.bookedCarRepository.findOne(it).isBusyAt(startDate, endDate)
//            }.collect(toList())
//            //ids that are this.bookedCarRepository.findAll() //that fit between a date
//            }.collect(toList())
//
//        availableCars.forEach {
//                    it.price = it.getPriceForPeriodPerDay(startDate.getDifferenceInDaysTill(endDate))
//                }

        model.addAttribute("availableCars", allCars/*availableCars*/)
        return "base-layout"
    }

    @GetMapping("/car/book/{id}")
    fun order(model: Model, @PathVariable id: Int): String {
        val car = this.carRepository.findOne(id) ?: return "redirect:/"
        model.addAttribute("car", car)
        model.addAttribute("view", "car/book")
        return "base-layout"
    }

    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        val sdf = SimpleDateFormat("MM-dd-yyyy")
        sdf.isLenient = true
        binder.registerCustomEditor(Date::class.java, CustomDateEditor(sdf, true))
    }
}