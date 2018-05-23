package com.rentautosofia.rentacar.controller

import javax.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import com.paypal.base.rest.PayPalRESTException
import com.rentautosofia.rentacar.config.PaypalPaymentIntent
import com.rentautosofia.rentacar.config.PaypalPaymentMethod
import com.rentautosofia.rentacar.service.PaypalService
import com.rentautosofia.rentacar.util.URLUtils

@Controller
class PaymentController(@Autowired
                        private val paypalService: PaypalService) {

    private val log = LoggerFactory.getLogger(javaClass)

    @RequestMapping(method = [(RequestMethod.POST)], value = ["/pay"])
    fun pay(request: HttpServletRequest): String {
        val cancelUrl = URLUtils.getBaseURl(request) + PAYPAL_CANCEL_URL
        val successUrl = URLUtils.getBaseURl(request) + PAYPAL_SUCCESS_URL
        try {
            val payment = paypalService.createPayment(
                    200.00,
                    "BGN",
                    PaypalPaymentMethod.paypal,
                    PaypalPaymentIntent.sale,
                    "RentAuto Sofia deposit payment",
                    cancelUrl,
                    successUrl)
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
    fun cancelPay(): String {
        return "cancel"
    }

    @RequestMapping(method = [RequestMethod.GET], value = [PAYPAL_SUCCESS_URL])
    fun successPay(@RequestParam("paymentId") paymentId: String, @RequestParam("PayerID") payerId: String): String {
        try {
            val payment = paypalService.executePayment(paymentId, payerId)
            if (payment.state == "approved") {
                return "success"
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