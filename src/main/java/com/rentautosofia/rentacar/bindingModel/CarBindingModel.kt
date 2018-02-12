/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.rentautosofia.rentacar.bindingModel.CarBindingModel
 *  javax.validation.constraints.NotNull
 *  javax.validation.constraints.Size
 */
package com.rentautosofia.rentacar.bindingModel

import javax.validation.constraints.Size

class CarBindingModel(@Size(min = 1)
                      var name: String,
                      @Size(min = 1)
                      var price: Int,
                      @Size(min = 1)
                      var imgURL: String) {
    constructor() : this("", 0, "") {}
}

