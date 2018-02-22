package com.rentautosofia.rentacar.entity

import java.util.*
import javax.persistence.*

@Entity
@Inheritance
open class BaseBooking(@Column open var carId: Int = 0,
                       @Column open var customerId: Int = 0,
                       @Column open var startDate: Date = Date(),
                       @Column open var endDate: Date = Date()) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Int = 0
}