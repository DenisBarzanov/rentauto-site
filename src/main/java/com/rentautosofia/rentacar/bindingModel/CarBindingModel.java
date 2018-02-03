/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.rentautosofia.rentacar.bindingModel.CarBindingModel
 *  javax.validation.constraints.NotNull
 *  javax.validation.constraints.Size
 */
package com.rentautosofia.rentacar.bindingModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CarBindingModel {
    @NotNull
    @Size(min=1)
    private String name;
    @NotNull
    @Size(min=1)
    private String price;
    @NotNull
    @Size(min=1)
    private String imgURL;

    public CarBindingModel() {
    }

    public CarBindingModel(String name, String price, String imgURL) {
        this.name = name;
        this.price = price;
        this.imgURL = imgURL;
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

