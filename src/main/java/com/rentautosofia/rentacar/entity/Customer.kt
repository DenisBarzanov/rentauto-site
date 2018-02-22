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
@Table(name = "customers")
data class Customer(@Column var phoneNumber: String = "",
                    @Column var name: String = "") {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0
}