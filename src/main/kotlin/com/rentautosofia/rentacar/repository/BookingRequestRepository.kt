package com.rentautosofia.rentacar.repository

import com.rentautosofia.rentacar.entity.BookingRequest
import org.springframework.transaction.annotation.Transactional

@Transactional
interface BookingRequestRepository : BookingRepository<BookingRequest>