package com.rentautosofia.rentacar.controller

import com.rentautosofia.rentacar.bindingModel.CustomerBindingModel
import com.rentautosofia.rentacar.entity.Customer
import com.rentautosofia.rentacar.repository.CustomerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import javax.validation.Valid

@Controller
class CustomerController @Autowired
constructor(private val customerRepository: CustomerRepository) {

    @GetMapping("/all_customers")
    fun showAllCustomers(model: Model) : String {
        val customers = this.customerRepository.findAll()
        model.addAttribute("customers", customers)
        model.addAttribute("view", "customer/all_customers")
        return "base-layout"
    }

    @GetMapping("/create_customer")
    fun createCustomer(model: Model): String {
        model.addAttribute("customer", CustomerBindingModel())
        model.addAttribute("view", "customer/create_customer")
        return "base-layout"
    }

    @PostMapping("/create_customer")
    fun createCustomerProcess(model: Model, @Valid customerBindingModel: CustomerBindingModel, bindingResult: BindingResult): String {
        if (bindingResult.hasErrors()) {
            model.addAttribute("view", "customer/create_customer")
            model.addAttribute("message", "Invalid data.")
            model.addAttribute("car", customerBindingModel)
            return "base-layout"
        }
        val customer = Customer(customerBindingModel.phoneNumber, customerBindingModel.name)
        this.customerRepository.saveAndFlush(customer)
        return "redirect:/all_customers"
    }

    @GetMapping("/delete_customer/{id}")
    fun delete(model: Model, @PathVariable id: Int): String {
        val customer = this.customerRepository.findOne(id) ?: return "redirect:/show_all_customers"
        model.addAttribute("customer", customer)
        model.addAttribute("view", "customer/delete_customer")
        return "base-layout"
    }

    @PostMapping("/delete_customer/{id}")
    fun deleteProcess(@PathVariable id: Int): String {
        val customer = this.customerRepository.findOne(id) ?: return "redirect:/show_all_customers"
        this.customerRepository.delete(customer)
        this.customerRepository.flush()
        return "redirect:/all_customers"
    }
}