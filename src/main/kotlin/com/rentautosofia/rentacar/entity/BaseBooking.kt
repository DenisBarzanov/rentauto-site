package com.rentautosofia.rentacar.entity

import com.rentautosofia.rentacar.util.DataAccessUtils
import com.rentautosofia.rentacar.util.daysTill
import com.rentautosofia.rentacar.util.findOne
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

    @Transient // hibernate will ignore it
    var totalPrice: Int = 0
        get() {
            val car = DataAccessUtils.carRepository.findOne(this.carId)!!
            val days = startDate daysTill endDate
            return pricePerDay * days + car.deposit
        }
        private set

    @Transient // hibernate will ignore it
    open var pricePerDay: Int = 0
        get() {
            val car = DataAccessUtils.carRepository.findOne(this.carId)!!
            val days = startDate daysTill endDate
            return car.getPricePerDayFor(days)
        }
}