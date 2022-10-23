package com.devpath.service

import com.devpath.constants.Constants.Companion.WELCOME
import org.springframework.stereotype.Service

@Service
class HomeService {
    fun welcome(): String {
        return WELCOME
    }
}
