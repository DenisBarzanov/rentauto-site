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
package com.rentautosofia.rentacar.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="cars")
public class Car {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable=false)
    private String name;
    @Column(nullable=false)
    private String price;
    @Column(nullable=false, length=1000)
    private String imgURL;

    public Car() {
    }

    public Car(String name, String price, String imgURL) {
        this.name = name;
        this.price = price;
        this.imgURL = imgURL;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImgURL() {
        return this.imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
}

