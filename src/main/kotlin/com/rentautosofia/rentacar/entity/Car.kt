package com.rentautosofia.rentacar.entity

import com.rentautosofia.rentacar.transmission.Transmission
import javax.persistence.*

@Entity
@Table(name = "cars")
data class Car(@Column var name: String = "",
               @Column var price: Int = 0,
               @Column(name = "imgurl", length = 1000) var imgUrl: String = "",
               @Column var LPG: Boolean = false,
               @Column @Enumerated(EnumType.STRING)
               var transmission: Transmission = Transmission.MANUAL,
               @Column var seatCount: Int? = null,
               @Id
               @GeneratedValue(strategy = GenerationType.IDENTITY)
               var id: Int = 0,
               @Column var deposit: Int = 0) {

    /**
     * @param days - for backwards compatibility
     * The price was supposed to change for a different
     * rental duration
     */
    fun getPricePerDayFor(days: Int) = this.price //when (days) {
//        in 1 .. 3 -> this.price
//        in 4 .. 8 -> this.price - 7
//        in 9 .. 15 -> this.price - 15
//        in 16 .. Int.MAX_VALUE -> this.price - 20
//        else -> throw IllegalArgumentException("Cannot get get price per day for $days (NON-POSITIVE) days!!")
//    }
}
//
//fun car(function: Car.() -> Unit): Car {
//    val car = Car()
//    car.function()
//    return car
//}
