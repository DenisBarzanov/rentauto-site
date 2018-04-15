package com.rentautosofia.rentacar.controller.admin

import com.rentautosofia.rentacar.bindingModel.CarBindingModel
import com.rentautosofia.rentacar.entity.car
import com.rentautosofia.rentacar.repository.CarRepository
import com.rentautosofia.rentacar.util.findOne
import javax.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
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
        model.addAttribute("car", CarBindingModel())
        model.addAttribute("view", "$PATH_ADMIN_CAR/create")
        return "base-layout"
    }

    @PostMapping("/create")
    fun createProcess(model: Model, @Valid carBindingModel: CarBindingModel, bindingResult: BindingResult): String {
        if (bindingResult.hasErrors()) {
            model.addAttribute("view", "$PATH_ADMIN_CAR/create")
            model.addAttribute("message", "Invalid data.")
            model.addAttribute("car", carBindingModel)
            return "base-layout"
        }

        val car = car {
            name = carBindingModel.name
            price = carBindingModel.price
            imgURL = carBindingModel.imgURL
        }

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
    fun editProcess(model: Model, @PathVariable id: Int, @Valid carBindingModel: CarBindingModel, bindingResult: BindingResult): String {
        if (bindingResult.hasErrors()) {
            model.addAttribute("message", "Invalid data.")
            model.addAttribute("car", carBindingModel)
            model.addAttribute("view", "$PATH_ADMIN_CAR/edit")
            return "base-layout"
        }
        val car = this.carRepository.findOne(id) ?: return "redirect:/$PATH_ADMIN_ALL_CARS"
        with(car) {
            name = carBindingModel.name
            imgURL = carBindingModel.imgURL
            price = carBindingModel.price
        }
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
