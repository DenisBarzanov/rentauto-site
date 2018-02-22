package com.rentautosofia.rentacar.entity

import javax.persistence.*

@Entity
@Table(name = "cars")
data class Car(@Column var name: String = "",
               @Column var price: Int = 0,
               @Column(length = 1000) var imgURL: String = "") {

    fun getPriceForPeriodPerDay(days: Int) = when (days) {
        in 0..3 -> this.price
        in 3..8 -> this.price - 7
        in 8..15 -> this.price - 15
        in 15..Int.MAX_VALUE -> this.price - 20
        else -> this.price
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0
}

