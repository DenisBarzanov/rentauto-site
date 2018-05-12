package com.rentautosofia.rentacar.entity

import javax.persistence.*

@Entity
@Table(name = "customers")
data class Customer(@Column var phoneNumber: String = "",
                    @Column var name: String = "",
                    @Column var email: String = "") {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0
}


fun customer(function: Customer.() -> Unit): Customer {
    val customer = Customer()
    customer.function()
    return customer
}