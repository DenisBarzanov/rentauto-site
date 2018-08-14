package com.rentautosofia.rentacar.entity

import java.util.*
import javax.persistence.*

@Entity
data class Booking(override var carId: Int = 0,
                   override var customerId: Int = 0,
                   override var startDate: Date = Date(),
                   override var endDate: Date = Date(),
                   @Column
                   var earnest: Int = 0,
                   @Column
                   var deposit: Int = 0,
                   override var id: Int = 0,
                   @Column override var pricePerDay: Int = 0,
                   @Column var notes: String = "") : BaseBooking(carId, customerId, startDate, endDate)

fun booking(function: Booking.() -> Unit): Booking {
    val booking = Booking()
    booking.function()
    return booking
}
