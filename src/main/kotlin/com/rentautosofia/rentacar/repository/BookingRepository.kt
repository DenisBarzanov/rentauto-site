package com.rentautosofia.rentacar.repository

import com.rentautosofia.rentacar.entity.BaseBooking
import org.springframework.data.jpa.repository.JpaRepository

interface BookingRepository<T : BaseBooking> : JpaRepository<T, Int>