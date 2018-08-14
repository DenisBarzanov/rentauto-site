package com.rentautosofia.rentacar.scheduled

import com.rentautosofia.rentacar.repository.BookedCarRepository
import com.rentautosofia.rentacar.repository.BookingRequestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*
import com.rentautosofia.rentacar.util.truncateDay

@Component
class ScheduledTasks @Autowired
constructor(private val bookedCarRepository: BookedCarRepository,
            private val bookingRequestRepository: BookingRequestRepository) {

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000) // 1 day
    fun deleteDatedBookings() {
        val today = Date().truncateDay()
        bookedCarRepository.let {
            it.deleteAll(it.findAll().filter { booking ->
                today.after(booking.endDate) // endDate has already passed
            })
        }
        bookingRequestRepository.let {
            it.deleteAll(it.findAll().filter { bookingRequest ->
                today.after(bookingRequest.endDate) // endDate has already passed
            })
        }
    }
}
