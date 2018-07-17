package com.rentautosofia.rentacar.service

import com.rentautosofia.rentacar.config.PaypalPaymentIntent
import com.rentautosofia.rentacar.config.PaypalPaymentMethod
import java.util.ArrayList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import com.paypal.api.payments.Amount
import com.paypal.api.payments.Payer
import com.paypal.api.payments.Payment
import com.paypal.api.payments.PaymentExecution
import com.paypal.api.payments.RedirectUrls
import com.paypal.api.payments.Transaction
import com.paypal.base.rest.APIContext
import com.paypal.base.rest.PayPalRESTException

@Service
class PaypalService {

    @Autowired
    private lateinit var apiContext: APIContext

    @Throws(PayPalRESTException::class)
    fun createPayment(
            total: Float?,
            currency: String,
            method: PaypalPaymentMethod,
            intent: PaypalPaymentIntent,
            description: String,
            cancelUrl: String,
            successUrl: String): Payment {
        val amount = Amount()
        amount.currency = currency
        amount.total = String.format("%.2f", total)

        val transaction = Transaction()
        transaction.description = description
        transaction.amount = amount

        val transactions = ArrayList<Transaction>()
        transactions.add(transaction)

        val payer = Payer()
        payer.paymentMethod = method.toString()

        val payment = Payment()
        payment.intent = intent.toString()
        payment.payer = payer
        payment.transactions = transactions
        val redirectUrls = RedirectUrls()
        redirectUrls.cancelUrl = cancelUrl
        redirectUrls.returnUrl = successUrl
        payment.redirectUrls = redirectUrls

        return payment.create(apiContext)
    }

    @Throws(PayPalRESTException::class)
    fun executePayment(paymentId: String, payerId: String): Payment {
        val payment = Payment()
        payment.id = paymentId
        val paymentExecute = PaymentExecution()
        paymentExecute.payerId = payerId
        return payment.execute(apiContext, paymentExecute)
    }
}