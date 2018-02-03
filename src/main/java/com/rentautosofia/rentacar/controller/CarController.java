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
package com.rentautosofia.rentacar.controller;

import com.rentautosofia.rentacar.bindingModel.CarBindingModel;
import com.rentautosofia.rentacar.entity.Car;
import com.rentautosofia.rentacar.repository.CarRepository;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CarController {
    private final CarRepository carRepository;

    @Autowired
    public CarController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @GetMapping(value={"/"})
    public String index(Model model) {
        List cars = this.carRepository.findAll();
        model.addAttribute("cars", cars);
        model.addAttribute("view", "car/index");
        return "base-layout";
    }

    @GetMapping(value={"/create"})
    public String create(Model model) {
        model.addAttribute("car", new CarBindingModel());
        model.addAttribute("view", "car/create");
        return "base-layout";
    }

    @PostMapping(value={"/create"})
    public String createProcess(Model model, @Valid CarBindingModel carBindingModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("view", "car/create");
            model.addAttribute("message", "Invalid data.");
            model.addAttribute("car", carBindingModel);
            return "base-layout";
        }
        Car car = new Car(carBindingModel.getName(), carBindingModel.getPrice(), carBindingModel.getImgURL());
        this.carRepository.saveAndFlush(car);
        return "redirect:/";
    }

    @GetMapping(value={"/edit/{id}"})
    public String edit(Model model, @PathVariable int id) {
        Car car = this.carRepository.findOne(id);
        if (car == null) {
            return "redirect:/";
        }
        model.addAttribute("car", car);
        model.addAttribute("view", "car/edit");
        return "base-layout";
    }

    @PostMapping(value={"/edit/{id}"})
    public String editProcess(Model model, @PathVariable int id, @Valid CarBindingModel carBindingModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("message", "Invalid data.");
            model.addAttribute("car", carBindingModel);
            model.addAttribute("view", "car/edit");
            return "base-layout";
        }
        Car car = this.carRepository.findOne(id);
        if (car == null) {
            return "redirect:/";
        }
        car.setName(carBindingModel.getName());
        car.setImgURL(carBindingModel.getImgURL());
        car.setPrice(carBindingModel.getPrice());
        this.carRepository.saveAndFlush(car);
        return "redirect:/";
    }

    @GetMapping(value={"/delete/{id}"})
    public String delete(Model model, @PathVariable int id) {
        Car car = this.carRepository.findOne(id);
        if (car == null) {
            return "redirect:/";
        }
        model.addAttribute("car", car);
        model.addAttribute("view", "car/delete");
        return "base-layout";
    }

    @PostMapping(value={"/delete/{id}"})
    public String deleteProcess(@PathVariable int id) {
        Car car = this.carRepository.findOne(id);
        if (car == null) {
            return "redirect:/";
        }
        this.carRepository.delete(car);
        this.carRepository.flush();
        return "redirect:/";
    }

    @GetMapping(value={"/order/{id}"})
    public String order(Model model, @PathVariable int id) {
        Car car = this.carRepository.findOne(id);
        if (car == null) {
            return "redirect:/";
        }
        model.addAttribute("car", car);
        model.addAttribute("view", "car/order");
        return "base-layout";
    }
}

