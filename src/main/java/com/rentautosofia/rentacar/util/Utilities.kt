package com.rentautosofia.rentacar.util

import com.rentautosofia.rentacar.entity.BaseBooking
import com.rentautosofia.rentacar.repository.RentedCarRepository
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

val format = SimpleDateFormat("dd-MM-yyyy")

fun Date.getDifferenceInDaysTill(otherDate: Date): Int {
    val difference = if (this.time >= otherDate.time)
        this.time - otherDate.time else otherDate.time - this.time
    return TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS).toInt()
}

fun Date.isBetween(startDate: Date, endDate: Date) : Boolean {
    return this.after(startDate).and(this.before(endDate))
}

fun RentedCarRepository.findAllIdsOfBookedCarsBetween(startDate: Date, endDate: Date): List<Int> {
    val allBookedCars = this.findAll()
    val bookedInPeriod = allBookedCars.filter {
        it.startDate.isBetween(startDate, endDate)
                .or(it.endDate.isBetween(startDate, endDate))
    }
    return bookedInPeriod.map { it.carId }
}
fun List<BaseBooking>.getIds(): List<Int> {
    return map { it.id }
}
//fun WaitingBookedCarRepository.findAllCustomerIds() : List<Int> {
//    val all = this.findAll()
//    val customerIds = mutableListOf<Int>()
//    all.mapTo(customerIds) { it.customerId }
//    return customerIds
//}

fun getDateFromString(dateString: String) : Date{
    return format.parse(dateString)
}
fun Date.formatProperly(): String {
    return format.format(this)
}
//fun CustomerRepository.findOneByPhoneNumber(phoneNumber: String): Customer? {
//    val customers = this.findAll()
//    return customers.firstOrNull { it.phoneNumber == phoneNumber }
//}
//fun WaitingBookedCarRepository.findOneByCustomerId(carId: Int): BookedCar? {
//    val allWaitingBookedCars = this.findAll()
//    return allWaitingBookedCars.firstOrNull { it.carId == carId }
//}
