package com.rentautosofia.rentacar.scheduled

import com.rentautosofia.rentacar.repository.BookingRepository
import com.rentautosofia.rentacar.repository.BookingRequestRepository
import com.rentautosofia.rentacar.repository.CustomerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*
import com.rentautosofia.rentacar.util.truncateDay
import org.slf4j.LoggerFactory


@Component
class ScheduledTasks @Autowired
constructor(private val bookingRepository: BookingRepository,
            private val bookingRequestRepository: BookingRequestRepository,
            private val customerRepository: CustomerRepository) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000) // 1 day
    fun deleteDatedBookings() {
        val today = Date().truncateDay()

        bookingRepository.let {
            val forDeletion = it.findAll().filter { booking ->
                today.after(booking.endDate) // endDate has already passed
            }
            logger.warn("Deleted obsolete booking records: $forDeletion")
            it.deleteAll(forDeletion)
        }
        bookingRequestRepository.let {
            val forDeletion = it.findAll().filter { booking ->
                today.after(booking.endDate) // endDate has already passed
            }
            logger.warn("Deleted obsolete bookingRequest records: $forDeletion")
            it.deleteAll(forDeletion)
        }
        customerRepository.let {
            val forDeletion = it.findAll().filter { customer ->
                (customer.id !in bookingRequestRepository.findAll().map { it.customerId }) and
                        (customer.id !in bookingRepository.findAll().map { it.customerId })
            }
            logger.warn("Deleted customers: $forDeletion")
            it.deleteAll(forDeletion)
        }
    }
}
