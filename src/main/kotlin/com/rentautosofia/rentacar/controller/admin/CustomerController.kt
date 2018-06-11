package com.rentautosofia.rentacar.controller.admin

import com.rentautosofia.rentacar.entity.Customer
import com.rentautosofia.rentacar.repository.CustomerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.validation.Valid
import com.rentautosofia.rentacar.util.findOne


const val PATH_ADMIN_CUSTOMER = "admin/customer"

@RequestMapping("/$PATH_ADMIN_CUSTOMER")
@Controller
class CustomerController @Autowired
constructor(private val customerRepository: CustomerRepository) {

    @GetMapping("/all")
    fun showAllCustomers(model: Model) : String {
        val customers = this.customerRepository.findAll()
        model.addAttribute("customers", customers)
        model.addAttribute("view", "$PATH_ADMIN_CUSTOMER/all")
        return "base-layout"
    }

    @GetMapping("/create")
    fun createCustomer(model: Model): String {
        model.addAttribute("customer", Customer())
        model.addAttribute("view", "$PATH_ADMIN_CUSTOMER/create")
        return "base-layout"
    }

    @PostMapping("/create")
    fun createCustomerProcess(model: Model, customer: Customer): String {
        this.customerRepository.saveAndFlush(customer)
        return "redirect:/$PATH_ADMIN_CUSTOMER/all"
    }

    @GetMapping("/{id}/edit")
    fun edit(model: Model, @PathVariable id: Int): String {
        val customer = this.customerRepository.findOne(id) ?: return "redirect:/$PATH_ADMIN_CUSTOMER/all"
        with(model) {
            addAttribute("customer", customer)
            addAttribute("view", "$PATH_ADMIN_CUSTOMER/edit")
        }
        return "base-layout"
    }

    @PostMapping("/{id}/edit")
    fun editProcess(model: Model, @PathVariable id: Int, customer: Customer): String {
        this.customerRepository.findOne(id) ?: return "redirect:/$PATH_ADMIN_CUSTOMER/all"
        this.customerRepository.saveAndFlush(customer)
        return "redirect:/$PATH_ADMIN_CUSTOMER/all"
    }

    @GetMapping("/{id}/delete")
    fun delete(model: Model, @PathVariable id: Int): String {
        val customer = this.customerRepository.findOne(id) ?: return "redirect:$PATH_ADMIN_CUSTOMER/all"
        with(model) {
            addAttribute("customer", customer)
            addAttribute("view", "$PATH_ADMIN_CUSTOMER/delete")
        }
        return "base-layout"
    }

    @PostMapping("/{id}/delete")
    fun deleteProcess(@PathVariable id: Int): String {
        val customer = this.customerRepository.findOne(id) ?: return "redirect:$PATH_ADMIN_CUSTOMER/all"
        this.customerRepository.delete(customer)
        this.customerRepository.flush()
        return "redirect:/$PATH_ADMIN_CUSTOMER/all"
    }

}
