/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.rentautosofia.rentacar.entity.Car
 *  javax.persistence.Column
 *  javax.persistence.Entity
 *  javax.persistence.GeneratedValue
 *  javax.persistence.GenerationType
 *  javax.persistence.Id
 *  javax.persistence.Table
 */
package com.rentautosofia.rentacar.entity

import javax.persistence.*

@Entity
@Table(name = "cars")

data class Car(@Column var name: String = "",
               @Column open var price: Int = 0,
               @Column(length = 1000) var imgURL: String = "") {

    fun getPriceForPeriodPerDay(days: Int) = when (days) {
        in 0..3 -> this.price
        in 3..8 -> this.price - 7
        in 8..15 -> this.price - 15
        in 15..Int.MAX_VALUE -> this.price - 20
        else -> 0
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Int = 0

    @ManyToMany(cascade = [CascadeType.ALL])
    @JoinTable(name = "booked_cars",
            joinColumns = [(JoinColumn(name = "car_id", referencedColumnName = "id"))],
            inverseJoinColumns = [JoinColumn(name = "customer_id", referencedColumnName = "id")])
    lateinit var customers: Set<Customer>
}

