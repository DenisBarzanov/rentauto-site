package com.rentautosofia.rentacar.entity

import javax.persistence.*

@Entity
@Table(name = "customers")
data class Customer(@Column var phoneNumber: String = "",
                    @Column var name: String = "") {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0
}
