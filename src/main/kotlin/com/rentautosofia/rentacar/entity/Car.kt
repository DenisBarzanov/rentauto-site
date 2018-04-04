package com.rentautosofia.rentacar.entity

import javax.persistence.*

@Entity
@Table(name = "cars")
data class Car(@Column var name: String = "",
               @Column var price: Int = 0,
               @Column(length = 1000) var imgURL: String = "") {

    fun getPricePerDayFor(days: Int) = when (days) {
        in 1 until 3 -> this.price
        in 4 until 8 -> this.price - 7
        in 9 until 15 -> this.price - 15
        in 16 until Int.MAX_VALUE -> this.price - 20
        else -> throw IllegalArgumentException("Cannot get get price per day for $days (NON-POSITIVE) days!!")
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0
}