package com.rentautosofia.rentacar.util

import com.rentautosofia.rentacar.entity.RequestedCar
import com.rentautosofia.rentacar.repository.CarRepository
import com.rentautosofia.rentacar.repository.CustomerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper




@Component
class ManagerInformer {
    @Autowired
    lateinit var emailSender: JavaMailSender
    @Autowired
    lateinit var carRepository: CarRepository
    @Autowired
    lateinit var customerRepository: CustomerRepository

    fun informManagerWith(requestedCar: RequestedCar) {
        val car = carRepository.findOne(requestedCar.carId)
        val customer = customerRepository.findOne(requestedCar.customerId)
        val days = requestedCar.startDate daysTill requestedCar.endDate
        val totalPrice = car!!.getPricePerDayFor(days) * days

        val mimeMessage = emailSender.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage, false, "utf-8")

        val html =
                """
            <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Untitled Document</title>
</head>

<body style="margin:0">
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" widtd="100%">
    <tr valign="top">
        <td height="30" align="center" bgcolor="#eeeeee">&nbsp;</td>
    </tr>
    <tr valign="top">
        <td align="center" bgcolor="#eeeeee">
            <table width="600" border="0" align="center" cellpadding="0" cellspacing="0" widtd="600">
                <tr>
                    <td bgcolor="#FFFFFF">&nbsp;</td>
                </tr>
                <tr>
                    <td align="center" bgcolor="#FFFFFF">
                        <table width="500" border="0" align="center" cellpadding="0" cellspacing="0"
                               style="font-family:Arial, Helvetica, sans-serif;" widtd="500">
                            <tr>
                                <td align="center" bgcolor="#333333" style="padding:15px 0"><h1
                                        style="color:#fff; font-size:24px; font-weight:bold; margin:0;">New Car Booking
                                    Request</h1></td>
                            </tr>
                            <tr>
                                <td align="left"><p
                                        style="font-size:14px; color:#555; font-weight:normal; line-height:20px;">
                                    <strong>Dear Admin</strong><br>
                                    Someone has send a request for car rental, here are the details.</p></td>
                            </tr>
                            <tr>
                                <td height="20"></td>
                            </tr>
                            <tr>
                                <td>
                                    <table width="500" border="0" cellspacing="0" cellpadding="5"
                                           style="font-size:14px; color:#888;">
                                        <tr>
                                            <td>Car</td>
                                            <td><strong>'${car.name}'</strong></td>
                                        </tr>
                                        <tr>
                                            <td>Дата на взимане</td>
                                            <td><strong>'${requestedCar.startDate}'</strong></td>
                                        </tr>
                                        <tr>
                                            <td>Дата на връщане</td>
                                            <td><strong>'${requestedCar.startDate}'</strong></td>
                                        </tr>
                                        <tr>
                                            <td>Name</td>
                                            <td><strong>'${
                                            if(customer?.name?.isNotEmpty() == true)
                                                customer.name
                                            else "Няма"
                                            }'</strong></td>
                                        </tr>
                                        <tr>
                                            <td>Phone</td>
                                            <td><strong>'${customer?.phoneNumber}'</strong></td>
                                        </tr>
                                        <tr>
                                            <td>TOTAL</td>
                                            <td><strong>&euro;'$totalPrice'</strong></td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td height="30" bgcolor="#FFFFFF">&nbsp;</td>
                </tr>
                <tr>
                    <td height="30" bgcolor="#F8C807">&nbsp;</td>
                </tr>
                <tr>
                    <td bgcolor="#F8C807">
                        <table width="500" border="0" align="center" cellpadding="0" cellspacing="0"
                               style="font-family:Arial, Helvetica, sans-serif;" widtd="350">
                            <tr>
                                <td align="center"><h1
                                        style="color:#fff; font-size:24px; font-weight:bold; margin:0 0 15px 0;">Car
                                    Rentals</h1></td>
                            </tr>
                            <tr>
                                <td align="center"><p
                                        style="font-size:14px; color:#fff; margin:0; font-weight:normal; line-height:20px;">
                                    Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla non enim ut nulla
                                    pulvinar sagittis. Duis varius sagittis pulvinar.</p></td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td height="30" bgcolor="#F8C807">&nbsp;</td>
                </tr>
                <tr>
                    <td bgcolor="#FFFFFF">&nbsp;</td>
                </tr>
                <tr>
                    <td align="center" bgcolor="#FFFFFF"
                        style="color:#b1b1b1; font-size:13px; font-family:Arial, Helvetica, sans-serif; font-weight:normal;">
                        &copy; 2018 Car Rentals | All Rights Reserved.
                    </td>
                </tr>
                <tr>
                    <td bgcolor="#FFFFFF">&nbsp;</td>
                </tr>
            </table>
        </td>
    </tr>
    <tr valign="top">
        <td height="30" align="center" bgcolor="#eeeeee">&nbsp;</td>
    </tr>
</table>
</body>
</html>'
            """
        mimeMessage.setContent(html, "text/html")

        helper.setTo("denbar@abv.bg")
        helper.setSubject("Нов човек си поръча кола!")

        emailSender.send(mimeMessage)
    }
}