/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.rentautosofia.rentacar.entity.Car
 *  com.rentautosofia.rentacar.repository.CarRepository
 *  org.springframework.data.jpa.repository.JpaRepository
 */
package com.rentautosofia.rentacar.repository

import com.rentautosofia.rentacar.entity.Car
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CarRepository : JpaRepository<Car, Int>

