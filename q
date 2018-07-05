[1mdiff --git a/src/main/kotlin/com/rentautosofia/rentacar/RentacarApplication.kt b/src/main/kotlin/com/rentautosofia/rentacar/RentacarApplication.kt[m
[1mindex 37c7412..7b5290d 100644[m
[1m--- a/src/main/kotlin/com/rentautosofia/rentacar/RentacarApplication.kt[m
[1m+++ b/src/main/kotlin/com/rentautosofia/rentacar/RentacarApplication.kt[m
[36m@@ -2,9 +2,7 @@[m [mpackage com.rentautosofia.rentacar[m
 [m
 import org.springframework.boot.autoconfigure.SpringBootApplication[m
 import org.springframework.boot.runApplication[m
[31m-import org.springframework.context.annotation.ComponentScan[m
 [m
[31m-//@ComponentScan("com.rentautosofia.rentacar.config")[m
 @SpringBootApplication[m
 class RentacarApplication[m
 [m
[1mdiff --git a/src/main/kotlin/com/rentautosofia/rentacar/controller/PaymentController.kt b/src/main/kotlin/com/rentautosofia/rentacar/controller/PaymentController.kt[m
[1mindex 0033d12..1e28906 100644[m
[1m--- a/src/main/kotlin/com/rentautosofia/rentacar/controller/PaymentController.kt[m
[1m+++ b/src/main/kotlin/com/rentautosofia/rentacar/controller/PaymentController.kt[m
[36m@@ -34,20 +34,21 @@[m [mclass PaymentController(@Autowired[m
     @RequestMapping(method = [(RequestMethod.POST)], value = ["/pay"])[m
     fun pay(model: Model, request: HttpServletRequest, @RequestBody multiParams: MultiValueMap<String, String>): String {[m
         val params = multiParams.toSingleValueMap()[m
[31m-        val depositAmount: Int[m
[32m+[m[32m//        val depositAmount: Int[m
         //depositAmount = //if (params["deposit"] != null) {[m
             //params["deposit"]!!.toInt()[m
         //} else {[m
         val booking = rentedCarRepository.findOne(params["orderId"]!!.toInt())!![m
         val car = carRepository.findOne(booking.carId)!![m
[31m-        depositAmount = car.getPricePerDayFor(booking.startDate daysTill booking.endDate)[m
[32m+[m[32m//        depositAmount = car.getPricePerDayFor(booking.startDate daysTill booking.endDate)[m
         //}[m
         val cancelUrl = URLUtils.getBaseURl(request) + PAYPAL_CANCEL_URL[m
         val successUrl = URLUtils.getBaseURl(request) + PAYPAL_SUCCESS_URL[m
 [m
         try {[m
             val payment = paypalService.createPayment([m
[31m-                    depositAmount.toDouble(),[m
[32m+[m[32m//                    depositAmount.toDouble(),[m
[32m+[m[32m                    100.00,[m
                     "EUR",[m
                     PaypalPaymentMethod.paypal,[m
                     PaypalPaymentIntent.sale,[m
[36m@@ -79,7 +80,7 @@[m [mclass PaymentController(@Autowired[m
             if (payment.state == "approved") {[m
 [m
                 print("\n\n\nDEPOSIT PAYED for id: $orderId\n\n\n")[m
[31m-                val booking = rentedCarRepository.findOne(orderId!!)[m
[32m+[m[32m                val booking = rentedCarRepository.findOne(orderId)[m
                 booking!!.payedDeposit = true[m
                 rentedCarRepository.saveAndFlush(booking) // Make it payed[m
 [m
[1mdiff --git a/src/main/kotlin/com/rentautosofia/rentacar/controller/admin/BookingController.kt b/src/main/kotlin/com/rentautosofia/rentacar/controller/admin/BookingController.kt[m
[1mindex 5d007eb..47f7704 100644[m
[1m--- a/src/main/kotlin/com/rentautosofia/rentacar/controller/admin/BookingController.kt[m
[1m+++ b/src/main/kotlin/com/rentautosofia/rentacar/controller/admin/BookingController.kt[m
[36m@@ -26,6 +26,13 @@[m [mconstructor(private val carRepository: CarRepository,[m
         model.addAttribute("view", "$PATH_ADMIN_BOOKING/all")[m
         val allBookings =[m
                 this.rentedCarRepository.findAll()[m
[32m+[m[32m        allBookings.sortWith(Comparator(fun (booking1: BookedCar, booking2: BookedCar): Int {[m
[32m+[m[32m            return when {[m
[32m+[m[32m                booking1.startDate.before(booking2.startDate) -> -1[m
[32m+[m[32m                booking1.startDate.after(booking2.startDate) -> 1[m
[32m+[m[32m                else -> 0[m
[32m+[m[32m            }[m
[32m+[m[32m        }))[m
         model.addAttribute("bookings", allBookings)[m
         return "base-layout"[m
     }[m
[1mdiff --git a/src/main/kotlin/com/rentautosofia/rentacar/entity/Customer.kt b/src/main/kotlin/com/rentautosofia/rentacar/entity/Customer.kt[m
[1mindex 08b68c1..b8c4825 100644[m
[1m--- a/src/main/kotlin/com/rentautosofia/rentacar/entity/Customer.kt[m
[1m+++ b/src/main/kotlin/com/rentautosofia/rentacar/entity/Customer.kt[m
[36m@@ -5,7 +5,6 @@[m [mimport javax.persistence.*[m
 @Entity[m
 @Table(name = "customers")[m
 data class Customer(@Column var phoneNumber: String = "",[m
[31m-                    @Column var name: String = "",[m
                     @Column var email: String = "",[m
                     @Id[m
                     @GeneratedValue(strategy = GenerationType.IDENTITY)[m
[1mdiff --git a/src/main/kotlin/com/rentautosofia/rentacar/entity/RequestedCar.kt b/src/main/kotlin/com/rentautosofia/rentacar/entity/RequestedCar.kt[m
[1mindex bf8d6af..8b12b00 100644[m
[1m--- a/src/main/kotlin/com/rentautosofia/rentacar/entity/RequestedCar.kt[m
[1m+++ b/src/main/kotlin/com/rentautosofia/rentacar/entity/RequestedCar.kt[m
[36m@@ -12,33 +12,22 @@[m [mdata class RequestedCar(override var carId: Int = 0,[m
                         @GeneratedValue(strategy = GenerationType.IDENTITY)[m
                         override var id: Int = 0) : BaseBooking(carId, customerId, startDate, endDate) {[m
 [m
[31m-[m
     override fun equals(other: Any?): Boolean {[m
         if (other !is RequestedCar) {[m
             return false[m
         }[m
         if ((other.carId == this.carId) and[m
                 (other.startDate == this.startDate) and[m
[31m-                (other.endDate == this.endDate) and [m
[31m-		(other.customerId == this.customerId)) {[m
[32m+[m[32m                (other.endDate == this.endDate) and[m
[32m+[m[32m                (other.customerId == this.customerId)) {[m
             return true[m
         }[m
         return false[m
     }[m
[31m-[m
[31m-    override fun hashCode(): Int {[m
[31m-        var result = carId[m
[31m-        result = 31 * result + customerId[m
[31m-        result = 31 * result + startDate.hashCode()[m
[31m-        result = 31 * result + endDate.hashCode()[m
[31m-        result = 31 * result + id[m
[31m-        return result[m
[31m-    }[m
 }[m
 [m
[31m-[m
[31m-fun requestedCar(function: RequestedCar.() -> Unit): RequestedCar {[m
[31m-    val requestedCar = RequestedCar()[m
[31m-    requestedCar.function()[m
[31m-    return requestedCar[m
[31m-}[m
[32m+[m[32m//fun requestedCar(function: RequestedCar.() -> Unit): RequestedCar {[m
[32m+[m[32m//    val requestedCar = RequestedCar()[m
[32m+[m[32m//    requestedCar.function()[m
[32m+[m[32m//    return requestedCar[m
[32m+[m[32m//}[m
[1mdiff --git a/src/main/kotlin/com/rentautosofia/rentacar/util/ManagerInformer.kt b/src/main/kotlin/com/rentautosofia/rentacar/util/ManagerInformer.kt[m
[1mindex d4dfac7..7d4bdc1 100644[m
[1m--- a/src/main/kotlin/com/rentautosofia/rentacar/util/ManagerInformer.kt[m
[1m+++ b/src/main/kotlin/com/rentautosofia/rentacar/util/ManagerInformer.kt[m
[36m@@ -84,14 +84,6 @@[m [mclass ManagerInformer {[m
                                                 <td><strong>'${requestedCar.endDate.getProperFormat()}'</strong></td>[m
                                             </tr>[m
                                             <tr>[m
[31m-                                                <td>Име на клиент</td>[m
[31m-                                                <td><strong>'${[m
[31m-                                                if(customer?.name?.isNotEmpty() == true)[m
[31m-                                                    customer.name[m
[31m-                                                else "Няма"[m
[31m-                                                }'</strong></td>[m
[31m-                                            </tr>[m
[31m-                                            <tr>[m
                                                 <td>Телефон на клиент</td>[m
                                                 <td><strong>'${customer?.phoneNumber}'</strong></td>[m
                                             </tr>                                            <tr>[m
[36m@@ -265,7 +257,7 @@[m [mclass ManagerInformer {[m
 						    Снимка на кола[m
 						</td>[m
 						<td>[m
[31m-						    <img src="${car?.imgURL}" />[m
[32m+[m						[32m    <img style="width: 300px;" src="${car.imgURL}" />[m
 						</td>[m
 					    <tr>[m
                                     </table>[m
[1mdiff --git a/src/main/kotlin/com/rentautosofia/rentacar/util/Utilities.kt b/src/main/kotlin/com/rentautosofia/rentacar/util/Utilities.kt[m
[1mindex 8cf035a..95dbdba 100644[m
[1m--- a/src/main/kotlin/com/rentautosofia/rentacar/util/Utilities.kt[m
[1m+++ b/src/main/kotlin/com/rentautosofia/rentacar/util/Utilities.kt[m
[36m@@ -27,9 +27,6 @@[m [mfun RentedCarRepository.findAllIdsOfBookedCarsBetween(startDate: Date, endDate:[m
     val bookedInPeriod = allBookedCars.filter {[m
         (it.endDate.before(startDate) or it.startDate.after(endDate))[m
                 .not()[m
[31m-[m
[31m-//        startDate.isBetween(it.startDate, it.endDate) or // misses one case[m
[31m-//                endDate.isBetween(it.startDate, it.endDate)[m
     }[m
     return bookedInPeriod.map { it.carId }[m
 }[m
[36m@@ -52,7 +49,6 @@[m [mfun <T> CrudRepository<T, Int>.findOne(id: Int): T? {[m
 [m
 fun RequestedCarRepository.hasBooking(booking: RequestedCar): Boolean {[m
     val allBookings = this.findAll()[m
[31m-    // Todo if dates and car are equal between 2 requests -> the second one would sadly be ignored[m
     for (current_booking in allBookings) {[m
         if (current_booking == booking) {[m
             return true[m
[1mdiff --git a/src/main/resources/static/css/style.css b/src/main/resources/static/css/style.css[m
[1mindex 7bcce91..75739fe 100644[m
[1m--- a/src/main/resources/static/css/style.css[m
[1m+++ b/src/main/resources/static/css/style.css[m
[36m@@ -1,3 +1,5 @@[m
[32m+[m[32m@import url('https://fonts.googleapis.com/css?family=Open+Sans:400,600,700|Roboto:300,400,500,700');[m
[32m+[m
 /*------------------------------------------[m
 Project Name : Car Rental[m
 Desgined By  : eCreative solutions[m
[36m@@ -21,6 +23,12 @@[m [mTable Of Index[m
 13. Footer css[m
 14. Media Quries css[m
 ------------------------------------------*/[m
[32m+[m[32m#paypal_btn {[m
[32m+[m[32m    margin: 10px 5px 0 5px;[m
[32m+[m[32m    color: white;[m
[32m+[m[32m    padding: 7px;[m
[32m+[m[32m    background-color: #3b7bbf;[m
[32m+[m[32m}[m
 [m
 .custom-input {[m
     padding: 12px 20px;[m
[36m@@ -31,7 +39,7 @@[m [mTable Of Index[m
     box-sizing: border-box;[m
 }[m
 [m
[31m-/* Modal PayPay form*/[m
[32m+[m[32m/* Modal PayPal form*/[m
 body {[m
     font-family: Arial, Helvetica, sans-serif;[m
 }[m
[36m@@ -107,8 +115,11 @@[m [mbody {[m
 [m
 .modal-header {[m
     padding: 2px 16px;[m
[31m-    background-color: #5cb85c;[m
[31m-    color: white !important;[m
[32m+[m[32m    background-color: #3b7bbf;[m
[32m+[m[32m}[m
[32m+[m
[32m+[m[32m.modal-header  h2 {[m
[32m+[m[32m    color: white;[m
 }[m
 [m
 .modal-body {[m
[36m@@ -117,7 +128,7 @@[m [mbody {[m
 [m
 .modal-footer {[m
     padding: 2px 16px;[m
[31m-    background-color: #5cb85c;[m
[32m+[m[32m    background-color: #3b7bbf;[m
     color: white;[m
 }[m
 [m
[36m@@ -176,7 +187,6 @@[m [mbody {[m
     }[m
 }[m
 [m
[31m-@import url('https://fonts.googleapis.com/css?family=Open+Sans:400,600,700|Roboto:300,400,500,700');[m
 html, body {[m
     min-height: 100%;[m
 }[m
[1mdiff --git a/src/main/resources/templates/admin/bookRequest/accept.html b/src/main/resources/templates/admin/bookRequest/accept.html[m
[1mindex d26d7f5..fa24f06 100644[m
[1m--- a/src/main/resources/templates/admin/bookRequest/accept.html[m
[1m+++ b/src/main/resources/templates/admin/bookRequest/accept.html[m
[36m@@ -6,7 +6,6 @@[m
                 <th>Име</th>[m
                 <th>Телефон</th>[m
                 <th>Email</th>[m
[31m-                <th>Име на клиент</th>[m
                 <th>Начална дата</th>[m
                 <th>Крайна дата</th>[m
             </tr>[m
[36m@@ -15,7 +14,6 @@[m
                 <td>[[${car.name}]]</td>[m
                 <td>[[${customer.phoneNumber}]]</td>[m
                 <td>[[${customer.email}]]</td>[m
[31m-                <td th:text="${customer?.name}">Няма</td>[m
                 <td>[[${#dates.format(requested.startDate, 'dd-MM-yyyy')}]]</td>[m
                 <td>[[${#dates.format(requested.endDate, 'dd-MM-yyyy')}]]</td>[m
             </tr>[m
[1mdiff --git a/src/main/resources/templates/admin/booking/delete.html b/src/main/resources/templates/admin/booking/delete.html[m
[1mindex bced810..f265f93 100644[m
[1m--- a/src/main/resources/templates/admin/booking/delete.html[m
[1m+++ b/src/main/resources/templates/admin/booking/delete.html[m
[36m@@ -20,12 +20,12 @@[m
     <div>[m
         <table>[m
             <tr>[m
[31m-                <th>Име на клиент</th>[m
                 <th>Телефон на клиент</th>[m
[32m+[m[32m                <th>Email на клиент</th>[m
             </tr>[m
             <tr>[m
[31m-                <td>[[${customer.name}]]</td>[m
                 <td>[[${customer.phoneNumber}]]</td>[m
[32m+[m[32m                <td>[[${customer.email}]]</td>[m
             </tr>[m
         </table>[m
     </div>[m
[1mdiff --git a/src/main/resources/templates/admin/customer/all.html b/src/main/resources/templates/admin/customer/all.html[m
[1mindex a7249d7..814bcf9 100644[m
[1m--- a/src/main/resources/templates/admin/customer/all.html[m
[1m+++ b/src/main/resources/templates/admin/customer/all.html[m
[36m@@ -8,7 +8,6 @@[m
     <div>[m
         <table>[m
             <tr>[m
[31m-                <th>Име</th>[m
                 <th>Телефон</th>[m
                 <th>Email</th>[m
                 <th>Опция</th>[m
[36m@@ -16,7 +15,6 @@[m
 [m
             <th:block th:each="customer : ${customers}">[m
             <tr>[m
[31m-                <td th:text="${customer.name}"></td>[m
                 <td th:text="${customer.phoneNumber}"></td>[m
                 <td th:text="${customer.email}"></td>[m
                 <td>[m
[1mdiff --git a/src/main/resources/templates/admin/customer/create.html b/src/main/resources/templates/admin/customer/create.html[m
[1mindex 7fba131..c90c98b 100644[m
[1m--- a/src/main/resources/templates/admin/customer/create.html[m
[1m+++ b/src/main/resources/templates/admin/customer/create.html[m
[36m@@ -3,8 +3,6 @@[m
     <div>[m
         <form th:action="@{/admin/customer/create}" method="POST">[m
             <div>[m
[31m-                <label for="name">Име</label>[m
[31m-                <input type="text" id="name" name="name" th:value="${customer.name}" />[m
 [m
                 <label for="phoneNumber">Телефон</label>[m
                 <input type="text" id="phoneNumber" name="phoneNumber" th:value="${customer.phoneNumber}" />[m
[1mdiff --git a/src/main/resources/templates/admin/customer/edit.html b/src/main/resources/templates/admin/customer/edit.html[m
[1mindex 345e904..aeac51f 100644[m
[1m--- a/src/main/resources/templates/admin/customer/edit.html[m
[1m+++ b/src/main/resources/templates/admin/customer/edit.html[m
[36m@@ -4,9 +4,6 @@[m
         <form th:action="@{/admin/customer/{id}/edit(id=${customer.id})}" method="POST">[m
             <div>[m
 [m
[31m-                <label for="name">Име</label>[m
[31m-                <input type="text" id="name" name="name" th:value="${customer.name}" />[m
[31m-[m
                 <label for="phoneNumber">Телефон</label>[m
                 <input type="text" id="phoneNumber" name="phoneNumber" th:value="${customer.phoneNumber}" />[m
 [m
[1mdiff --git a/src/main/resources/templates/fragments/nav.html b/src/main/resources/templates/fragments/nav.html[m
[1mindex 8e6d228..a367cfc 100644[m
[1m--- a/src/main/resources/templates/fragments/nav.html[m
[1m+++ b/src/main/resources/templates/fragments/nav.html[m
[36m@@ -29,7 +29,7 @@[m
 [m
                 </li>[m
                 <li>[m
[31m-                    <a href="javascript:void(0)" id="myBtn" class="btn btn-default" style="margin: 10px 5px 0 5px; color: gray;padding: 7px">[m
[32m+[m[32m                    <a href="javascript:void(0)" id="paypal_btn" class="btn btn-default">[m
                         <i class="fa fa-paypal" aria-hidden="true">Депозит</i>[m
                     </a>[m
                 </li>[m
[36m@@ -61,7 +61,7 @@[m
             <form th:action="@{/pay}" method="POST">[m
                 <label for="orderId">ID на поръчка</label>[m
                 <input type="number" class="custom-input" id="orderId" name="orderId" th:value="${orderId}" required="required" />[m
[31m-                <button type="submit" class="btn btn-success">Плати</button>[m
[32m+[m[32m                <button type="submit" class="btn btn-success" style="background-color: #3b7bbf; border-color: #3b7bbf;">Плати</button>[m
             </form>[m
         </div>[m
         <div class="modal-footer" style="text-align: center">[m
[36m@@ -76,7 +76,7 @@[m
     var modal = document.getElementById('myModal');[m
 [m
     // Get the button that opens the modal[m
[31m-    var btn = document.getElementById("myBtn");[m
[32m+[m[32m    var btn = document.getElementById("paypal_btn");[m
 [m
     // Get the <span> element that closes the modal[m
     var span = document.getElementsByClassName("close")[0];[m
