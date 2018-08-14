package com.rentautosofia.rentacar.util

import com.rentautosofia.rentacar.entity.BaseBooking
import com.rentautosofia.rentacar.entity.BookingRequest
import com.rentautosofia.rentacar.repository.CarRepository
import com.rentautosofia.rentacar.repository.BookedCarRepository
import com.rentautosofia.rentacar.repository.BookingRequestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

val format = SimpleDateFormat("dd-MM-yyyy")

infix fun Date.daysTill(otherDate: Date): Int {
    val difference = if (this.time >= otherDate.time)
        this.time - otherDate.time else otherDate.time - this.time
    return TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS).toInt()
}
//
//fun Date.isBetween(startDate: Date, endDate: Date) : Boolean {
//    return this.after(startDate) and
//            this.before(endDate)
//}

//infix fun Date.addDays(days: Int): Date
//{
  //  Calendar cal = Calendar.getInstance();
//    cal.setTime(this);
  //  cal.add(Calendar.DATE, days); //minus number would decrement the days
    //return cal.getTime();
//}

fun BookedCarRepository.findAllIdsOfBookedCarsBetween(startDate: Date, endDate: Date): List<Int> {
    val allBookedCars = this.findAll()
    val bookedInPeriod = allBookedCars.filter {
//        !it.endDate.before(startDate) && !it.startDate.after(endDate)
        it.endDate.after(startDate) and it.startDate.before(endDate)
//        startDate.after(it.endDate) and endDate.before(it.startDate)
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

fun Date.truncateDay(): Date = getDateFrom(this.getProperFormat())

fun <T> CrudRepository<T, Int>.findOne(id: Int): T? {
    val entity = this.findById(id)
    return if (entity.isPresent) entity.get() else null
}

fun BookingRequestRepository.hasBooking(booking: BookingRequest): Boolean {
    val allBookings = this.findAll()
    for (current_booking in allBookings) {
        if (current_booking == booking) {
            return true
        }
    }
    return false
}

@Component
class DataAccessUtils {
    companion object {
        lateinit var bookedCarRepository: BookedCarRepository
        lateinit var carRepository: CarRepository
    }

    @Autowired
    private fun setRentedCarRepository(bookedCarRepository: BookedCarRepository) {
        DataAccessUtils.bookedCarRepository = bookedCarRepository
    }

    @Autowired
    private fun setCarRepository(carRepository: CarRepository) {
        DataAccessUtils.carRepository = carRepository
    }
}
