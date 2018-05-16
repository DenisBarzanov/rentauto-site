package com.rentautosofia.rentacar.repository

import com.rentautosofia.rentacar.entity.RequestedCar
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Transactional
interface RequestedCarRepository : BookingsRepository<RequestedCar> {
    fun hasBooking(booking: RequestedCar): Boolean {
        val allBookings = this.findAll()

        for (current_booking in allBookings) {
            if (current_booking == booking) {
                return true
            }
        }
        return false
    }
}