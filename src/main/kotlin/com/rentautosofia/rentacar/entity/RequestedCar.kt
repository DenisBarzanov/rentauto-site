package com.rentautosofia.rentacar.entity

import java.util.*
import javax.persistence.*

@Entity
data class RequestedCar(override var carId: Int = 0,
                        override var customerId: Int = 0,
                        override var startDate: Date = Date(),
                        override var endDate: Date = Date()) : BaseBooking(carId, customerId, startDate, endDate) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Int = 0
}