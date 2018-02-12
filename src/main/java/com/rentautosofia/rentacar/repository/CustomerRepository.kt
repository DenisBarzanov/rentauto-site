package com.rentautosofia.rentacar.repository

import com.rentautosofia.rentacar.entity.Customer
import org.springframework.data.jpa.repository.JpaRepository

interface CustomerRepository : JpaRepository<Customer, Int>