package com.rentautosofia.rentacar.entity

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "booked_cars")
class BookedCar(@Column(name = "car_id") var carId: Int = 0,
                @Column(name = "customer_id") var customerId: Int = 0,
                @Column var startDate: Date = Date(),
                @Column var endDate: Date = Date()) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0
}