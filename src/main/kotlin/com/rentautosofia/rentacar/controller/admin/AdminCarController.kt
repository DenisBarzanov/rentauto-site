package com.rentautosofia.rentacar.controller.admin

import com.rentautosofia.rentacar.entity.Car
import com.rentautosofia.rentacar.repository.CarRepository
import com.rentautosofia.rentacar.util.findOne
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

const val PATH_ADMIN_CAR = "admin/car"
const val PATH_ADMIN_ALL_CARS = "$PATH_ADMIN_CAR/all"

@RequestMapping("/$PATH_ADMIN_CAR")
@Controller
class CarController @Autowired
constructor(private val carRepository: CarRepository) {

    @GetMapping("/create")
    fun create(model: Model): String {
        model.addAttribute("car", Car())
        model.addAttribute("view", "$PATH_ADMIN_CAR/create")
        return "base-layout"
    }

    @PostMapping("/create")
    fun createProcess(model: Model, car: Car): String {
        this.carRepository.saveAndFlush(car)
        return "redirect:/$PATH_ADMIN_ALL_CARS"
    }

    @GetMapping("/{id}/edit")
    fun edit(model: Model, @PathVariable id: Int): String {
        val car = this.carRepository.findOne(id) ?: return "redirect:/$PATH_ADMIN_ALL_CARS"
        model.addAttribute("car", car)
        model.addAttribute("view", "$PATH_ADMIN_CAR/edit")
        return "base-layout"
    }

    @PostMapping("/{id}/edit")
    fun editProcess(model: Model, @PathVariable id: Int, newCar: Car): String {
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("message", "Invalid data.")
//            model.addAttribute("car", carBindingModel)
//            model.addAttribute("view", "$PATH_ADMIN_CAR/edit")
//            return "base-layout"
//        }
        this.carRepository.findOne(id) ?: return "redirect:/$PATH_ADMIN_ALL_CARS"
        val car = newCar.copy(id = id)
        this.carRepository.saveAndFlush(car)
        return "redirect:/$PATH_ADMIN_ALL_CARS"
    }

    @GetMapping("/{id}/delete")
    fun delete(model: Model, @PathVariable id: Int): String {
        val car = this.carRepository.findOne(id) ?: return "redirect:/$PATH_ADMIN_ALL_CARS"
        model.addAttribute("car", car)
        model.addAttribute("view", "$PATH_ADMIN_CAR/delete")
        return "base-layout"
    }

    @PostMapping("/{id}/delete")
    fun deleteProcess(@PathVariable id: Int): String {
        val car = this.carRepository.findOne(id) ?: return "redirect:/$PATH_ADMIN_ALL_CARS"
        this.carRepository.delete(car)
        this.carRepository.flush()
        return "redirect:/$PATH_ADMIN_ALL_CARS"
    }

    @GetMapping("/all")
    fun all(model: Model): String {
        model.addAttribute("view", PATH_ADMIN_ALL_CARS)
        val cars = this.carRepository.findAll()
        model.addAttribute("cars", cars)
        return "base-layout"
    }
}
