package com.rentautosofia.rentacar.util

import com.rentautosofia.rentacar.entity.BookedCar
import com.rentautosofia.rentacar.entity.Customer
import com.rentautosofia.rentacar.repository.BookedCarRepository
import com.rentautosofia.rentacar.repository.CustomerRepository
import org.springframework.mail.SimpleMailMessage
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun Date.getDifferenceInDaysTill(otherDate: Date): Int {
    val difference = if (this.time >= otherDate.time)
        this.time - otherDate.time else otherDate.time - this.time
    return TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS).toInt()
}

fun Date.isBetween(startDate: Date, endDate: Date) : Boolean {
    return this.after(startDate).and(this.before(endDate))
}

fun BookedCarRepository.findAllIdsOfBookedCarsBetween(startDate: Date, endDate: Date): List<Int> {
    val allBookedCars = this.findAll()
    val bookedInPeriod = allBookedCars.filter {
        it.startDate.isBetween(startDate, endDate)
                .or(it.endDate.isBetween(startDate, endDate))
    }
    return bookedInPeriod.getIds()
}
fun List<BookedCar>.getIds(): List<Int> {
    val ids = mutableListOf<Int>()
    this.mapTo(ids) { it.carId }
    return ids
}
fun getDateFromString(dateString: String) : Date{
    val format = SimpleDateFormat("dd-MM-yyyy")
    return format.parse(dateString)
}
//fun CustomerRepository.findOneByPhoneNumber(phoneNumber: String): Customer? {
//    val customers = this.findAll()
//    return customers.firstOrNull { it.phoneNumber == phoneNumber }
//}
