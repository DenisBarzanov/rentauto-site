package com.rentautosofia.rentacar.repository

import com.rentautosofia.rentacar.entity.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepository : JpaRepository<Customer, Int> {
    fun findOneByPhoneNumber(phoneNumber: String) : Customer?
}