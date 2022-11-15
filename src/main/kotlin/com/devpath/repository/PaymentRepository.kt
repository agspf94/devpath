package com.devpath.repository

import com.devpath.entity.Payment
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentRepository: JpaRepository<Payment, Int>
