package com.rentautosofia.rentacar.entity

import java.util.*
import javax.persistence.*

@Entity
open class BookedCar(override var carId: Int = 0,
                     override var customerId: Int = 0,
                     override var startDate: Date = Date(),
                     override var endDate: Date = Date(),
                     var payedDeposit: Boolean? = false,
                     @Id
                     @GeneratedValue(strategy = GenerationType.IDENTITY)
                     override var id: Int = 0) : BaseBooking(carId, customerId, startDate, endDate)


fun bookedCar(function: BookedCar.() -> Unit): BookedCar {
    val bookedCar = BookedCar()
    bookedCar.function()
    return bookedCar
}
