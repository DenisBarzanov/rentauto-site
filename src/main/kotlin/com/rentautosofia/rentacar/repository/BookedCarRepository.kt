package com.rentautosofia.rentacar.repository

import com.rentautosofia.rentacar.entity.Booking
import org.springframework.transaction.annotation.Transactional

@Transactional
interface BookingRepository : BaseBookingRepository<Booking>
