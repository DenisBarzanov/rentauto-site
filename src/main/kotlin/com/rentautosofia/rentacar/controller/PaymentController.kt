package com.rentautosofia.rentacar.controller

import org.springframework.ui.Model
import javax.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import com.paypal.base.rest.PayPalRESTException
import com.rentautosofia.rentacar.config.PaypalPaymentIntent
import com.rentautosofia.rentacar.config.PaypalPaymentMethod
import com.rentautosofia.rentacar.repository.RentedCarRepository
import com.rentautosofia.rentacar.service.PaypalService
import com.rentautosofia.rentacar.util.URLUtils
import com.rentautosofia.rentacar.util.findOne
import org.springframework.util.MultiValueMap
import com.rentautosofia.rentacar.repository.CarRepository
import com.rentautosofia.rentacar.util.daysTill
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.validation.BindingResult


@Controller
class PaymentController(@Autowired
                        private val paypalService: PaypalService) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    private lateinit var rentedCarRepository: RentedCarRepository
    @Autowired
    private lateinit var carRepository: CarRepository

    @RequestMapping(method = [(RequestMethod.POST)], value = ["/pay"])
    fun pay(model: Model, request: HttpServletRequest, @RequestBody multiParams: MultiValueMap<String, String>): String {
        val params = multiParams.toSingleValueMap()
//        val depositAmount: Int
        //depositAmount = //if (params["deposit"] != null) {
            //params["deposit"]!!.toInt()
        //} else {
        val booking = rentedCarRepository.findOne(params["orderId"]!!.toInt())!!
        val car = carRepository.findOne(booking.carId)!!
//        depositAmount = car.getPricePerDayFor(booking.startDate daysTill booking.endDate)
        //}
        val cancelUrl = URLUtils.getBaseURl(request) + PAYPAL_CANCEL_URL
        val successUrl = URLUtils.getBaseURl(request) + PAYPAL_SUCCESS_URL

        try {
            val payment = paypalService.createPayment(
//                    depositAmount.toDouble(),
                    100.00,
                    "EUR",
                    PaypalPaymentMethod.paypal,
                    PaypalPaymentIntent.sale,
                    "RentAuto Sofia deposit payment",
                    cancelUrl,
                    "$successUrl?orderId=${booking.id}")
            for (links in payment.links) {
                if (links.rel == "approval_url") {
                    return "redirect:" + links.href
                }
            }
        } catch (e: PayPalRESTException) {
            log.error(e.message)
        }

        return "redirect:/"
    }

    @RequestMapping(method = [RequestMethod.GET], value = [PAYPAL_CANCEL_URL])
    fun cancelPay(model: Model): String {
        model.addAttribute("view", "cancel")
        return "client-base-layout"
    }

    @RequestMapping(method = [RequestMethod.GET], value = [PAYPAL_SUCCESS_URL])
    fun successPay(@RequestParam("orderId") orderId: Int, model: Model, @RequestParam("paymentId") paymentId: String, @RequestParam("PayerID") payerId: String): String {
        try {
            val payment = paypalService.executePayment(paymentId, payerId)
            if (payment.state == "approved") {

                print("\n\n\nDEPOSIT PAYED for id: $orderId\n\n\n")
                val booking = rentedCarRepository.findOne(orderId)
                booking!!.payedDeposit = true
                rentedCarRepository.saveAndFlush(booking) // Make it payed

                model.addAttribute("view", "success")
                return "client-base-layout"
            }
        } catch (e: PayPalRESTException) {
            log.error(e.message)
        }

        return "redirect:/"
    }

    companion object {
        const val PAYPAL_SUCCESS_URL = "/pay/success"
        const val PAYPAL_CANCEL_URL = "/pay/cancel"
    }

}
