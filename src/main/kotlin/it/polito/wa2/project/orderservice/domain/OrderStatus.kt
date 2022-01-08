package it.polito.wa2.project.orderservice.domain

import com.fasterxml.jackson.annotation.JsonCreator

enum class OrderStatus {
    ISSUED,
    DELIVERING,
    DELIVERED,
    FAILED,
    CANCELED;

    companion object {
        private val mapping: MutableMap<String, OrderStatus> = mutableMapOf(
            "issued" to ISSUED,
            "Issued" to ISSUED,
            ISSUED.name to ISSUED,
            "0" to ISSUED,

            "delivering" to DELIVERING,
            "Delivering" to DELIVERING,
            DELIVERING.name to DELIVERING,
            "1" to DELIVERING,

            "delivered" to DELIVERED,
            "Delivered" to DELIVERED,
            DELIVERED.name to DELIVERED,
            "2" to DELIVERED,

            "failed" to FAILED,
            "Failed" to FAILED,
            FAILED.name to FAILED,
            "3" to FAILED,

            "canceled" to CANCELED,
            "Canceled" to CANCELED,
            CANCELED.name to CANCELED,
            "4" to FAILED
        )

        @JsonCreator
        fun fromString(value:String): OrderStatus? = mapping.get(value)
    }

}