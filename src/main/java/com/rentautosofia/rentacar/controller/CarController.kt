/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.rentautosofia.rentacar.bindingModel.CarBindingModel
 *  com.rentautosofia.rentacar.controller.CarController
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
package com.rentautosofia.rentacar.controller

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

@Controller
class CarController @Autowired
constructor(private val carRepository: CarRepository) {

    @GetMapping("/")
    fun index(model: Model): String {
        val cars = this.carRepository.findAll()
        model.addAttribute("cars", cars)
        model.addAttribute("view", "car/index")
        return "base-layout"
    }

    @GetMapping("/create_car")
    fun create_car(model: Model): String {
        model.addAttribute("car", CarBindingModel())
        model.addAttribute("view", "car/create_car")
        return "base-layout"
    }

    @PostMapping("/create_car")
    fun createCarProcess(model: Model, @Valid carBindingModel: CarBindingModel, bindingResult: BindingResult): String {
        if (bindingResult.hasErrors()) {
            model.addAttribute("view", "car/create_car")
            model.addAttribute("message", "Invalid data.")
            model.addAttribute("car", carBindingModel)
            return "base-layout"
        }
        val car = Car(carBindingModel.name, carBindingModel.price, carBindingModel.imgURL)
        this.carRepository.saveAndFlush(car)
        return "redirect:/"
    }

    @GetMapping("/edit/{id}")
    fun edit(model: Model, @PathVariable id: Int): String {
        val car = this.carRepository.findOne(id) ?: return "redirect:/"
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
        val car = this.carRepository.findOne(id) ?: return "redirect:/"
        car.name = carBindingModel.name
        car.imgURL = carBindingModel.imgURL
        car.price = carBindingModel.price
        this.carRepository.saveAndFlush(car)
        return "redirect:/"
    }

    @GetMapping("/delete/{id}")
    fun delete(model: Model, @PathVariable id: Int): String {
        val car = this.carRepository.findOne(id) ?: return "redirect:/"
        model.addAttribute("car", car)
        model.addAttribute("view", "car/delete")
        return "base-layout"
    }

    @PostMapping("/delete/{id}")
    fun deleteProcess(@PathVariable id: Int): String {
        val car = this.carRepository.findOne(id) ?: return "redirect:/"
        this.carRepository.delete(car)
        this.carRepository.flush()
        return "redirect:/"
    }

    @GetMapping("/order/{id}")
    fun order(model: Model, @PathVariable id: Int): String {
        val car = this.carRepository.findOne(id) ?: return "redirect:/"
        model.addAttribute("car", car)
        model.addAttribute("view", "car/order")
        return "base-layout"
    }
}

