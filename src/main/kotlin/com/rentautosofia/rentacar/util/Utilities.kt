package com.rentautosofia.rentacar.util

import com.rentautosofia.rentacar.entity.BaseBooking
import com.rentautosofia.rentacar.entity.Car
import com.rentautosofia.rentacar.entity.RequestedCar
import com.rentautosofia.rentacar.repository.RentedCarRepository
import com.rentautosofia.rentacar.repository.RequestedCarRepository
import org.springframework.data.repository.CrudRepository
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

val format = SimpleDateFormat("dd-MM-yyyy")

infix fun Date.daysTill(otherDate: Date): Int {
    val difference = if (this.time >= otherDate.time)
        this.time - otherDate.time else otherDate.time - this.time
    return TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS).toInt()
}

fun Date.isBetween(startDate: Date, endDate: Date) : Boolean {
    return (this.after(startDate) or (this == startDate)) and
            (this.before(endDate) or (this == endDate))
}

fun RentedCarRepository.findAllIdsOfBookedCarsBetween(startDate: Date, endDate: Date): List<Int> {
    val allBookedCars = this.findAll()
    val bookedInPeriod = allBookedCars.filter {
        it.startDate.isBetween(startDate, endDate) or
                it.endDate.isBetween(startDate, endDate)
    }
    return bookedInPeriod.map { it.carId }
}
fun List<BaseBooking>.getIds(): List<Int> {
    return map { it.id }
}

fun getDateFrom(dateString: String) : Date{
    return format.parse(dateString)
}

fun Date.getProperFormat(): String {
    return format.format(this)
}

fun <T> CrudRepository<T, Int>.findOne(id: Int): T? {
    val entity = this.findById(id)
    return if (entity.isPresent) entity.get() else null
}

fun RequestedCarRepository.hasBooking(booking: RequestedCar): Boolean {
    val allBookings = this.findAll()

    for (current_booking in allBookings) {
        if (current_booking == booking) {
            return true
        }
    }
    return false
}
