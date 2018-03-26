package com.rentautosofia.rentacar.util

import com.rentautosofia.rentacar.entity.RequestedCar
import com.rentautosofia.rentacar.repository.CarRepository
import com.rentautosofia.rentacar.repository.CustomerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.SimpleMailMessage
import org.springframework.stereotype.Component
import org.springframework.mail.javamail.JavaMailSender



@Component
class InformManager {
    @Autowired
    lateinit var emailSender: JavaMailSender
    @Autowired
    lateinit var carRepository: CarRepository
    @Autowired
    lateinit var customerRepository: CustomerRepository

    fun informManagerWith(requestedCar: RequestedCar) {
        val message = SimpleMailMessage()
        val car = carRepository.findOne(requestedCar.carId)
        val customer = customerRepository.findOne(requestedCar.customerId)
        val price = car!!.getPricePerDayFor(requestedCar.startDate daysTill requestedCar.endDate)
        message.setTo("denbar@abv.bg")
        message.setSubject("Нов човек си поръча кола!")
        message.setText("Име на кола: ${car.name}\n" +
                "Дата на взимане: ${requestedCar.startDate}\n" +
                "Дата на връщане: ${requestedCar.endDate}\n" +
                "Цена: $price\n" +
                "Tелефон: ${customer!!.phoneNumber}" +
                if(customer.name.isNotEmpty()) "Име: ${customer.name}" else "")
        emailSender.send(message)
    }
}