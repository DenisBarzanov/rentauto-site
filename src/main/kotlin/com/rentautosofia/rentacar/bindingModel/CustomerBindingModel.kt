package com.rentautosofia.rentacar.bindingModel

import javax.validation.constraints.Size

data class CustomerBindingModel(@Size(min = 6)
                                var phoneNumber: String,
                                @Size(min = 2)
                                var name: String) {

    constructor() : this("", "")
}