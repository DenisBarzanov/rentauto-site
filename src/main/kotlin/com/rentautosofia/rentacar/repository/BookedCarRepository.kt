package com.rentautosofia.rentacar.repository

import com.rentautosofia.rentacar.entity.BookedCar
import org.springframework.transaction.annotation.Transactional

@Transactional
interface BookedCarRepository : BookingRepository<BookedCar>
