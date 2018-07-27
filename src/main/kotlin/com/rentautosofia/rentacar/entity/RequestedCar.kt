package com.rentautosofia.rentacar.entity

import java.util.*
import javax.persistence.*

@Entity
data class BookingRequest(override var carId: Int = 0,
                          override var customerId: Int = 0,
                          override var startDate: Date = Date(),
                          override var endDate: Date = Date(),
                          @Id
                        @GeneratedValue(strategy = GenerationType.IDENTITY)
                        override var id: Int = 0) : BaseBooking(carId, customerId, startDate, endDate) {

    override fun equals(other: Any?): Boolean {
        if (other !is BookingRequest) {
            return false
        }
        if ((other.carId == this.carId) and
                (other.startDate == this.startDate) and
                (other.endDate == this.endDate) and
                (other.customerId == this.customerId)) {
            return true
        }
        return false
    }
}
