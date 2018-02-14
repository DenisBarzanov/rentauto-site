/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.rentautosofia.rentacar.bindingModel.CarBindingModel
 *  com.rentautosofia.rentacar.controller.admin.CarController
 *  com.rentautosofia.rentacar.entity.Car
 *  com.rentautosofia.rentacar.repository.CarRepository
 *  javax.validation.Valid
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Controller
 *  org.springframework.ui.Model
 *  org.springframework.validation.BindingResult
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 */
package com.rentautosofia.rentacar.controller.admin

import com.rentautosofia.rentacar.bindingModel.CarBindingModel
import com.rentautosofia.rentacar.entity.Car
import com.rentautosofia.rentacar.repository.CarRepository
import javax.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

const val PATH_ADMIN_CAR = "/admin/car"
const val PATH_ADMIN_ALL_CARS = PATH_ADMIN_CAR + "/all"

@RequestMapping(PATH_ADMIN_CAR)
@Controller
class CarController @Autowired
constructor(private val carRepository: CarRepository) {

    @GetMapping("/create")
    fun create(model: Model): String {
        model.addAttribute("car", CarBindingModel())
        model.addAttribute("view", "car/create")
        return "base-layout"
    }

    @PostMapping("/create")
    fun createProcess(model: Model, @Valid carBindingModel: CarBindingModel, bindingResult: BindingResult): String {
        if (bindingResult.hasErrors()) {
            model.addAttribute("view", "car/create")
            model.addAttribute("message", "Invalid data.")
            model.addAttribute("car", carBindingModel)
            return "base-layout"
        }
        val car = Car(carBindingModel.name, carBindingModel.price, carBindingModel.imgURL)
        this.carRepository.saveAndFlush(car)
        return "redirect:/$PATH_ADMIN_ALL_CARS"
    }

    @GetMapping("/edit/{id}")
    fun edit(model: Model, @PathVariable id: Int): String {
        val car = this.carRepository.findOne(id) ?: return "redirect:/$PATH_ADMIN_ALL_CARS"
        model.addAttribute("car", car)
        model.addAttribute("view", "car/edit")
        return "base-layout"
    }

    @PostMapping("/edit/{id}")
    fun editProcess(model: Model, @PathVariable id: Int, @Valid carBindingModel: CarBindingModel, bindingResult: BindingResult): String {
        if (bindingResult.hasErrors()) {
            model.addAttribute("message", "Invalid data.")
            model.addAttribute("car", carBindingModel)
            model.addAttribute("view", "car/edit")
            return "base-layout"
        }
        val car = this.carRepository.findOne(id) ?: return "redirect:/$PATH_ADMIN_ALL_CARS"
        car.name = carBindingModel.name
        car.imgURL = carBindingModel.imgURL
        car.price = carBindingModel.price
        this.carRepository.saveAndFlush(car)
        return "redirect:/$PATH_ADMIN_ALL_CARS"
    }

    @GetMapping("/delete/{id}")
    fun delete(model: Model, @PathVariable id: Int): String {
        val car = this.carRepository.findOne(id) ?: return "redirect:/$PATH_ADMIN_ALL_CARS"
        model.addAttribute("car", car)
        model.addAttribute("view", "car/delete")
        return "base-layout"
    }

    @PostMapping("/delete/{id}")
    fun deleteProcess(@PathVariable id: Int): String {
        val car = this.carRepository.findOne(id) ?: return "redirect:/$PATH_ADMIN_ALL_CARS"
        this.carRepository.delete(car)
        this.carRepository.flush()
        return "redirect:/$PATH_ADMIN_ALL_CARS"
    }

}

