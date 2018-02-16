package com.rentautosofia.rentacar.util

import com.rentautosofia.rentacar.entity.BookedCar
import com.rentautosofia.rentacar.entity.Customer
import com.rentautosofia.rentacar.repository.CarRepository
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

    fun informManagerWith(customer: Customer, bookedCar: BookedCar, price: Int, phoneNumber: String) {
        val message = SimpleMailMessage()
        val car = carRepository.findOne(bookedCar.carId)
        message.to = arrayOf("denbar@abv.bg")
        message.subject = "Нов човек си поръча кола!"
        message.text =  "Име на кола: ${car.name}\n" +
                "Дата на взимане: ${bookedCar.startDate}\n" +
                "Дата на връщане: ${bookedCar.endDate}\n" +
                "Цена: $price\n" +
                "Tелефон: $phoneNumber"
        emailSender.send(message)
    }
}