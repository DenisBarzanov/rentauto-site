package com.rentautosofia.rentacar.repository

import com.rentautosofia.rentacar.entity.BookedCar
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Transactional
interface RentedCarRepository : BookingsRepository<BookedCar>
