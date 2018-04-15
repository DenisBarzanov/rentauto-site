package com.rentautosofia.rentacar.repository

import com.rentautosofia.rentacar.entity.Car
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CarRepository : JpaRepository<Car, Int>

