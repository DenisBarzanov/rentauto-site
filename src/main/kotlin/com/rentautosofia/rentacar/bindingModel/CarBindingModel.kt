package com.rentautosofia.rentacar.bindingModel

import com.rentautosofia.rentacar.transmission.Transmission
import javax.validation.constraints.Size

class CarBindingModel(@Size(min = 1)
                      var name: String = "",
                      @Size(min = 1)
                      var price: Int = 0,
                      @Size(min = 1)
                      var imgURL: String = "",
                      var LPG: Boolean? = false,
                      var transmission: Transmission = Transmission.MANUAL)
